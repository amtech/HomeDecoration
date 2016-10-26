package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.entity_erp.ErpOrder;
import com.giants3.hd.server.entity_erp.ErpOrderItem;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.noEntity.ErpOrderDetail;
import com.giants3.hd.server.noEntity.OrderReportItem;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
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
    WorkFlowRepository workFlowRepository;


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


        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
    public RemoteData<ErpOrderDetail> getOrderDetail(String os_no) {


        ErpOrderDetail orderDetail = new ErpOrderDetail();

        List<ErpOrderItem> orderItems = repository.findItemsByOrderNo(os_no);
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
        orderDetail.items = orderItems;
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


                //绑定订单跟踪数据
                WorkFlowOrderItem workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItem.id);
                if (workFlowOrderItem != null) {
                    item.currentWorkFlow = workFlowOrderItem.workFlowName;
                }

            }


        }

        return wrapData(orderDetail);

    }


    @Transactional
    public RemoteData<ErpOrderDetail> saveOrderDetail(ErpOrderDetail orderDetail) {

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
        return getOrderDetail(erpOrder.os_no);
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
        orderItem.prd_no = erpOrderItem.prd_no;
        orderItem.pVersion = erpOrderItem.pVersion;
        orderItem.url = erpOrderItem.url;
        orderItem.bat_no = erpOrderItem.bat_no;


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
        orderReportItem.cus_prd_no = orderItem.bat_no;
        orderReportItem.qty = orderItem.qty;
        orderReportItem.prd_no = orderItem.prd_no;
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
     * 启动订单跟踪
     * 该订单下的所有产品 走产品流程
     */
    @Transactional
    public RemoteData<Void> startTrack(String os_no) {


        Order order = orderRepository.findFirstByOsNoEquals(os_no);
        List<OrderItem> orderItems = orderItemRepository.findByOsNoEqualsOrderByItm(os_no);

        if (orderItems == null || orderItems.size() == 0)
            return wrapError("该订单未保存，请选保存。");
        ;
        List<WorkFlowOrderItem> workFlows = orderItemWorkFlowRepository.findByOrderNameEquals(os_no);
        if (workFlows == null || workFlows.size() == 0) {


            for (OrderItem orderItem : orderItems) {
                WorkFlowOrderItem workFlowOrderItem = new WorkFlowOrderItem();
                workFlowOrderItem.orderId = order.id;
                workFlowOrderItem.orderName = order.osNo;
                workFlowOrderItem.orderItemId = orderItem.id;
                workFlowOrderItem.productName = orderItem.prd_no + (StringUtils.isEmpty(orderItem.pVersion) ? "" : ("-" + orderItem.pVersion));
                workFlowOrderItem.tranQty = orderItem.qty;

                //设置当前为初始状态
                Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(orderItem.prd_no, StringUtils.isEmpty(orderItem.pVersion) ? "" : orderItem.pVersion);

                if (product == null)
                    return wrapError("该订单中有货款，没有产品资料，请补全。");
                ;
                if (product != null) {
                    workFlowOrderItem.workFlowStep = WorkFlow.FIRST_STEP;
                    workFlowOrderItem.workFlowName = WorkFlow.STEP_1;
                    workFlowOrderItem.workFlowSteps = product.workFlowSteps;
                    workFlowOrderItem.createTime = Calendar.getInstance().getTimeInMillis();
                    workFlowOrderItem.workFlowSteps = product.workFlowSteps;
                    workFlowOrderItem.workFlowNames = product.workFlowNames;
                    //默认第一个流程发起提交
                    workFlowOrderItem.workFlowState = 1;
                    workFlowOrderItem.createTime = Calendar.getInstance().getTimeInMillis();
                    workFlowOrderItem.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


                }


                //构建信息发出消息
                //

                WorkFlowMessage workFlowMessage = new WorkFlowMessage();
                workFlowMessage.orderId = order.id;
                workFlowMessage.orderName = order.osNo;
                workFlowMessage.orderItemId = orderItem.id;
                workFlowMessage.orderItemQty = orderItem.qty;
                workFlowMessage.transportQty = orderItem.qty;

                workFlowMessage.name = WorkFlowMessage.NAME_SUBMIT;
                workFlowMessage.fromFlowStep = workFlowOrderItem.workFlowStep;
                workFlowMessage.fromFlowName = workFlowOrderItem.workFlowName;


                workFlowMessage.toFlowStep = Integer.valueOf(StringUtils.split(workFlowOrderItem.workFlowSteps, StringUtils.PRODUCT_NAME_COMMA)[1]);
                workFlowMessage.toFlowName = StringUtils.split(workFlowOrderItem.workFlowNames, StringUtils.PRODUCT_NAME_COMMA)[1];

                workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
                workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
                workFlowMessage.state = WorkFlowMessage.STATE_SEND;
                workFlowMessage.unit = product.pUnitName;
                workFlowMessage.productId = product.id;
                workFlowMessage.productName = product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("-" + product.pVersion));
                workFlowMessage.url = product.url;

                workFlowMessageRepository.save(workFlowMessage);


                workFlows.add(workFlowOrderItem);


            }
            orderItemWorkFlowRepository.save(workFlows);


        }


        return wrapData();


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

        if (workFlow.userId != loginUser.id) {
            return wrapError("你不是目标流程的负责人：" + workFlow.userCName);
        }


        OrderItem orderItem = orderItemRepository.findOne(message.orderItemId);
        if (orderItem == null) {
            return
                    wrapError("未找到该订单货款 :" + message.orderName + "   " + message.productName);
        }

        WorkFlowOrderItem workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(message.orderItemId);
        if (workFlowOrderItem == null) {
            return
                    wrapError("未找到该货款流程数据 :" + message.orderName + "   " + message.productName);
        }


        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        if (message.state == WorkFlowMessage.STATE_SEND) {
        //有审核人
            if (workFlow.checkerId > 0) {
                message.state = WorkFlowMessage.STATE_RECEIVE;
                workFlowOrderItem.workFlowState = 1;
            } else {
                //无审核人 系统自动审核通过
                message.state = WorkFlowMessage.STATE_PASS;
                message.checkTime = Calendar.getInstance().getTimeInMillis();
                message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            }

            //不管有无审核人 ，只要接收就进入下一流程
                //进入目标流程
                workFlowOrderItem.workFlowName = workFlow.name;
                workFlowOrderItem.workFlowStep = workFlow.flowStep;
                workFlowOrderItem.workFlowState = 0;
                workFlowOrderItem.tranQty = message.transportQty;




        } else if (message.state == WorkFlowMessage.STATE_REWORK) {
            //返工 状态 自动通过。
            message.state = WorkFlowMessage.STATE_PASS;
            message.receiveTime = Calendar.getInstance().getTimeInMillis();
            message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            //进入目标流程
            workFlowOrderItem.workFlowName = workFlow.name;
            workFlowOrderItem.workFlowStep = workFlow.flowStep;
            workFlowOrderItem.workFlowState = 0;
            workFlowOrderItem.tranQty = message.transportQty;

        }
        orderItemWorkFlowRepository.save(workFlowOrderItem);

        workFlowMessageRepository.save(message);


        return wrapData();
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

        if (workFlow.checkerId != loginUser.id) {
            return wrapError("你不是目标流程的传递的审核人：" + workFlow.userCName);
        }


        OrderItem orderItem = orderItemRepository.findOne(message.orderItemId);
        if (orderItem == null) {
            return
                    wrapError("未找到该订单货款 :" + message.orderName + "   " + message.productName);
        }

        WorkFlowOrderItem workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(message.orderItemId);
        if (workFlowOrderItem == null) {
            return
                    wrapError("未找到该货款流程数据 :" + message.orderName + "   " + message.productName);
        }



        message.state = WorkFlowMessage.STATE_PASS;
        message.checkTime = Calendar.getInstance().getTimeInMillis();
        message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        orderItemWorkFlowRepository.save(workFlowOrderItem);


        workFlowMessageRepository.save(message);


        return wrapData();

    }


    /**
     * 获取指定用户可以操作的生产流程转移的订单items
     *
     * @return
     */
    public RemoteData<ErpOrderItem> getOrderItemForTransform(User loginUser) {


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
        //已经发送 或者 已经接受未审核的  都不能再发。
        List<WorkFlowOrderItem> orderItems = orderItemWorkFlowRepository.findByWorkFlowStepInAndWorkFlowStateEqualsOrderByOrderNameDesc(steps, 0);


        List<ErpOrderItem> erpOrderItems = convertWorkFlowItemToErpOrderItem(orderItems);

        return wrapData(erpOrderItems);


    }

    private List<ErpOrderItem> convertWorkFlowItemToErpOrderItem(List<WorkFlowOrderItem> orderItems) {
        List<ErpOrderItem> erpOrderItems = new ArrayList<>();
        for (WorkFlowOrderItem orderItem : orderItems) {

            ErpOrderItem erpOrderItem = new ErpOrderItem();
            erpOrderItem.id = orderItem.orderItemId;
            erpOrderItem.os_no = orderItem.orderName;
            erpOrderItem.prd_name = orderItem.productName;

            erpOrderItem.currentWorkFlow = orderItem.workFlowName;
            erpOrderItem.currentWorkStep = orderItem.workFlowStep;
            erpOrderItem.currentWorkState = orderItem.workFlowState;
            erpOrderItem.tranQty = orderItem.tranQty;

            OrderItem oItem = orderItemRepository.findOne(orderItem.orderItemId);
            if (oItem != null) {

                erpOrderItem.qty = oItem.qty;
                erpOrderItem.prd_no = oItem.prd_no;
                erpOrderItem.pVersion = oItem.pVersion;
                erpOrderItem.ut = oItem.ut;
                erpOrderItem.bat_no = oItem.bat_no;
                erpOrderItem.pVersion = oItem.pVersion;
                erpOrderItem.prd_no = oItem.prd_no;
                erpOrderItem.url = oItem.url;
                erpOrderItem.thumbnail = oItem.thumbnail;
                erpOrderItem.sendDate = oItem.sendDate;
                erpOrderItem.verifyDate = oItem.verifyDate;

            }
            erpOrderItems.add(erpOrderItem);
        }

        return erpOrderItems;
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


        List<WorkFlow> workFlows = workFlowRepository.findByCheckerIdEqualsOrUserIdEquals(loginUser.id, loginUser.id);
        if (workFlows == null || workFlows.size() == 0) return wrapData();

        int size = workFlows.size();
        int[] flowSteps = new int[size];
        for (int i = 0; i < size; i++) {
            WorkFlow workFlow = workFlows.get(i);
            flowSteps[i] = workFlow.flowStep;
        }
        int[] state = new int[]{WorkFlowMessage.STATE_SEND, WorkFlowMessage.STATE_RECEIVE, WorkFlowMessage.STATE_REWORK};
        List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findByStateInAndToFlowStepIn(state, flowSteps);

        return wrapData(workFlowMessages);

    }

    /**
     * 向指定流程发起生产提交
     *
     * @param user
     * @param orderItemId
     * @param flowStep
     * @param tranQty
     */


    @Transactional
    public RemoteData<Void> sendWorkFlowMessage(User user, long orderItemId, int flowStep, int tranQty, String memo) {


        WorkFlowOrderItem workFlowOrderItem = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItemId);
        if (workFlowOrderItem == null) return wrapError("订单数据不存在");


        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(flowStep);
        if (workFlow == null) {
            return wrapError("目标流程：" + flowStep + " 不存在");
        }


        workFlowOrderItem.workFlowState = 1;
        orderItemWorkFlowRepository.save(workFlowOrderItem);

        OrderItem orderItem = orderItemRepository.findOne(workFlowOrderItem.orderItemId);
        //设置当前为初始状态
        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(orderItem.prd_no, StringUtils.isEmpty(orderItem.pVersion) ? "" : orderItem.pVersion);
        if (product == null)
            return wrapError("该订单中有货款，没有产品资料，请补全。");
        //构建信息发出消息
        WorkFlowMessage workFlowMessage = new WorkFlowMessage();
        workFlowMessage.orderId = workFlowOrderItem.id;
        workFlowMessage.orderName = workFlowOrderItem.orderName;
        workFlowMessage.orderItemId = workFlowOrderItem.orderItemId;
        workFlowMessage.orderItemQty = orderItem.qty;
        workFlowMessage.transportQty = tranQty;
        workFlowMessage.memo = memo;
        workFlowMessage.name = WorkFlowMessage.NAME_SUBMIT;
        workFlowMessage.fromFlowStep = workFlowOrderItem.workFlowStep;
        workFlowMessage.fromFlowName = workFlowOrderItem.workFlowName;
        workFlowMessage.toFlowStep = workFlow.flowStep;
        workFlowMessage.toFlowName = workFlow.name;
        workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
        workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        workFlowMessage.state = WorkFlowMessage.STATE_SEND;
        workFlowMessage.unit = orderItem.ut;
        workFlowMessage.productId = product.id;
        workFlowMessage.productName = product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("-" + product.pVersion));
        workFlowMessage.url = product.url;
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

        /**
         * 找出负责人 ，并且不是最后一道流程,最后一道流程不能发起提交了。
         */
        List<WorkFlow> workFlows = workFlowRepository.findByUserIdEqualsAndFlowStepNot(loginUser.id, WorkFlow.FINAL_STEP);

        if (workFlows.size() == 0) return wrapData();


        final int size = workFlows.size();
        int[] steps = new int[size];
        for (int i = 0; i < size; i++) {
            steps[i] = workFlows.get(i).flowStep;

        }

        return wrapData(workFlowMessageRepository.findByFromFlowStepInOrderByCreateTimeDesc(steps));
    }

    public RemoteData<ErpOrderItem> getUnCompleteOrderItem(User user) {


        List<WorkFlowOrderItem> orderItems = orderItemWorkFlowRepository.findByWorkFlowStepNotOrderByCreateTimeDesc(WorkFlow.FINAL_STEP);

        List<ErpOrderItem> erpOrderItems = convertWorkFlowItemToErpOrderItem(orderItems);

        return wrapData(erpOrderItems);


    }

    public RemoteData<ErpOrderItem> getWorkFlowOrderItem(User user, String key, int pageIndex, int pageSize) {

        String keyForSearch = "%" + key + "%";
        Pageable pageable = constructPageSpecification(pageIndex, pageSize);

        Page<WorkFlowOrderItem> pageValue = orderItemWorkFlowRepository.findByOrderNameLikeOrProductNameLikeOrderByOrderNameDesc(keyForSearch, keyForSearch, pageable);

        List<WorkFlowOrderItem> orderItems = pageValue.getContent();

        List<ErpOrderItem> erpOrderItems = convertWorkFlowItemToErpOrderItem(orderItems);

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), erpOrderItems);


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

//        workFlowRepository.
        return wrapData();


    }
}
