package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.module.*;
import com.giants3.hd.domain.repository.*;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.entity.OutFactory;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.schedulers.Schedulers;

import java.io.File;
import java.sql.Statement;
import java.util.List;


/**
 * 用例工厂类
 * Created by david on 2015/9/16.
 */
public class UseCaseFactory {


    @Inject
    HdTaskRepository taskRepository;

    @Inject
    HdTaskLogRepository taskLogRepository;

    @Inject
    OrderRepository orderRepository;
    @Inject
    WorkFlowRepository workFlowRepository;
    @Inject
    ProductRepository productRepository;
    @Inject
    MaterialRepository materialRepository;
    @Inject
    XiankangRepository xiankangRepository;
    @Inject
    QuotationRepository quotationRepository;
    @Inject
    StockRepository stockRepository;

    @Inject
    FileRepository fileRepository;

    @Inject
    UserRepository userRepository;


    @Inject
    AuthRepository authRepository;

    @Inject
    FactoryRepository factoryRepository;

    private UseCaseFactory() {


        Guice.createInjector(new HdTaskModule(), new QuotationModule(), new OrderModule(), new ProductModule(),
                new StockModule(), new FileModule(), new AuthModule()
                ,new MaterialModule()
        ).injectMembers(this);

    }


    public static UseCaseFactory factory = null;


    public synchronized static UseCaseFactory getInstance() {


        if (factory == null) {

            factory = new UseCaseFactory();

        }
        return factory;
    }


    public UseCase createQuotationDetail(long qutationId) {


        return new GetQuotationDetail(Schedulers.newThread(), Schedulers.immediate(), qutationId, quotationRepository);
    }


    public UseCase createProductByNameBetween(String startName, String endName, boolean withCopy) {


        return new ProductUseCase(Schedulers.newThread(), Schedulers.immediate(), startName, endName, withCopy, productRepository);
    }


    public UseCase createUpdateXiankang() {

        return new UpdateXiankangUseCase(Schedulers.newThread(), Schedulers.immediate(), xiankangRepository);
    }


    public UseCase readTaskListUseCase() {

        return new HdTaskListUseCase(Schedulers.newThread(), Schedulers.immediate(), taskRepository);
    }

    public UseCase addHdTaskUseCase(HdTask hdTask) {

        return new HdTaskAddUseCase(Schedulers.newThread(), Schedulers.immediate(), hdTask, taskRepository);
    }


    public UseCase deleteHdTaskUseCase(long taskId) {


        return new HdTaskDeleteUseCase(Schedulers.newThread(), Schedulers.immediate(), taskId, taskRepository);
    }

    public UseCase findTaskLogUseCase(long taskId) {

        return new HdTaskLogListUseCase(Schedulers.newThread(), Schedulers.immediate(), taskId, taskLogRepository);

    }

    public UseCase createProductByNameRandom(String productList, boolean withCopy) {

        return new ProductRandomUseCase(Schedulers.newThread(), Schedulers.immediate(), productList, withCopy, productRepository);
    }


    public UseCase createOrderListUseCase(String key, long salesId, int pageIndex, int pageSize) {

        return new GetOrderListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, salesId, pageIndex, pageSize, orderRepository);
    }

    public UseCase createOrderItemListUseCase(String os_no) {


        return new GetOrderItemListUseCase(Schedulers.newThread(), Schedulers.immediate(), os_no, orderRepository);
    }
    public UseCase searchOrderItemListUseCase(String key,int pageIndex, int pageSize) {


        return new SearchOrderItemListUseCase(Schedulers.newThread(), Schedulers.immediate(), key,  pageIndex,   pageSize, orderRepository);
    }

    public UseCase createGetProductByPrdNo(String prdNo) {
        return new GetProductByPrdNoUseCase(Schedulers.newThread(), Schedulers.immediate(), prdNo, productRepository);
    }


    /**
     * 读取出库列表case
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createStockOutListUseCase(String key, long salesId, int pageIndex, int pageSize) {

        return new GetStockOutListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, salesId, pageIndex, pageSize, stockRepository);
    }

    /**
     * 读取销库列表case
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createStockXiaokuListUseCase(String key, int pageIndex, int pageSize) {

        return new GetStockXiaokuListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, stockRepository);
    }

    /**
     * 读取进库与缴库数据case
     *
     * @param key
     * @param startDate
     * @param endDate
     * @return
     */
    public UseCase createStockInAndSubmitListUseCase(String key, String startDate, String endDate) {

        return new StockInAndSubmitListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, startDate, endDate, stockRepository);

    }

    /**
     * 读取出库列表case
     *
     * @param ck_no 出库单号
     * @return
     */
    public UseCase createStockOutDetailUseCase(String ck_no) {

        return new GetStockOutDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), ck_no, stockRepository);
    }

    /**
     * 上传临时文件
     *
     * @param file
     * @return
     */
    public UseCase uploadTempFileUseCase(File[] file) {

        return new UploadTempFileUseCase(Schedulers.newThread(), Schedulers.immediate(), file, fileRepository);

    }

    /**
     * @param erpStockOutDetail
     * @return
     */
    public UseCase saveStockOutDetail(ErpStockOutDetail erpStockOutDetail) {

        return new SaveStockOutDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), erpStockOutDetail, stockRepository);
    }

    public UseCase createOrderDetailUseCase(String os_no) {

        return new GetOrderDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), os_no, orderRepository);
    }

    /**
     * 保存订单详情用例
     *
     * @param orderDetail
     * @return
     */
    public UseCase saveOrderDetail(ErpOrderDetail orderDetail) {
        return new SaveOrderDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), orderDetail, orderRepository);
    }

    /**
     * 读取报价明细权限
     *
     * @return
     */
    public UseCase createQuoteAuthListCase() {

        return new GetQuoteAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(), authRepository);
    }


    /**
     * 读取订单明细权限
     *
     * @return
     */
    public UseCase createOrderAuthListCase() {

        return new GetOrderAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(), authRepository);
    }


    /**
     * 读取出库明细权限
     *
     * @return
     */
    public UseCase createStockOutAuthListCase() {

        return new GetStockOutAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(), authRepository);
    }

    /**
     * 获取保存出库权限用例
     *
     * @param stockOutAuths
     * @return
     */
    public UseCase createStockOutAuthSaveCase(List<StockOutAuth> stockOutAuths) {

        return new SaveStockOutAuthUseCase(Schedulers.newThread(), Schedulers.immediate(), stockOutAuths, authRepository);


    }


    /**
     * 获取保存订单权限用例
     *
     * @param orderAuths
     * @return
     */
    public UseCase createOrderAuthSaveCase(List<OrderAuth> orderAuths) {

        return new SaveOrderAuthUseCase(Schedulers.newThread(), Schedulers.immediate(), orderAuths, authRepository);


    }

    /**
     * 获取保存报价权限用例
     *
     * @param quoteAuths
     * @return
     */
    public UseCase createQuoteAuthSaveCase(List<QuoteAuth> quoteAuths) {

        return new SaveQuoteAuthUseCase(Schedulers.newThread(), Schedulers.immediate(), quoteAuths, authRepository);


    }

    /**
     * 订单报表查询
     *
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createOrderReportUseCase(long userId, String dateStart, String dateEnd, int pageIndex, int pageSize) {
        return new GetOrderReportUseCase(Schedulers.newThread(), Schedulers.immediate(), userId, dateStart, dateEnd, pageIndex, pageSize, orderRepository);

    }

    /**
     * 获取生产流程列表
     *
     * @return
     */
    public UseCase createGetWorkFlowUseCase() {

        return new GetWorkFlowUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlowRepository);
    }

    /**
     * 获取生产流程二级流程列表
     *
     * @return
     */
    public UseCase createGetWorkFlowSubTypeUseCase() {

        return new GetWorkFlowSubTypeUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlowRepository);
    }

    /**
     * 保存生产流程列表
     *
     * @return
     */
    public UseCase createSaveWorkFlowUseCase(List<WorkFlow> workFlows) {

        return new SaveWorkFlowUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlows, workFlowRepository);
    }


    /**
     * 保存产品相关生产流程列表
     *
     * @return
     */
    public UseCase createSaveWorkProductUseCase(WorkFlowProduct workFlowProduct) {

        return new SaveWorkFlowProductUseCase(
                Schedulers.newThread(), Schedulers.immediate(), workFlowProduct, workFlowRepository);

    }

    /**
     * 获取用户列表
     *
     * @return
     */
    public UseCase createGetUserListUseCase() {

        return new GetUserListUseCase(
                Schedulers.newThread(), Schedulers.immediate(), userRepository);
    }

    /**
     * 启动订单跟踪
     *
     * @return
     */
    public UseCase startOrderTrackUseCase(String os_no) {

        return new StartOrderTrackUseCase(
                Schedulers.newThread(), Schedulers.immediate(), os_no, workFlowRepository);
    }

    public UseCase createUnCompleteOrderWorkFlowReportUseCase() {

        return new UnCompleteOrderWorkFlowReportUseCase(Schedulers.newThread(), Schedulers.immediate(), orderRepository);

    }

    public UseCase createOrderWorkFlowReportUseCase(String key, int pageIndex, int pageSize) {

        return new OrderWorkFlowReportUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, orderRepository);
    }

    public UseCase createStockXiaokuItemListUseCase(String ps_no) {
        return new StockXiaokuItemUseCase(Schedulers.newThread(), Schedulers.immediate(), ps_no, stockRepository);
    }


    public UseCase createStockXiaokuItemListUseCase(String key, final String startDate, final String endDate) {
        return new StockXiaokuItemSearchUseCase(Schedulers.newThread(), Schedulers.immediate(), key, startDate, endDate, stockRepository);
    }

    /**
     * 获取产品的生产流程配置信息
     *
     * @param productId
     * @return
     */
    public UseCase createGetWorkFlowOfProduct(long productId) {

        return new GetWorkFlowOfProduct(Schedulers.newThread(), Schedulers.immediate(), productId, workFlowRepository);
    }


    /**
     * 获取产品的生产流程配置信息
     *
     * @param
     * @return
     */
    public UseCase createGetOutFactoryUseCase() {

        return new GetOutFactoryUseCase(Schedulers.newThread(), Schedulers.immediate(), factoryRepository);
    }

    public UseCase createSaveOutFactoryListUseCase(List<OutFactory> datas) {
        return new SaveOutFactoryListUseCase(Schedulers.newThread(), Schedulers.immediate(), datas, factoryRepository);
    }

    /**
     * 保存排厂类型列表
     *
     * @param datas
     * @return
     */
    public UseCase createSaveWorkFlowSubTypeListUseCase(List<WorkFlowSubType> datas) {
        return new SaveWorkFlowSubTypeListUseCase(Schedulers.newThread(), Schedulers.immediate(), datas, workFlowRepository);
    }


    /**
     * 启动订单生产流程
     *
     * @param orderItemWorkFlow
     * @return
     */
    public UseCase createStartOrderItemWorkFlowUseCase(OrderItemWorkFlow orderItemWorkFlow) {
        return new StartOrderItemWorkFlowUseCase(Schedulers.newThread(), Schedulers.immediate(), orderItemWorkFlow, factoryRepository);

    }

    public UseCase createGetOrderItemWorkFlowState(long orderItemId) {
        return new GetOrderItemWorkFlowState(Schedulers.newThread(), Schedulers.immediate(), orderItemId, workFlowRepository);

    }


    public UseCase createCorrectThumbnaiUseCase(long productId) {
        return new CorrectThumbnaiUseCase(Schedulers.newThread(), Schedulers.immediate(), productId, productRepository);


    }

    public UseCase createGetOrderItemWorkFlow(long orderItemId) {
        return new GetOrderItemWorkFlowUseCase(Schedulers.newThread(), Schedulers.immediate(), orderItemId, orderRepository);
    }

    public UseCase createSearchZhilingdanUseCase(String osName, String startDate, String endDate) {
        return new SearchZhilingdanUseCase(Schedulers.newThread(), Schedulers.immediate(), osName,   startDate,   endDate, workFlowRepository);
    }

    /**
     * 公式改变时候， 进行产品表的数据同步。
     * @return
     */
    public UseCase createSynchronizeProductOnEquationUpdate() {


        return new SynchronizeProductOnEquationUpdateUseCase(Schedulers.newThread(), Schedulers.immediate() ,   productRepository);
    }

    public UseCase createUpdateMaterialClassUseCase(MaterialClass materialClass) {
        return new UpdateMaterialClassUseCase(Schedulers.newThread(), Schedulers.immediate() ,materialClass,   materialRepository);
    }

    public UseCase createDeleteMaterialClassUseCase(long materialClassId) {
        return new DeleteMaterialClassUseCase(Schedulers.newThread(), Schedulers.immediate() ,materialClassId,   materialRepository);
    }

    public UseCase createGetWorkFlowWorkerUseCase() {

        return new GetWorkFlowWorkerUseCase(Schedulers.newThread(), Schedulers.immediate() , workFlowRepository);

    }

    public UseCase createSaveWorkFlowWorkerUseCase(WorkFlowWorker workFlowWorker) {
        return new UpdateWorkFlowWorkerUseCase(Schedulers.newThread(), Schedulers.immediate() ,workFlowWorker,   workFlowRepository);
    }

    public UseCase createSaveWorkFlowArrangerUseCase(WorkFlowArranger workFlowArranger) {
        return new UpdateWorkFlowArrangerUseCase(Schedulers.newThread(), Schedulers.immediate() ,workFlowArranger,   workFlowRepository);
    }

    public UseCase createGetWorkFlowArrangerUseCase() {

        return new GetWorkFlowArrangerUseCase(Schedulers.newThread(), Schedulers.immediate() ,   workFlowRepository);
    }

    public UseCase createDeleteWorkFlowWorkerUseCase(long workFlowWorkerId) {


        return new DeleteWorkFlowWorkerUseCase(Schedulers.newThread(), Schedulers.immediate() ,workFlowWorkerId, workFlowRepository);

    }

    public UseCase createDeleteWorkFlowArrangerUseCase(long workFlowArrangerId) {
        return new DeleteWorkFlowArrangerUseCase(Schedulers.newThread(), Schedulers.immediate() ,workFlowArrangerId, workFlowRepository);
    }


    /**
     * 同步所有产品关联的图片
     * @return
     */
    public UseCase createSyncRelateProductPictureUseCase()
    {

        return new  SyncRelateProductPictureUseCase(Schedulers.newThread(), Schedulers.immediate() , productRepository);
    }

    public UseCase CreateCancelOrderWorkFlowUseCase(long orderItemWorkFlowId) {


        return  new CancelOrderWorkFlowUseCase(orderItemWorkFlowId, orderRepository );
    }
}
