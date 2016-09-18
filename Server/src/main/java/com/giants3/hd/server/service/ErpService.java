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
                item.packAttaches = product.packAttaches;
                item.packageInfo = product.packInfo;
            }


            // 附加数据
            OrderItem orderItem = orderItemRepository.findFirstByOsNoEqualsAndItmEquals(item.os_no, item.itm);
            if (orderItem != null) {

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

        List<WorkFlowOrderItem> workFlows = orderItemWorkFlowRepository.findByOrderNameEquals(os_no);
        if (workFlows == null || workFlows.size() == 0) {


            for (OrderItem orderItem : orderItems) {
                WorkFlowOrderItem workFlowOrderItem = new WorkFlowOrderItem();
                workFlowOrderItem.orderId = order.id;
                workFlowOrderItem.orderName = order.osNo;
                workFlowOrderItem.orderItemId = orderItem.id;
                workFlowOrderItem.productName = orderItem.prd_no;


                //设置当前为初始状态
                Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(orderItem.prd_no, StringUtils.isEmpty(orderItem.pVersion) ? "" : orderItem.pVersion);
                if (product != null) {
                    workFlowOrderItem.workFlowIndex = WorkFlow.STEP_INDEX_1;
                    workFlowOrderItem.workFlowName = WorkFlow.STEP_1;
                    workFlowOrderItem.workFlowIndexs = product.workFlowIndexs;
                    workFlowOrderItem.createTime = Calendar.getInstance().getTimeInMillis();
                    workFlowOrderItem.workFlowIndexs = product.workFlowIndexs;
                    workFlowOrderItem.workFlowNames = product.workFlowNames;
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


                workFlowMessage.fromFlowIndex = workFlowOrderItem.workFlowIndex;
                workFlowMessage.fromFlowName = workFlowOrderItem.workFlowName;


                workFlowMessage.toFlowIndex = Integer.valueOf(StringUtils.split(workFlowOrderItem.workFlowIndexs, StringUtils.PRODUCT_NAME_COMMA)[1]);
                workFlowMessage.toFlowName = StringUtils.split(workFlowOrderItem.workFlowNames, StringUtils.PRODUCT_NAME_COMMA)[1];

                workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
                workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
                workFlowMessage.state = 0;
                workFlowMessage.unit = product.pUnitName;
                workFlowMessage.productId = product.id;


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
    public void receiveOrderItemWorkFlow(User loginUser, long messageId) {


    }


    /**
     * 审核订单的传递数据 审核结束， 订单进入下一流程
     *
     * @param loginUser
     * @param messageId
     */
    public void checkOrderItemWorkFlow(User loginUser, long messageId) {

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
        int[] flowIndexs = new int[size];
        for (int i = 0; i < size; i++) {
            WorkFlow workFlow = workFlows.get(i);
            flowIndexs[i] = workFlow.flowIndex;
        }
        List<WorkFlowMessage> workFlowMessages = workFlowMessageRepository.findByStateEqualsAndToFlowIndexIn(0, flowIndexs);

        return wrapData(workFlowMessages);

    }

    /**
     * 向指定流程发起生产提交
     *
     * @param user
     * @param orderItemId
     * @param toWorkFlowId
     */
    public void sendOrderItemWorkFlow(User user, long orderItemId, long toWorkFlowId) {

    }
}
