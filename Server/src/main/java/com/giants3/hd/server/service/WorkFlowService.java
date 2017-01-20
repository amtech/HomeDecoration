package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    WorkFlowRepository workFlowRepository;

    @Autowired
    OrderItemWorkFlowRepository orderItemWorkFlowRepository;
    @Autowired
    OrderItemWorkFlowRepository2 orderItemWorkFlowRepository2;
    @Autowired
    OrderItemWorkFlowStateRepository orderItemWorkFlowStateRepository;


    @Autowired
    WorkFlowProductRepository workFlowProductRepository;

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
     * 获取产品的生产流程
     *
     * @param orderItemId
     * @return
     */
    public List<WorkFlowOrderItem> getOrderItemWorkFlows(long orderItemId) {
        return orderItemWorkFlowRepository.findByOrderItemIdEquals(orderItemId);
    }


    public WorkFlowOrderItem createOrderItemWorkFlow(long orderItemId, Long[] workFlowIds) {

        WorkFlowOrderItem workFlowOrderItem = new WorkFlowOrderItem();
        workFlowOrderItem.orderItemId = orderItemId;

        // workFlowOrderItem.processFlowIds= StringUtils.combine(workFlowIds);


        return workFlowOrderItem;

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
     * 启动订单生产流程
     *
     * @param orderItemWorkFlow
     */
    @Transactional(rollbackFor = {HdException.class})
    public void startOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow) {





        //初始化， 发送第一步


            String[] workFlowIds=orderItemWorkFlow.workFlowIds.split(ConstantData.STRING_DIVIDER_SEMICOLON);
            String[] workFlowNames=orderItemWorkFlow.workFlowNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);
            String[] workFlowTypes=orderItemWorkFlow.workFlowTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
            String[] productTypes=orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
            String[] productTypeNames=orderItemWorkFlow.productTypeNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);
            String[] factoryIds=orderItemWorkFlow.productFactoryIds.split(ConstantData.STRING_DIVIDER_SEMICOLON);
            String[] factoryNames=orderItemWorkFlow.productFactoryNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        OrderItem orderItem = orderIemRepository.findOne(orderItemWorkFlow.orderItemId);

        StringBuilder describe=new StringBuilder();





        int size=productTypes.length;

        //二级排厂  排给厂家



//            WorkFlowMessage message=new WorkFlowMessage();


//            message.fromFlowName;
//            message.fromFLowStep;
//            message.toFlowName;
//            message.toFlowStep;



            //不同产品类型，对应不同的厂家排厂
            for(int i=0;i<size;i++) {

                OrderItemWorkFlowState orderItemWorkFlowState=new OrderItemWorkFlowState();
                orderItemWorkFlowState.orderId=orderItemWorkFlow.orderId;
                orderItemWorkFlowState.orderItemId=orderItemWorkFlow.orderItemId;
                orderItemWorkFlowState.orderName=orderItemWorkFlow.orderName;
                orderItemWorkFlowState.workFlowType=workFlowTypes[0];
                orderItemWorkFlowState.workFlowId=Long.valueOf(workFlowIds[0]);
                orderItemWorkFlowState.workFlowName=workFlowNames[0];

                orderItemWorkFlowState.nextWorkFlowType=workFlowTypes[1];
                orderItemWorkFlowState.nextWorkFlowId=Long.valueOf(workFlowIds[1]);
                orderItemWorkFlowState.nextWorkFlowName=workFlowNames[1];


                if("1".equals(workFlowTypes[0])) {
                    orderItemWorkFlowState.productType = productTypes[i];
                    orderItemWorkFlowState.productTypeName = productTypeNames[i];


                    orderItemWorkFlowState.factoryId = Long.parseLong(factoryIds[i]);
                    orderItemWorkFlowState.workFlowName = factoryNames[i];
                    orderItemWorkFlowState.qty = orderItem.qty;


                }
                orderItemWorkFlowState.qty = orderItem.qty;
                orderItemWorkFlowState.orderQty = orderItem.qty;
                orderItemWorkFlowState.createTime = Calendar.getInstance().getTimeInMillis();
                orderItemWorkFlowState.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


                orderItemWorkFlowStateRepository.save(orderItemWorkFlowState);


                describe.append( orderItemWorkFlowState.getMessage());
                describe.append("\n\r");


            }


        orderItemWorkFlow.workFlowDiscribe=describe.toString();
        orderItemWorkFlow = orderItemWorkFlowRepository2.save(orderItemWorkFlow);

















        if (orderItem != null) {

            orderItem.orderWorkFlowId = orderItemWorkFlow.id;
            orderItem.workFlowDescribe =describe.toString();
             orderIemRepository.save(orderItem);
        }




    }

    public RemoteData<OrderItemWorkFlow> findWorkFlowByOrderItemId(long orderItemId) {

        OrderItemWorkFlow orderItemWorkFlow=orderItemWorkFlowRepository2.findFirstByOrderItemIdEquals(orderItemId);
        return wrapData(orderItemWorkFlow);
    }

    public RemoteData<OrderItemWorkFlowState> findOrderItemWorkFlowState(long orderItemId) {


        List<OrderItemWorkFlowState>   list=orderItemWorkFlowStateRepository.findAll();
        return wrapData(list);

    }
}
