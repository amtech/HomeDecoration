package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.module.*;
import com.giants3.hd.domain.repository.*;
import com.giants3.hd.utils.entity.HdTask;
import com.giants3.hd.utils.entity.OrderAuth;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity.StockOutAuth;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.schedulers.Schedulers;

import java.io.File;
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
    ProductRepository productRepository;
    @Inject
    XiankangRepository xiankangRepository;
    @Inject
    QuotationRepository quotationRepository;
    @Inject
    StockRepository stockRepository;

    @Inject
    FileRepository fileRepository;


    @Inject
    AuthRepository authRepository;

    private UseCaseFactory() {


        Guice.createInjector(new HdTaskModule(), new QuotationModule(), new OrderModule(), new ProductModule(), new StockModule(), new FileModule(),new AuthModule()).injectMembers(this);

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


    public UseCase createOrderListUseCase(String key,long salesId, int pageIndex, int pageSize) {

        return new GetOrderListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, salesId,pageIndex, pageSize, orderRepository);
    }

    public UseCase createOrderItemListUseCase(String os_no) {


        return new GetOrderItemListUseCase(Schedulers.newThread(), Schedulers.immediate(), os_no, orderRepository);
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
    public UseCase createStockOutListUseCase(String key,long  salesId,int pageIndex, int pageSize) {

        return new GetStockOutListUseCase(Schedulers.newThread(), Schedulers.immediate(), key,salesId, pageIndex, pageSize, stockRepository);
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
     * @param orderDetail
     * @return
     */
    public UseCase saveOrderDetail(ErpOrderDetail orderDetail) {
        return new SaveOrderDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), orderDetail, orderRepository);
    }

    /**
     * 读取报价明细权限
     * @return
     */
    public UseCase createQuoteAuthListCase() {

        return new GetQuoteAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(),  authRepository);
    }


    /**
     * 读取订单明细权限
     * @return
     */
    public UseCase createOrderAuthListCase() {

        return new GetOrderAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(),  authRepository);
    }



    /**
     * 读取出库明细权限
     * @return
     */
    public UseCase createStockOutAuthListCase() {

        return new GetStockOutAuthListUseCase(Schedulers.newThread(), Schedulers.immediate(),  authRepository);
    }

    /**
     * 获取保存出库权限用例
     * @param stockOutAuths
     * @return
     */
    public UseCase createStockOutAuthSaveCase(List<StockOutAuth> stockOutAuths) {

        return new SaveStockOutAuthUseCase(Schedulers.newThread(), Schedulers.immediate(),stockOutAuths , authRepository);



    }


    /**
     * 获取保存订单权限用例
     * @param orderAuths
     * @return
     */
    public UseCase createOrderAuthSaveCase(List<OrderAuth> orderAuths) {

        return new SaveOrderAuthUseCase(Schedulers.newThread(), Schedulers.immediate(),orderAuths , authRepository);



    }

    /**
     * 获取保存报价权限用例
     * @param quoteAuths
     * @return
     */
    public UseCase createQuoteAuthSaveCase(List<QuoteAuth> quoteAuths) {

        return new SaveQuoteAuthUseCase(Schedulers.newThread(), Schedulers.immediate(),quoteAuths , authRepository);



    }

    /**
     * 订单报表查询
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createOrderReportUseCase(String key,   String dateStart,String dateEnd, int pageIndex, int pageSize) {
       return new GetOrderReportUseCase(Schedulers.newThread(), Schedulers.immediate(), key, dateStart,dateEnd,pageIndex, pageSize, orderRepository);

    }
}
