package com.giants3.hd.server.service;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.*;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 生产流程管理业务处理类
 * Created by david on 2016/2/15.
 */
@Service
public class WorkFlowService extends AbstractService implements InitializingBean, DisposableBean {

    private static final Logger logger = Logger.getLogger(WorkFlowService.class);

    @Autowired
    ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkFlowSubTypeRepository workFlowSubTypeRepository;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    OrderItemRepository orderIemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WorkFlowRepository workFlowRepository;


    @Autowired
    OrderItemWorkFlowRepository orderItemWorkFlowRepository;
    @Autowired
    OrderItemWorkFlowStateRepository orderItemWorkFlowStateRepository;


    @Autowired
    WorkFlowProductRepository workFlowProductRepository;
    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;
    @Autowired
    WorkFlowArrangerRepository workFlowArrangerRepository;
    @Autowired
    WorkFlowMessageRepository workFlowMessageRepository;


    @Autowired

    ErpService erpService;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    /**
     * 获取所有流程数据
     *
     * @return
     */
    public List<WorkFlow> getAllWorkFlow() {
        return workFlowRepository.findAll();
    }


    /**
     * 获取配置的二级流程类型数据
     *
     * @return
     */
    public RemoteData<WorkFlowSubType> getWorkFlowSubTypes() {

        return wrapData(workFlowSubTypeRepository.findAll());
    }

    /**
     * 初始数据配置
     */
    public void initData() {


        logger.info("开始流程相关数据初始化");


        if (workFlowSubTypeRepository.count() == 0) {

            int size = WorkFlowSubType.TYPES.length;
            for (int i = 0; i < size; i++) {

                WorkFlowSubType workFlowSubType = new WorkFlowSubType();
                workFlowSubType.typeId = WorkFlowSubType.TYPES[i];
                workFlowSubType.typeName = WorkFlowSubType.TYPENAMES[i];
                workFlowSubTypeRepository.save(workFlowSubType);
            }
        }


        // 生产流程数据初始化

        List<WorkFlow> workFlows = workFlowRepository.findAll();
        if (workFlows.size() == 0) {
            workFlowRepository.save(WorkFlow.initWorkFlowData());
            productService.setDefaultWorkFlowIds();

        }


        logger.info("流程相关数据初始化结束");

    }


    /**
     * 根据产品id查询 流程配置信息
     *
     * @param productId
     * @return
     */
    public RemoteData<WorkFlowProduct> findWorkFlowByProductId(long productId) {

        List<WorkFlowProduct> workFlowProducts = workFlowProductRepository.findByProductIdEquals(productId);
        WorkFlowProduct workFlowProduct;
        if (workFlowProducts != null && workFlowProducts.size() > 0) {
            workFlowProduct = workFlowProducts.get(0);
        } else {
            workFlowProduct = new WorkFlowProduct();
            workFlowProduct.productId = productId;
            Product product = productService.findProductById(productId);
            if (product != null) {
                workFlowProduct.productName = product.name;
                workFlowProduct.productPVersion = product.pVersion;
            }
        }

        return wrapData(workFlowProduct);

    }

    @Transactional
    public RemoteData<WorkFlowProduct> saveWorkFlowProduct(WorkFlowProduct workFlowProduct) {

        if (workFlowProduct.productId <= 0) return wrapError("产品不存在");

        return wrapData(workFlowProductRepository.save(workFlowProduct));

    }

    /**
     * 保存订单流程配置
     *
     * @param orderItemWorkFlow
     */
    @Transactional(rollbackFor = {HdException.class})
    public void saveOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow) {






    }


    /**
     * 启动订单生产流程
     *
     * @param orderItemWorkFlow
     */
    @Transactional(rollbackFor = {HdException.class})
    public RemoteData<OrderItemWorkFlow> startOrderItemWorkFlow(User loginUser, OrderItemWorkFlow orderItemWorkFlow) {


        //人员验证
        WorkFlowArranger workFlowArranger = null;
        if (orderItemWorkFlow.produceType == ProduceType.SELF_MADE) {
            workFlowArranger = workFlowArrangerRepository.findFirstByUserIdEqualsAndSelfMadeEquals(loginUser.id, true);
        }
        if (orderItemWorkFlow.produceType == ProduceType.PURCHASE) {
            workFlowArranger = workFlowArrangerRepository.findFirstByUserIdEqualsAndPurchaseEquals(loginUser.id, true);
        }

        if (workFlowArranger == null) {
            return wrapError("没有排厂权限");


        }

         if(!StringUtils.isEmpty(orderItemWorkFlow.workFlowDiscribe))
         {
             return wrapError("订单已经排厂");
             //
         }


        //初始化， 发送第一步


        String[] workFlowSteps = orderItemWorkFlow.workFlowSteps.split(ConstantData.STRING_DIVIDER_SEMICOLON);


        String[] workFlowNames = orderItemWorkFlow.workFlowNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);

        OrderItem orderItem = orderIemRepository.findOne(orderItemWorkFlow.orderItemId);

        Order order = orderRepository.findFirstByOsNoEquals(orderItem.osNo);
        if (order != null) {
            orderItemWorkFlow.orderId = order.id;
            orderItemWorkFlow.orderName = order.osNo;


        } else {
            logger.error("can not find order  by osName:" + orderItem.osNo);
        }

        orderItemWorkFlow.productFullName = orderItem.prdNo;

        boolean isSelfMade = orderItemWorkFlow.produceType == ProduceType.SELF_MADE;
        StringBuilder describe = new StringBuilder();


        String[] factoryIds = StringUtils.split(isSelfMade ? orderItemWorkFlow.conceptusFactoryIds : orderItemWorkFlow.produceFactoryId);
        String[] factoryNames = StringUtils.split(isSelfMade ? orderItemWorkFlow.conceptusFactoryNames : orderItemWorkFlow.produceFactoryName);


        String[] productTypes = !isSelfMade ? null : orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        String[] productTypeNames = !isSelfMade ? null : orderItemWorkFlow.productTypeNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);


        int size = isSelfMade ? productTypes.length : 1;

        //二级排厂  排给厂家


        //不同产品类型， 对应不同的orderitemstate  就有多条流程记录  外厂就只有一条
        for (int i = 0; i < size; i++) {

            OrderItemWorkFlowState orderItemWorkFlowState = new OrderItemWorkFlowState();
            orderItemWorkFlowState.orderId = orderItemWorkFlow.orderId;
            orderItemWorkFlowState.orderItemId = orderItemWorkFlow.orderItemId;
            orderItemWorkFlowState.orderItemWorkFLowId = orderItemWorkFlow.id;
            orderItemWorkFlowState.orderName = orderItemWorkFlow.orderName;
            orderItemWorkFlowState.productFullName = StringUtils.isEmpty(orderItem.pVersion) ? orderItem.prdNo : (orderItem.prdNo + "-" + orderItem.pVersion);
            orderItemWorkFlowState.photoThumb = orderItem.thumbnail;
            orderItemWorkFlowState.pictureUrl = orderItem.url;
            orderItemWorkFlowState.workFlowStep = Integer.valueOf(workFlowSteps[0]);
            orderItemWorkFlowState.workFlowName = workFlowNames[0];
            orderItemWorkFlowState.nextWorkFlowStep = Integer.valueOf(workFlowSteps[1]);
            orderItemWorkFlowState.nextWorkFlowName = workFlowNames[1];
            orderItemWorkFlowState.factoryId = factoryIds[i];
            orderItemWorkFlowState.factoryName = factoryNames[i];


            //自制有类型数据
            if (isSelfMade) {
                orderItemWorkFlowState.productType = productTypes[i];
                orderItemWorkFlowState.productTypeName = productTypeNames[i];

            }


            orderItemWorkFlowState.qty = orderItem.qty;
            orderItemWorkFlowState.unSendQty = orderItem.qty;
            orderItemWorkFlowState.orderQty = orderItem.qty;
            orderItemWorkFlowState.createTime = Calendar.getInstance().getTimeInMillis();
            orderItemWorkFlowState.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
            orderItemWorkFlowStateRepository.save(orderItemWorkFlowState);
            describe.append(orderItemWorkFlowState.getMessage());
            describe.append("\n\r");


        }


        orderItemWorkFlow.workFlowDiscribe = describe.toString();
        orderItemWorkFlow = orderItemWorkFlowRepository.save(orderItemWorkFlow);



        if (orderItem != null) {

            orderItem.orderWorkFlowId = orderItemWorkFlow.id;
            orderItem.workFlowDescribe = describe.toString();
            orderIemRepository.save(orderItem);
        }






        return findWorkFlowByOrderItemId(orderItemWorkFlow.orderItemId);

    }


    /**
     * 排厂自动发送流程信息
     */
    private void sendMessageFromStart()
    {}

    public RemoteData<OrderItemWorkFlow> findWorkFlowByOrderItemId(long orderItemId) {

        OrderItemWorkFlow orderItemWorkFlow = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItemId);
        return wrapData(orderItemWorkFlow);
    }

    public RemoteData<OrderItemWorkFlowState> findOrderItemWorkFlowState(long orderItemId) {


        List<OrderItemWorkFlowState> list = orderItemWorkFlowStateRepository.findByQtyIsGreaterThanAndOrderItemIdEqualsOrderByOrderNameDescCreateTimeDesc(0, orderItemId);
        return wrapData(list);

    }

    public RemoteData<OrderItemWorkFlowState> searchOrderItemWorkFlowState(User user, String key, int pageIndex, int pageSize) {


        return null;


    }

    /**
     * 保存产品排厂类型
     *
     * @param workFlowSubTypes
     * @return
     */
    @Transactional
    public RemoteData<WorkFlowSubType> saveSubTypes(List<WorkFlowSubType> workFlowSubTypes) {


        for (WorkFlowSubType workFlowSubType : workFlowSubTypes) {

            if (workFlowSubType.id <= 0) {

                if (workFlowSubType.typeId <= 0 || workFlowSubTypeRepository.findFirstByTypeIdEquals(workFlowSubType.typeId) != null) {

                    return wrapError("类型Id,不能为空，且不能相同");
                }
            }

        }

        return wrapData(workFlowSubTypeRepository.save(workFlowSubTypes));


    }

    /**
     * 获取关联的流程信息
     *
     * @param orderItemId
     * @param workFlowStep
     * @return
     */
    public RemoteData<OrderItemWorkFlowState> getOrderItemWorkFlowState(long orderItemId, int workFlowStep) {

        List<OrderItemWorkFlowState> states = orderItemWorkFlowStateRepository.findByOrderItemIdEqualsAndWorkFlowStepEqualsAndUnSendQtyGreaterThan(orderItemId, workFlowStep, 0);

        List<OrderItemWorkFlowState> result = new ArrayList<>();
        for (OrderItemWorkFlowState state : states) {
            if (state.nextWorkFlowStep > 0)
                result.add(state);

        }

        return wrapData(result);

    }

    public RemoteData<OrderItemWorkFlow> getOrderItemWorkFlow(long orderItemId) {


        OrderItemWorkFlow orderItemWorkFlow = orderItemWorkFlowRepository.findFirstByOrderItemIdEquals(orderItemId);
        if (orderItemWorkFlow == null) return wrapData();
        return wrapData(orderItemWorkFlow);
    }

    public RemoteData<WorkFlowWorker> findWorkers() {


        List<WorkFlowWorker> workFlowWorkers = workFlowWorkerRepository.findAll();
        return wrapData(workFlowWorkers);


    }

    @Transactional
    public RemoteData<WorkFlowWorker> saveWorker(WorkFlowWorker workFlowWorker) {


        if (workFlowWorker.userId <= 0) return wrapError("未选择用户");
        if (workFlowWorker.workFlowId <= 0) return wrapError("未选择流程");

        return wrapData(workFlowWorkerRepository.save(workFlowWorker));

    }

    public RemoteData<WorkFlowArranger> saveArranger(WorkFlowArranger workFlowWorker) {
        return wrapData(workFlowArrangerRepository.save(workFlowWorker));
    }

    public RemoteData<WorkFlowArranger> findArrangers() {


        return wrapData(workFlowArrangerRepository.findAll());
    }

    public RemoteData<Void> deleteWorkFlowArranger(long id) {
        workFlowArrangerRepository.delete(id);
        return wrapData();
    }

    public RemoteData<Void> deleteWorkFlowWorker(long id) {


        workFlowWorkerRepository.delete(id);
        return wrapData();
    }

    public List<WorkFlowWorker> getWorkFlowWorkers(long userId) {


        return workFlowWorkerRepository.findByUserIdEquals(userId);


    }

    public WorkFlowArranger getWorkFlowArranger(long userId) {


        return workFlowArrangerRepository.findFirstByUserIdEquals(userId);

    }

    public RemoteData<WorkFlowMessage> getOrderItemWorkFlowMessage(User user, long orderItemWorkFlowId, int workFlowStep) {



        List<WorkFlowMessage> messages=workFlowMessageRepository.findByToFlowStepEqualsAndOrderItemWorkFlowIdEqualsOrderByCreateTimeDesc(workFlowStep,orderItemWorkFlowId);





        return wrapData(messages);




    }
}
