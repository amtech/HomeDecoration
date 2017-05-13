package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.utils.*;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.OrderReportItem;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Erp 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ErpService extends AbstractService implements InitializingBean, DisposableBean {


    ErpOrderRepository repository;

    EntityManager manager;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    OrderItemRepository orderItemRepository;



    @Autowired
    OrderItemWorkFlowRepository orderItemWorkFlowRepository;


    @Autowired
    WorkFlowMessageRepository workFlowMessageRepository;
    @Autowired
    OrderItemWorkFlowStateRepository orderItemWorkFlowStateRepository;

    @Autowired
    WorkFlowRepository workFlowRepository;


    @Autowired
    WorkFlowProductRepository workFlowProductRepository;

    @Autowired
    WorkFlowArrangerRepository workFlowArrangerRepository;
    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;


    @Autowired
    OrderRepository orderRepository;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;

    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;


    @Autowired
    OrderAuthRepository orderAuthRepository;


    @Override
    public void destroy() throws Exception {
        super.destroy();

        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        repository = new ErpOrderRepository(manager);
    }


    /**
     * 查找订单
     *
     * @param loginUser
     * @param key
     * @param salesId   是否制定业务员 -1 表示所有人
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public RemoteData<ErpOrder> findByKey(User loginUser, String key, long salesId, int pageIndex, int pageSize) {
        List<ErpOrder> result;
        int totalCount;
        if (loginUser.isAdmin() && salesId == -1) {
            result = repository.findOrders(key, pageIndex, pageSize);
            totalCount = repository.getOrderCountByKey(key);
        } else {
            List<String> salesNos = null;
            //查询所有
            OrderAuth orderAuth = orderAuthRepository.findFirstByUser_IdEquals(loginUser.id);
            if (orderAuth != null && !StringUtils.isEmpty(orderAuth.relatedSales)) {
                salesNos = userService.extractUserCodes(loginUser.id, salesId, orderAuth.relatedSales);
            }


            if (salesNos == null || salesNos.size() == 0) return wrapData();
            result = repository.findOrders(key, salesNos, pageIndex, pageSize);
            totalCount = repository.getOrderCountByKey(key, salesNos);
        }
        //进行业务员配对。

        for (ErpOrder erpOrder : result) {

            User user = userRepository.findFirstByCodeEquals(erpOrder.sal_no);
            if (user != null) {
                attachData(erpOrder, user);
            }

            Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);
            if (order != null) {
                attachData(erpOrder, order);
            }

        }


        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, result);

    }


//


    /**
     * 查询订单详情
     *
     * @param os_no
     * @return
     */
    public RemoteData<ErpOrderDetail> getOrderDetail(User loginUser, String os_no) {


        ErpOrderDetail orderDetail = new ErpOrderDetail();


        ErpOrder erpOrder = repository.findOrderByNO(os_no);

        Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);
        if (order != null) {
            attachData(erpOrder, order);
        }

        User user = userRepository.findFirstByCodeEquals(erpOrder.sal_no);
        if (user != null) {
            attachData(erpOrder, user);
        }


        orderDetail.erpOrder = erpOrder;
        orderDetail.items = searchOrderItems(loginUser, os_no);


        return wrapData(orderDetail);

    }


    /**
     * 查找指定订单号的订单项目
     *
     * @param loginUser
     * @param os_no
     * @return
     */
    public List<ErpOrderItem> searchOrderItems(User loginUser, String os_no) {
        List<ErpOrderItem> orderItems = repository.findItemsByOrderNo(os_no);
        bindDataToOrderItem(loginUser, orderItems);
        return orderItems;

    }


    @Transactional
    public RemoteData<ErpOrderItem> searchOrderItems(User loginUser, String key, int pageIndex, int pageSize) {


        int totalCount = repository.getCountOfSearchOrderItem(key);
        List<ErpOrderItem> orderItems = repository.findItemsByOrderNoLike(key, pageIndex, pageSize);
        bindDataToOrderItem(loginUser, orderItems);

        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, orderItems);

    }


    private void bindDataToOrderItem(User loginUser, List<ErpOrderItem> orderItems) {
        //从报价系统读取产品的单位信息，图片信息
        for (ErpOrderItem item : orderItems) {

            String productCode = item.prd_no;
            String pVersion = "";
            try {
                pVersion = item.id_no.substring(productCode.length() + 2);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            item.pVersion = pVersion;


            Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(item.prd_name, pVersion);
            //  item.prd_no

            if (product != null) {
                item.ut = product.pUnitName;
                item.productId = product.id;
                item.url = product.url;
                item.thumbnail = product.thumbnail;
                item.packAttaches = product.packAttaches;
                item.packageInfo = product.packInfo;


                WorkFlowProduct workFlowProduct = workFlowProductRepository.findFirstByProductIdEquals(product.id);
                if (workFlowProduct != null) {
                    item.produceType = workFlowProduct.produceType;


                    WorkFlowArranger workFlowArranger = workFlowArrangerRepository.findFirstByUserIdEquals(loginUser.id);
                    if (workFlowArranger == null) {
                        item.canArrange = false;
                    } else
                        item.canArrange =  workFlowArranger.purchase && item.produceType == ProduceType.PURCHASE || workFlowArranger.selfMade && item.produceType== ProduceType.SELF_MADE;

                } else {
                    item.produceType = ProduceType.NOT_SET;
                    item.canArrange = true;

                }

                item.hasProductWorkFlowSet = workFlowProduct != null;


            }


            // 附加数据
            OrderItem orderItem = orderItemRepository.findFirstByOsNoEqualsAndItmEquals(item.os_no, item.itm);
            if (orderItem != null) {

                item.id = orderItem.id;
                item.maitou = orderItem.maitou;
                item.guagou = orderItem.guagou;

                //如果订单无产品包装说明，默认使用产品的说明
                if (!StringUtils.isEmpty(orderItem.packageInfo))
                    item.packageInfo = orderItem.packageInfo;
                item.sendDate = orderItem.sendDate;
                item.verifyDate = orderItem.verifyDate;


                //订单流程数据
                item.workFlowDescribe = orderItem.workFlowDescribe;
                item.orderWorkFlowId = orderItem.orderWorkFlowId;


                //绑定订单跟踪数据
                OrderItemWorkFlow workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItem.id);
                if (workFlowOrderItem != null) {
                    item.workFlowDescribe = workFlowOrderItem.workFlowDiscribe;
                }


            }
        }
    }

    @Transactional
    public RemoteData<ErpOrderDetail> saveOrderDetail(User loginUser, ErpOrderDetail orderDetail) {

        ErpOrder erpOrder = orderDetail.erpOrder;

        Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);
        if (order == null) {
            order = new Order();
        }


        String oldAttaches = erpOrder.attaches;
        detachData(erpOrder, order);


        //附件处理
        order.attaches = AttachFileUtils.updateProductAttaches(erpOrder.attaches, oldAttaches, "ORDER_" + order.osNo, attachfilepath, tempFilePath);
        orderRepository.save(order);


        List<ErpOrderItem> newStockOutItems = orderDetail.items;

        for (ErpOrderItem item : newStockOutItems) {

            OrderItem orderItem = orderItemRepository.findFirstByOsNoEqualsAndItmEquals(item.os_no, item.itm);
            if (orderItem == null) {
                orderItem = new OrderItem();
            }
            detachData(item, orderItem);


            orderItemRepository.save(orderItem);
        }
        return getOrderDetail(loginUser, erpOrder.os_no);
    }

    /**
     * 剥离数据  保存本系统
     *
     * @param erpOrderItem
     */
    private void detachData(ErpOrderItem erpOrderItem, OrderItem orderItem) {
        if (erpOrderItem == null || orderItem == null) {
            return;
        }

        orderItem.maitou = erpOrderItem.maitou;
        orderItem.guagou = erpOrderItem.guagou;
        orderItem.packageInfo = erpOrderItem.packageInfo;
        orderItem.sendDate = erpOrderItem.sendDate;
        orderItem.verifyDate = erpOrderItem.verifyDate;
        orderItem.osNo = erpOrderItem.os_no;
        orderItem.itm = erpOrderItem.itm;


        orderItem.qty = erpOrderItem.qty;
        orderItem.ut = erpOrderItem.ut;
        orderItem.prdNo = erpOrderItem.prd_no;
        orderItem.pVersion = erpOrderItem.pVersion;
        orderItem.url = erpOrderItem.url;
        orderItem.batNo = erpOrderItem.bat_no;


    }


    /**
     * 剥离数据
     *
     * @param erpOrder
     * @param order
     */
    private void detachData(ErpOrder erpOrder, Order order) {
        if (erpOrder == null || order == null) {
            return;
        }


        order.cemai = erpOrder.cemai;
        order.zhengmai = erpOrder.zhengmai;
        order.neheimai = erpOrder.neheimai;
        order.memo = erpOrder.memo;
        order.osNo = erpOrder.os_no;
        order.attaches = erpOrder.attaches;


        order.zuomai = erpOrder.zuomai;
        order.youmai = erpOrder.youmai;
        order.sal_no = erpOrder.sal_no;
        order.sal_name = erpOrder.sal_name;
        order.sal_cname = erpOrder.sal_cname;
        order.cus_no = erpOrder.cus_no;

    }


    /**
     * 附加数据
     *
     * @param order
     * @param erpOrder
     */
    private void attachData(ErpOrder erpOrder, Order order) {
        if (order == null || erpOrder == null) {
            return;
        }


        erpOrder.cemai = order.cemai;
        erpOrder.zhengmai = order.zhengmai;
        erpOrder.neheimai = order.neheimai;
        erpOrder.zuomai = order.zuomai;
        erpOrder.youmai = order.youmai;
        erpOrder.memo = order.memo;
        erpOrder.attaches = order.attaches;


    }


    /**
     * 附加数据
     *
     * @param erpOrder
     * @param user
     */
    private void attachData(ErpOrder erpOrder, User user) {
        if (user == null || erpOrder == null) {
            return;
        }


        erpOrder.sal_no = user.code;
        erpOrder.sal_name = user.name;
        erpOrder.sal_cname = user.chineseName;


    }

    public RemoteData<ErpOrder> findByCheckDate(String key, String dateStart, String dateEnd, int pageIndex, int pageSize) {


        List<ErpOrder> result = repository.findOrderByKeyCheckDate(key, dateStart, dateEnd, pageIndex, pageSize);
        //进行业务员配对。

        for (ErpOrder erpOrder : result) {

            User user = userRepository.findFirstByCodeEquals(erpOrder.sal_no);
            if (user != null) {
                attachData(erpOrder, user);
            }

            Order order = orderRepository.findFirstByOsNoEquals(erpOrder.os_no);
            if (order != null) {
                attachData(erpOrder, order);
            }

        }
        int totalCount = repository.getOrderCountByKeyAndCheckDate(key, dateStart, dateEnd);
        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, result);
    }

    /**
     * 导出出货报表数据
     *
     * @param loginUser
     * @param salesId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public RemoteData<OrderReportItem> findItemByCheckDate(User loginUser, long salesId, String dateStart, String dateEnd) {

        List<String> salesNos = null;
        //查询所有
        OrderAuth orderAuth = orderAuthRepository.findFirstByUser_IdEquals(loginUser.id);
        if (orderAuth != null && !StringUtils.isEmpty(orderAuth.relatedSales)) {
            salesNos = userService.extractUserCodes(loginUser.id, salesId, orderAuth.relatedSales);
        }


        if (salesNos == null || salesNos.size() == 0) return wrapData();

        List<OrderItem> orderItems = orderItemRepository.findByVerifyDateGreaterThanEqualAndVerifyDateLessThanEqual(dateStart, dateEnd);


        List<OrderReportItem> items = new ArrayList<>();
        for (OrderItem orderitem : orderItems) {
            Order order = orderRepository.findFirstByOsNoEquals(orderitem.osNo);
            if (order != null) {
                //所有人并且是管理员的 所有记录都通过。 否则只接受指定业务员记录
                if ((salesId == -1 && loginUser.isAdmin()) || salesNos.indexOf(order.sal_no) > -1) {
                    OrderReportItem orderReportItem = new OrderReportItem();
                    bindReportData(orderReportItem, orderitem, order);
                    items.add(orderReportItem);
                }

            }
        }


        return wrapData(items);
    }

    /**
     * 绑定生成报表数据
     *
     * @param orderReportItem
     * @param orderItem
     * @param order
     */
    private void bindReportData(OrderReportItem orderReportItem, OrderItem orderItem, Order order) {
        orderReportItem.cus_no = order.cus_no;
        orderReportItem.os_no = order.osNo;
        orderReportItem.cus_prd_no = orderItem.batNo;
        orderReportItem.qty = orderItem.qty;
        orderReportItem.prd_no = orderItem.prdNo;
        orderReportItem.url = orderItem.url;
        orderReportItem.thumbnail = orderItem.thumbnail;
        orderReportItem.sendDate = orderItem.sendDate;
        orderReportItem.verifyDate = orderItem.verifyDate;
        orderReportItem.unit = orderItem.ut;

        orderReportItem.saleName = order.sal_name + " " + order.sal_cname;


    }


    /**
     * 更新d订单附件
     * 将附件从临时文件转移到附件文件夹下
     */
    public void updateAttachFiles() {


        Page<Order> page;
        Pageable pageable = constructPageSpecification(0, 100);
        page = orderRepository.findAll(pageable);
        handleList(page.getContent());

        while (page.hasNext()) {
            pageable = page.nextPageable();
            page = orderRepository.findAll(pageable);
            List<Order> products = page.getContent();
            handleList(products);

        }


    }

    private void handleList(List<Order> products) {


        for (Order order : products) {

            String newAttaches = AttachFileUtils.getNewAttaches(order.attaches, attachfilepath, tempFilePath, AttachFileUtils.ORDER_ATTACH_PREFIX + order.osNo);
            if (!newAttaches.equals(order.attaches)) {
                order.attaches = newAttaches;
                orderRepository.save(order);

            }

        }


        productRepository.flush();

    }


    /**
     * 接收产品订单的递交数据
     * 如果该流程未配置审核人， 则自动通过审核。并使该订单进入下一个流程
     */
    @Transactional
    public RemoteData<Void> receiveOrderItemWorkFlow(User loginUser, long messageId) {


        WorkFlowMessage message = workFlowMessageRepository.findOne(messageId);
        if (message == null) {
            return wrapError("消息不存在：" + messageId);
        }
        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(message.toFlowStep);
        if (workFlow == null) {
            return wrapError("目标流程不存在" + message.toFlowStep);
        }


        OrderItemWorkFlow orderItemWorkFlow = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(message.orderItemId);
        if (orderItemWorkFlow == null) {
            return wrapError("异常，未找到关联的订单排厂信息");
        }

        OrderItem orderItem = orderItemRepository.findOne(message.orderItemId);
        if (orderItem == null) {
            return
                    wrapError("未找到该订单货款 :" + message.orderName + "   " + message.productName);
        }

        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowIdEqualsAndReceiveEquals(loginUser.id, workFlow.id, true);
        boolean canOperate = workFlowWorker != null;


        if (!canOperate) {
            return wrapError("未配置对该流程处理的权限");

        }


        OrderItemWorkFlowState state = orderItemWorkFlowStateRepository.findOne(message.orderItemWorkFlowStateId);


        if (state == null) {
            return wrapError("未找到对应的流程状态信息");

        }

        state.receverId = loginUser.id;
        state.receverName = loginUser.toString();
        //上流程 数量整理
        state.sendingQty -= message.transportQty;
        state.qty -= message.transportQty;
        state.sentQty += message.transportQty;


        orderItemWorkFlowStateRepository.save(state);


        String[] workFlowSteps = orderItemWorkFlow.workFlowSteps.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        String[] workFlowNames = orderItemWorkFlow.workFlowNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        String[] productTypes = StringUtils.split(orderItemWorkFlow.productTypes);


        OrderItemWorkFlowState newState = orderItemWorkFlowStateRepository.findFirstByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsAndFactoryIdEqualsAndProductTypeEqualsOrderByOrderNameDescCreateTimeDesc(0, state.orderItemId, state.nextWorkFlowStep, state.factoryId, state.productType);
        if (newState == null) {
            newState = GsonUtils.fromJson(GsonUtils.toJson(state), OrderItemWorkFlowState.class);
            newState.id = 0;
            newState.qty = message.transportQty;
            newState.sendingQty = 0;
            newState.unSendQty = newState.qty;
            newState.sentQty = 0;
            newState.workFlowStep = newState.nextWorkFlowStep;
            newState.workFlowName = newState.nextWorkFlowName;
        } else {

            //数量合并累加
            newState.qty += message.transportQty;
            newState.unSendQty += message.transportQty;
        }


        newState.createTime = Calendar.getInstance().getTimeInMillis();
        newState.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        int length = workFlowSteps.length;


        //判断是否是汇总节点。
        boolean needGroupUp = newState.workFlowStep == orderItemWorkFlow.groupStep && productTypes != null;

        //获取下个节点流程数据
        String[] nextWorkFlow = getNextWorkFlow(newState.workFlowStep, newState.productType, orderItemWorkFlow);

        int nextWorkFlowStep;
        String nextWorkFlowName;

        if (nextWorkFlow != null) {
            nextWorkFlowStep
                    = Integer.valueOf(nextWorkFlow[0]);
            nextWorkFlowName = nextWorkFlow[1];
        } else {

            //节点到此结束
            nextWorkFlowStep
                    = 0;
            nextWorkFlowName = "";

        }


        if (needGroupUp) {
            //附上下一节点流程数据
            newState.nextWorkFlowStep = 0;
            newState.nextWorkFlowName = "";

        } else {
            //附上下一节点流程数据
            newState.nextWorkFlowStep = nextWorkFlowStep;
            newState.nextWorkFlowName = nextWorkFlowName;

        }
        newState = orderItemWorkFlowStateRepository.save(newState);


        //检查数据合并

        //查找当前订单 当前节点的所有状态数据
        if (needGroupUp) {
            List<OrderItemWorkFlowState> relateStates = orderItemWorkFlowStateRepository.findByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsOrderByOrderNameDescCreateTimeDesc(0, newState.orderItemId, newState.workFlowStep);

            //需要合并
            if (!StringUtils.isEmpty(newState.factoryId)) {// && "0".equals(newState.workFlowType)

                //多少个产品类型 就会派发多少个厂家跟进度。
                if (relateStates.size() == productTypes.length) {
                    boolean isAllPrepared = true;
                    for (OrderItemWorkFlowState aState : relateStates) {
                        if (aState.qty != state.orderQty) {
                            isAllPrepared = false;
                            break;
                        }
                    }


                    //清除分厂家的进度信息（设置零）， 汇总数据（新数据 配置数量，清除厂家数据）。
                    if (isAllPrepared) {
                        newState = GsonUtils.fromJson(GsonUtils.toJson(newState), OrderItemWorkFlowState.class);
                        for (OrderItemWorkFlowState aState : relateStates) {
                            aState.qty = 0;
                            aState.sendingQty = 0;
                            aState.unSendQty = 0;
                        }
                        orderItemWorkFlowStateRepository.save(relateStates);

                        newState.id = 0;
                        newState.nextWorkFlowStep = nextWorkFlowStep;
                        newState.nextWorkFlowName = nextWorkFlowName;

                        newState.qty = newState.orderQty;
                        newState.unSendQty = newState.orderQty;
                        newState.sendingQty = 0;

                        newState.factoryId = "";
                        newState.factoryName = "";
                        newState.productType = "";
                        newState.productTypeName = "";
                        orderItemWorkFlowStateRepository.save(newState);
                        //删除记录， 合成新记录。

                    }


                }


            }


        }


        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        if (message.state == WorkFlowMessage.STATE_SEND) {

            //无审核人 系统自动审核通过
            message.state = WorkFlowMessage.STATE_PASS;
            message.checkTime = Calendar.getInstance().getTimeInMillis();
            message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        } else if (message.state == WorkFlowMessage.STATE_REWORK) {
            //返工 状态 自动通过。
            message.state = WorkFlowMessage.STATE_PASS;
            message.receiveTime = Calendar.getInstance().getTimeInMillis();
            message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        }

        workFlowMessageRepository.save(message);


        return wrapData();
    }


    /**
     * 返回下一节点的 节点名称
     *
     * @return
     */
    private String[] getNextWorkFlow(int currentWorkFlowStep, String currentProductType, OrderItemWorkFlow orderItemWorkFlow) {

        String[] nextWorkFlow = new String[2];


        String[] workFlowSteps = StringUtils.split(orderItemWorkFlow.workFlowSteps);
        String[] workFlowNames = StringUtils.split(orderItemWorkFlow.workFlowNames);

        int flowStepIndex = StringUtils.index(workFlowSteps, String.valueOf(currentWorkFlowStep));
        int flowStepCount = workFlowSteps.length;
        if (flowStepIndex >= flowStepCount - 1)
            return null;
        String[] configs = StringUtils.split(orderItemWorkFlow.configs);
        String[] productTypes = orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);

        int productTypeIndex = StringUtils.index(productTypes, currentProductType);
        if (productTypeIndex > -1) {


            nextWorkFlow = new String[]{workFlowSteps[flowStepIndex + 1], workFlowNames[flowStepIndex + 1]};

        } else {


            for (int i = flowStepIndex + 1; i < flowStepCount; i++) {

                String[] configArray = StringUtils.split(configs[i], StringUtils.STRING_SPLIT_COMMA);
                if ("1".equals(configArray[productTypeIndex])) {

                    nextWorkFlow = new String[]{workFlowSteps[i], workFlowNames[i]};
                    break;
                }

            }


        }


        return nextWorkFlow;


    }


    /**
     * 审核订单的传递数据 审核结束， 订单进入下一流程
     *
     * @param loginUser
     * @param messageId
     */
    @Transactional
    public RemoteData<Void> checkOrderItemWorkFlow(User loginUser, long messageId) {

        WorkFlowMessage message = workFlowMessageRepository.findOne(messageId);
        if (message == null) {
            return wrapError("消息不存在：" + messageId);
        }
        if (message.state != WorkFlowMessage.STATE_RECEIVE) {
            return wrapError("当前消息的状态不对，必须是已接收未审核的");
        }


        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(message.toFlowStep);
        if (workFlow == null) {
            return wrapError("目标流程不存在" + message.toFlowStep);
        }


        OrderItem orderItem = orderItemRepository.findOne(message.orderItemId);
        if (orderItem == null) {
            return
                    wrapError("未找到该订单货款 :" + message.orderName + "   " + message.productName);
        }

        OrderItemWorkFlow workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(message.orderItemId);
        if (workFlowOrderItem == null) {
            return
                    wrapError("未找到该货款流程数据 :" + message.orderName + "   " + message.productName);
        }
        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowIdEqualsAndReceiveEquals(loginUser.id, workFlow.id, true);
        boolean canOperate = workFlowWorker != null;


        message.state = WorkFlowMessage.STATE_PASS;
        message.checkTime = Calendar.getInstance().getTimeInMillis();
        message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        orderItemWorkFlowRepository.save(workFlowOrderItem);


        workFlowMessageRepository.save(message);


        return wrapData();

    }

    /**
     * 检查流程跟用户的关联
     *
     * @param userId
     * @param workFlowId
     * @return
     */
    private boolean checkWorkFlowAndUser(long userId, long workFlowId) {
        boolean canOperate = false;
        //验证流程
        List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findByUserIdEqualsAndWorkFlowIdEquals(userId, workFlowId);
        for (WorkFlowWorker workFlowWorker : workFlowWorkers) {
            if (workFlowWorker.receive) {

                canOperate = true;
                break;
            }
        }
        return canOperate;
    }


    /**
     * 获取指定用户可以操作的生产流程转移的订单流程items
     *
     * @return
     */
    public RemoteData<OrderItemWorkFlowState> getOrderItemForTransform(User loginUser) {


        /**
         * 找出负责人 ，并且不是最后一道流程,最后一道流程不能发起提交了。
         */
        List<WorkFlow> workFlows = workFlowRepository.findByUserIdEqualsAndFlowStepNot(loginUser.id, WorkFlow.FINAL_STEP);

        if (workFlows == null || workFlows.size() == 0) {
            return wrapError("你不是流程负责人，不能发送流程操作");
        }


        final int size = workFlows.size();
        int[] steps = new int[size];
        for (int i = 0; i < size; i++) {
            steps[i] = workFlows.get(i).flowStep;

        }

        List<OrderItemWorkFlowState> workFlowStates = orderItemWorkFlowStateRepository.findByWorkFlowStepInAndQtyIsGreaterThanOrderByOrderNameDesc(steps, 0);

        List<OrderItemWorkFlowState> validData = new ArrayList<>();

        for (OrderItemWorkFlowState state : workFlowStates) {
            if (state.unSendQty > 0 && !StringUtils.isEmpty(state.nextWorkFlowName)) {
                validData.add(state);
            }

        }

//        //已经发送 或者 已经接受未审核的  都不能再发。
//        List<WorkFlowOrderItem> orderItems = orderItemWorkFlowRepository.findByWorkFlowStepInAndWorkFlowStateEqualsOrderByOrderNameDesc(steps, 0);
//
//
//        List<ErpOrderItem> erpOrderItems = convertWorkFlowItemToErpOrderItem(orderItems);

        return wrapData(validData);


    }


    /**
     * 获取生产流程表
     *
     * @return
     */
    public RemoteData<WorkFlow> getWorkFlowList() {


        List<WorkFlow> workFlows = workFlowRepository.findAll();
        return wrapData(workFlows);
    }

    /**
     * 保存生产流程表
     *
     * @return
     */
    @Transactional
    public RemoteData<WorkFlow> saveWorkFlowList(List<WorkFlow> workFlowList) {


        for (WorkFlow workFlow : workFlowList) {
            if (workFlow.id > 0)
                workFlowRepository.save(workFlow);
        }

        return getWorkFlowList();

    }


    /**
     * 获取指定用户未处理的消息。
     *
     * @param loginUser
     * @return
     */
    public RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessage(User loginUser) {




        List<WorkFlowWorker> workFlowWorkers=workFlowWorkerRepository.findByUserIdEqualsAndReceiveEquals(loginUser.id,true);

        if(!ArrayUtils.isNotEmpty(workFlowWorkers)) return wrapData();

        int size = workFlowWorkers.size();
        int[] flowSteps = new int[size];
        for (int i = 0; i < size; i++) {
            WorkFlowWorker workFlow = workFlowWorkers.get(i);
            flowSteps[i] = workFlow.workFlowStep;
        }
        int[] state = new int[]{WorkFlowMessage.STATE_SEND, WorkFlowMessage.STATE_REWORK}; //WorkFlowMessage.STATE_RECEIVE,
        List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findByStateInAndToFlowStepIn(state, flowSteps);

        return wrapData(workFlowMessages);

    }

    /**
     * 向指定流程发起生产提交
     *
     * @param user
     * @param orderItemWorFlowStateId 订单项对应流程状态id
     * @param tranQty                 传递数量
     * @param memo                    备注
     */


    @Transactional
    public RemoteData<Void> sendWorkFlowMessage(User user, long orderItemWorFlowStateId, int tranQty, String memo) {


        OrderItemWorkFlowState workFlowOrderItemState = orderItemWorkFlowStateRepository.findOne(orderItemWorFlowStateId);
        if (workFlowOrderItemState == null)
            return wrapError("订单流程状态数据不存在");

        if (workFlowOrderItemState.nextWorkFlowStep <= 0)
            return wrapError("该订单下一流程数据不存在");

        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.workFlowStep);
        if (workFlow == null)
            return wrapError("数据异常，该订单当前流程不存在");
        WorkFlow newWorkFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.nextWorkFlowStep);
        if (newWorkFlow == null)
            return wrapError("数据异常，该订单下一流程数据不存在");

        if (tranQty > workFlowOrderItemState.unSendQty) {

            return wrapError("提交数量超过当前流程数量");
        }


        //验证人员

        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowIdEqualsAndSendEquals(user.id, workFlow.id, true);
        if (workFlowWorker == null) {
            return wrapError("无权限在当前节点:" + workFlow.name + " 发送流程");
        }


        OrderItem orderItem = orderItemRepository.findOne(workFlowOrderItemState.orderItemId);
        //设置当前为初始状态
        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(orderItem.prdNo, StringUtils.isEmpty(orderItem.pVersion) ? "" : orderItem.pVersion);
        if (product == null)
            return wrapError("该订单中有货款，没有产品资料，请补全。");


        //扣减数量
        workFlowOrderItemState.unSendQty -= tranQty;
        workFlowOrderItemState.sendingQty += tranQty;

        workFlowOrderItemState.senderId = user.id;
        workFlowOrderItemState.senderName = user.toString();
        orderItemWorkFlowStateRepository.save(workFlowOrderItemState);


        //构建信息发出消息
        WorkFlowMessage workFlowMessage = new WorkFlowMessage();


        workFlowMessage.orderItemWorkFlowStateId = workFlowOrderItemState.id;
        workFlowMessage.orderId = workFlowOrderItemState.orderId;
        workFlowMessage.orderName = workFlowOrderItemState.orderName;
        workFlowMessage.orderItemId = workFlowOrderItemState.orderItemId;
        workFlowMessage.orderItemWorkFlowId = workFlowOrderItemState.orderItemWorkFLowId;
        workFlowMessage.orderItemQty = orderItem.qty;
        workFlowMessage.transportQty = tranQty;
        workFlowMessage.memo = memo;
        workFlowMessage.name = WorkFlowMessage.NAME_SUBMIT;
        workFlowMessage.fromFlowStep = workFlowOrderItemState.workFlowStep;
        workFlowMessage.fromFlowName = workFlowOrderItemState.workFlowName;
        workFlowMessage.toFlowStep = workFlowOrderItemState.nextWorkFlowStep;
        workFlowMessage.toFlowName = workFlowOrderItemState.nextWorkFlowName;
        workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
        workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        workFlowMessage.state = WorkFlowMessage.STATE_SEND;
        workFlowMessage.unit = orderItem.ut;
        workFlowMessage.productId = product.id;
        workFlowMessage.productName = product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("-" + product.pVersion));
        workFlowMessage.url = product.url;

        workFlowMessage.factoryName = workFlowOrderItemState.factoryName;
        workFlowMessage.productTypeName = workFlowOrderItemState.productTypeName;
        workFlowMessageRepository.save(workFlowMessage);


        return wrapData();


    }


    /**
     * 获取指定用户发送的流程消息列表
     *
     * @param loginUser
     * @return
     */

    public RemoteData<WorkFlowMessage> getSendWorkFlowMessageList(User loginUser) {


        List<WorkFlowWorker> workFlowWorkers=workFlowWorkerRepository.findByUserIdEqualsAndSendEquals(loginUser.id,true);

        if(!ArrayUtils.isNotEmpty(workFlowWorkers)) return wrapData();


        final int size = workFlowWorkers.size();
        int[] steps = new int[size];
        for (int i = 0; i < size; i++) {
            steps[i] = workFlowWorkers.get(i).workFlowStep;

        }

        return wrapData(workFlowMessageRepository.findByFromFlowStepInOrderByCreateTimeDesc(steps));
    }


    public RemoteData<OrderItemWorkFlowState> getUnCompleteOrderItem(User user) {


        List<OrderItemWorkFlowState> orderItems = orderItemWorkFlowStateRepository.findByQtyIsGreaterThanOrderByOrderNameDescCreateTimeDesc(0);


        return wrapData(orderItems);


    }

    public RemoteData<OrderItemWorkFlowState> getWorkFlowOrderItem(User user, String key, int pageIndex, int pageSize) {

        String keyForSearch = "%" + key + "%";
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);

        Page<OrderItemWorkFlowState> pageValue = orderItemWorkFlowStateRepository.findByOrderNameLikeAndQtyIsGreaterThanOrProductFullNameLikeAndQtyIsGreaterThanOrderByOrderNameDescCreateTimeDesc(keyForSearch, 0, keyForSearch, 0, pageable);


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), pageValue.getContent());


        // return wrapData(orderItemWorkFlowStateRepository.findByOrderNameLikeOrProductFullNameLikeOrderNameDescCreateTimeDesc(key,key));


    }

    @Transactional
    public RemoteData<Void> rejectWorkFlowMessage(User user, long workFlowMsgId, int toWorkFlowStep, String reason) {

        WorkFlowMessage message = workFlowMessageRepository.findOne(workFlowMsgId);

        WorkFlow to = workFlowRepository.findFirstByFlowStepEquals(toWorkFlowStep);

        message.state = WorkFlowMessage.STATE_REJECT;
        message.memo = reason;
        message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        message.checkTime = Calendar.getInstance().getTimeInMillis();


        workFlowMessageRepository.save(message);


        //构建返工发出消息
        WorkFlowMessage workFlowMessage = new WorkFlowMessage();
        workFlowMessage.orderId = message.orderId;
        workFlowMessage.orderName = message.orderName;
        workFlowMessage.orderItemId = message.orderItemId;
        workFlowMessage.orderItemWorkFlowId = message.orderItemWorkFlowId;
        workFlowMessage.orderItemQty = message.orderItemQty;
        workFlowMessage.transportQty = message.transportQty;
        workFlowMessage.memo = reason;
        workFlowMessage.name = WorkFlowMessage.NAME_REWORK;

        workFlowMessage.fromFlowStep = message.fromFlowStep;
        workFlowMessage.fromFlowName = message.fromFlowName;


        workFlowMessage.toFlowStep = to.flowStep;
        workFlowMessage.toFlowName = to.name;

        workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
        workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        workFlowMessage.state = WorkFlowMessage.STATE_REWORK;
        workFlowMessage.unit = message.unit;
        workFlowMessage.productId = message.productId;
        workFlowMessage.productName = message.productName;
        workFlowMessage.url = message.url;

        workFlowMessageRepository.save(workFlowMessage);

        return wrapData();


    }

    /**
     * 获取制定订单的流程生产状态
     *
     * @param orderItemId
     * @return
     */
    public RemoteData<WorkFlowReport> getOrderItemWorkState(long orderItemId) {


        OrderItemWorkFlow workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItemId);

        if (workFlowOrderItem == null)

        {
            return wrapError("未找到该订单,该订单未排厂");

        }


        String[] workFlowSteps = workFlowOrderItem.workFlowSteps.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        String[] workFlowNames = workFlowOrderItem.workFlowNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        String[] workFlowConfigs = StringUtils.split(workFlowOrderItem.configs);


        int size = workFlowSteps.length;

        String[] productTypes = workFlowOrderItem.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);


        List<WorkFlowReport> workFlowReports = new ArrayList<>();


        for (int i = 0; i < size; i++) {

            WorkFlowReport report = new WorkFlowReport();
            int workFlowStep = Integer.valueOf(workFlowSteps[i]);


            String[] workFlowTypes = StringUtils.split(workFlowConfigs[i], StringUtils.STRING_SPLIT_COMMA);
            int divideSize = 0;

            //判断平均百分比数量
            if (workFlowOrderItem.produceType == ProduceType.SELF_MADE) {
                for (int index = 0; index < workFlowTypes.length; index++) {
                    divideSize += Integer.valueOf(workFlowTypes[index]);
                }
            } else {
                divideSize = 1;
            }
            divideSize = Math.max(divideSize, 1);

            report.orderItemId = orderItemId;
            report.orderName = workFlowOrderItem.orderName;
            report.workFlowStep = workFlowStep;
            report.workFlowName = workFlowNames[i];


            List<OrderItemWorkFlowState> workFlowStates = orderItemWorkFlowStateRepository.findByOrderItemIdEqualsAndWorkFlowStepEquals(orderItemId, workFlowStep);

            //无状态数据 表示流程未到。
            if (workFlowStates.size() == 0)
                report.percentage = 0;
            else {


                for (OrderItemWorkFlowState state : workFlowStates) {

                    if (state.nextWorkFlowStep > 0) {
                        //当前数量为0 表示已经全部发出
                        report.percentage += (float) (state.sentQty) / state.orderQty;
                    }

                }

                report.percentage /= divideSize;
            }
            workFlowReports.add(report);


        }


        return wrapData(workFlowReports);
    }

    public RemoteData<OrderItem> searchOrderItem(String key, int pageIndex, int pageSize) {
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        String keyForSearch = "%" + key.trim() + "%";
        Page<OrderItem> pageValue = orderItemRepository.findByOsNoLikeOrPrdNoLikeOrderByOsNoDesc(keyForSearch, keyForSearch, pageable);
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), pageValue.getContent());
    }

    /**
     * @param orderItemWorkFlowId
     * @return
     */
    @Transactional
    public RemoteData<Void> cancelOrderWorkFlow(User loginUser, long orderItemWorkFlowId) {


        OrderItemWorkFlow orderItemWorkFlow = orderItemWorkFlowRepository.findOne(orderItemWorkFlowId);
        if (orderItemWorkFlow == null) {
            return wrapError("该订单未排厂");
        }


        boolean isSelfMade = orderItemWorkFlow.produceType == ProduceType.SELF_MADE;

        WorkFlowArranger workFlowArranger = null;
        if (isSelfMade) {

            workFlowArranger = workFlowArrangerRepository.findFirstByUserIdEqualsAndSelfMadeEquals(loginUser.id, true);
        } else {
            workFlowArranger = workFlowArrangerRepository.findFirstByUserIdEqualsAndPurchaseEquals(loginUser.id, true);
        }

        if (workFlowArranger == null) {
            return wrapError("无权限撤销排厂操作");
        }


        int rowCount = orderItemWorkFlowStateRepository.deleteByOrderItemIdEquals(orderItemWorkFlow.orderItemId);

        logger.info("删除订单" + orderItemWorkFlow.orderName + "," + orderItemWorkFlow.productFullName + "流程数据，共" + rowCount + "条");

        rowCount = workFlowMessageRepository.deleteByOrderItemIdEquals(orderItemWorkFlow.orderItemId);


        logger.info("删除订单" + orderItemWorkFlow.orderName + "," + orderItemWorkFlow.productFullName + "流程消息数据，共" + rowCount + "条");


        orderItemWorkFlow.workFlowDiscribe = "";

        orderItemWorkFlowRepository.save(orderItemWorkFlow);

        return wrapData();


    }


}
