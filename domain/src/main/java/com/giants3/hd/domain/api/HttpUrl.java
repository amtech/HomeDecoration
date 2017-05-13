package com.giants3.hd.domain.api;

import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.StringUtils;
import com.ning.http.util.UTF8UrlEncoder;

import java.net.URLEncoder;


/**
 * 网络请求  url 串
 * <p/>
 * 读取 load 开头
 * <p/>
 * 保存 save 开头
 */
public class HttpUrl {


    public static final String URL_ENCODING = "UTF-8";

    // public  static     String BaseUrl="http://192.168.10.198:8080/Server/";
    public static String BaseUrl = "http://127.0.0.1:8080/";
    //表示桌面端请求

    private static int versionCode;

    public static final void iniBaseUrl
            (String configUrl) {
        BaseUrl = configUrl;
    }


    public static String TOKEN = "";


    public static void setToken(String newToken) {


        TOKEN = newToken;
    }

    public static String additionInfo(String url) {

        if (url.contains("?")) {
            url += "&appVersion=" + versionCode;
        } else {
            url += "?appVersion=" + versionCode;
        }

        if (url.contains("?")) {
            url += "&client=" + ConstantData.CLIENT_DESK;
        } else {
            url += "?client=" + ConstantData.CLIENT_DESK;
        }

        if (StringUtils.isEmpty(TOKEN)) {
            return url;
        }

        if (url.contains("?")) {
            url += "&token=" + TOKEN;
        } else {
            url += "?token=" + TOKEN;
        }


        return url;
    }

    public static void setVersionCode(int versionCode) {
        HttpUrl.versionCode = versionCode;
    }


    public static String loadProductList(String productName, int pageIndex, int pageSize) {
        return additionInfo(BaseUrl + "api/product/search?proName=" + productName + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);
    }

    public static String loadProductListByNameBetween(String startName, String endName, boolean withCopy) {
        return additionInfo(BaseUrl + "api/product/loadByNameBetween?startName=" + startName + "&endName=" + endName + "&withCopy=" + withCopy);
    }


    public static String loadDeleteProducts(String value, int pageIndex, int pageSize) {

        return additionInfo(BaseUrl + "api/product/searchDelete?proName=" + value + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);


    }


    public static String loadDeleteQuotations(String value, int pageIndex, int pageSize) {

        return additionInfo(BaseUrl + "api/quotation/searchDelete?keyword=" + value + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);


    }


    /**
     * 恢复被删除产品
     *
     * @param deleteProductId
     * @return
     */
    public static String resumeDeleteProduct(long deleteProductId) {

        return additionInfo(BaseUrl + "api/product/resumeDelete?deleteProductId=" + deleteProductId);
    }

    /**
     * 恢复被删除报价单
     *
     * @param deleteQuotationId
     * @return
     */
    public static String resumeDeleteQuotation(long deleteQuotationId) {

        return additionInfo(BaseUrl + "api/quotation/resumeDelete?deleteQuotationId=" + deleteQuotationId);
    }

    /**
     * 保存产品信息
     *
     * @return
     */
    public static String saveProduct() {
        return additionInfo(BaseUrl + "api/product/save");
    }


    /**
     * 读取产品详情
     *
     * @param id
     * @return
     */
    public static String loadProductDetail(long id) {
        return additionInfo(BaseUrl + "api/product/detail?id=" + id);
    }


    /**
     * 读取已经删除产品详情
     *
     * @param deleteProductId
     * @return
     */
    public static String productDetailDelete(long deleteProductId) {
        return additionInfo(BaseUrl + "api/product/detailDelete?id=" + deleteProductId);
    }


    /**
     * 读取已经删除报价详情
     *
     * @param quotationDeleteId
     * @return
     */
    public static String quotationDetailDelete(long quotationDeleteId) {
        return additionInfo(BaseUrl + "api/quotation/detailDelete?id=" + quotationDeleteId);
    }
//    /**
//     * 生成读取产品图片的url
//     * @param productName
//     * @return
//     */
//    public static String loadProductPicture(String productName,String version ) {
//        String url= BaseUrl+"api/file/download/product/"+productName+"/"+version ;
//        url+=  ".jpg";
//        return  url ;
//    }


    /**
     * 生成读取产品图片的url
     *
     * @param productUrl
     * @return
     */
    public static String loadProductPicture(String productUrl) {
        String url = BaseUrl + productUrl;
        return url;
    }

    /**
     * 生成图片的url
     *
     * @param relativeUrl
     * @return
     */
    public static String loadPicture(String relativeUrl) {
        String url = BaseUrl + relativeUrl;
        return url;
    }

    /**
     * 读取产品类型
     *
     * @return
     */
    public static String loadProductClass() {

        return additionInfo(BaseUrl + "api/productClass/list");

    }

    /**
     * 读取业务员
     *
     * @return
     */
    public static String loadSalesmans() {

        return additionInfo(BaseUrl + "api/salesman/list");

    }


    /**
     * 保存业务员业务员
     *
     * @return
     */
    public static String saveSalesmans() {

        return additionInfo(BaseUrl + "api/salesman/save");

    }

    /**
     * 读取客户
     *
     * @return
     */
    public static String loadCustomers() {

        return additionInfo(BaseUrl + "api/customer/list");

    }


    /**
     * 保存客户列表
     *
     * @return
     */
    public static String saveCustomers() {
        return additionInfo(BaseUrl + "api/customer/save");
    }


    public static String loadMaterialByCodeOrName(String value, String classId, int pageIndex, int pageSize
    ) {

        return additionInfo(BaseUrl + "api/material/search?codeOrName=" + UTF8UrlEncoder.encode(value) + "&classId=" + classId + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);

    }


    /**
     * 包装材料类型
     *
     * @return
     */
    public static String loadPackMaterialType() {
        return additionInfo(BaseUrl + "api/packMaterialType/list");
    }

    /**
     * 包装材料大分类
     *
     * @return
     */
    public static String loadPackMaterialClass() {
        return additionInfo(BaseUrl + "api/packMaterialClass/list");
    }

    /**
     * 包装材料使用位置
     *
     * @return
     */
    public static String loadPackMaterialPosition() {
        return additionInfo(BaseUrl + "api/packMaterialPosition/list");
    }


    /**
     * 保存材料列表
     *
     * @return
     */
    public static String saveMaterials() {
        return additionInfo(BaseUrl + "api/material/saveList");
    }


    /**
     * 根据材料编码列表 查询材料列表
     */
    public static String loadMaterialListByCodeEquals() {
        return additionInfo(BaseUrl + "api/material/findListByCodes");
    }

    /**
     * 根据材料名称列表 查询材料列表
     */
    public static String loadMaterialListByNameEquals() {
        return additionInfo(BaseUrl + "api/material/findListByNames");
    }


    /**
     * 读取包装列表
     *
     * @return
     */
    public static String loadPacks() {

        return additionInfo(BaseUrl + "api/pack/list");

    }


    /**
     * 获取材料类型
     *
     * @return
     */
    public static String loadMaterialTypes() {

        return additionInfo(BaseUrl + "api/material/listType");
    }


    /**
     * 获取材料分类
     *
     * @return
     */
    public static String loadMaterialClasses() {

        return additionInfo(BaseUrl + "api/material/listClass");
    }

    public static String saveMaterial() {
        return additionInfo(BaseUrl + "api/material/save");
    }

    /**
     * 读取材料计算公式
     *
     * @return
     */
    public static String loadMaterialEquations() {
        return additionInfo(BaseUrl + "api/material/listEquation");

    }


    /**
     * 复制产品  翻新
     *
     * @return
     */
    public static String copyProductDetail(long id, String productName, String version, boolean copyPicture) {
        return additionInfo(BaseUrl + "api/product/copy?id=" + id + "&name=" + productName + "&version=" + version + "&copyPicture=" + copyPicture);
    }

    public static String deleteProductLogic(long productId) {
        return additionInfo(BaseUrl + "api/product/logicDelete?id=" + productId);
    }


    public static String deleteMaterialLogic(long materialId) {
        return additionInfo(BaseUrl + "api/material/logicDelete?id=" + materialId);
    }

    /**
     * 同步材料图片
     *
     * @return
     */
    public static String syncMaterialPhoto() {
        return additionInfo(BaseUrl + "api/material/syncPhoto");
    }

    /**
     * 同步产品图片
     *
     * @return
     */
    public static String syncProductPhoto() {

        return additionInfo(BaseUrl + "api/product/syncPhoto");
    }


    /**
     * 读取材料图片
     *
     * @param materialUrl
     * @return
     */
    public static String loadMaterialPicture(String materialUrl) {
        String url = BaseUrl + materialUrl;
        return url;
    }


    /**
     * 上传文文件的uRl
     *
     * @param productName
     * @return
     */
    public static String uploadProductPicture(String productName, boolean doesOverride) {
        return additionInfo(BaseUrl + "api/file/uploadProduct?name=" + productName + "&doesOverride=" + doesOverride);
    }


    /**
     * 上传材料图片文件的uRl
     *
     * @param materialName
     * @return
     */
    public static String uploadMaterialPicture(String materialName, boolean doesOverride) {
        return additionInfo(BaseUrl + "api/file/uploadMaterial?name=" + materialName + "&doesOverride=" + doesOverride);
    }


    /**
     * 工序列表url
     *
     * @return
     */
    public static String loadProductProcess() {

        return additionInfo(BaseUrl + "api/process/list");
    }

    /**
     * 保存工序列表数据
     *
     * @return
     */
    public static String saveProductProcesses() {

        return additionInfo(BaseUrl + "api/process/saveList");
    }

    /**
     * 模糊查询工序
     *
     * @param value
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static String loadProcessByCodeOrName(String value, int pageIndex, int pageSize) {
        return additionInfo(BaseUrl + "api/process/search?name=" + UTF8UrlEncoder.encode(value) + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);

    }


    /**
     * 同步erp 材料
     *
     * @return
     */
    public static String syncErpMaterial() {
        return additionInfo(BaseUrl + "api/material/syncERP");
    }

    /**
     * 保存材料分类数据
     *
     * @return
     */
    public static String saveMaterialClasses() {
        return additionInfo(BaseUrl + "api/material/saveClassList");
    }


    /**
     * 读取报价记录
     *
     * @param searchValue
     * @param salesmanId
     * @param pageIndex
     * @param pageSize    @return
     */
    public static String loadQuotation(String searchValue, long salesmanId, int pageIndex, int pageSize
    ) {

        return additionInfo(BaseUrl + "api/quotation/search?searchValue=" + UTF8UrlEncoder.encode(searchValue) + "&salesmanId=" + salesmanId + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);

    }

    /**
     * 读取报价详情
     *
     * @param id
     * @return
     */
    public static String loadQuotationDetail(long id) {

        return additionInfo(BaseUrl + "api/quotation/detail?id=" + id);

    }

    /**
     * 保存报价详细数据
     *
     * @return
     */
    public static String saveQuotationDetail() {
        return additionInfo(BaseUrl + "api/quotation/save");
    }


    /**
     * 保存并审核详细数据
     *
     * @return
     */
    public static String saveAndVerifyQuotationDetail() {
        return additionInfo(BaseUrl + "api/quotation/verify");
    }


    /**
     * 撤销报价单审核
     *
     * @return
     */
    public static String unVerifyQuotation(long quotationId) {
        return additionInfo(BaseUrl + "api/quotation/unVerify?quotationId=" + quotationId);
    }


    public static String deleteQuotationLogic(long quotationId) {

        return additionInfo(BaseUrl + "api/quotation/logicDelete?id=" + quotationId);
    }


    /**
     * 读取报价文件
     *
     * @return
     */
    public static String loadQuotationFile(String name) {

        return loadQuotationFile(name, null);
    }

    /**
     * 读取报价文件
     *
     * @return
     */
    public static String loadQuotationFile(String name, String appendix) {

        String url = BaseUrl + "api/file/download/quotation?name=" + UTF8UrlEncoder.encode(name);
        if (!StringUtils.isEmpty(appendix)) {
            url += "&appendix=" + appendix;
        }
        return url;
    }

    public static String loadUsers() {
        return additionInfo(BaseUrl + "api/user/list");
    }


    /**
     * 读取用户权限
     *
     * @param id
     * @return
     */
    public static String loadAuthorityByUser(long id) {

        return additionInfo(BaseUrl + "api/authority/findByUser?userId=" + id);


    }

    /**
     * 保存权限数据
     *
     * @return
     */
    public static String saveAuthorities(long userId) {
        return additionInfo(BaseUrl + "api/authority/saveList?userId=" + userId);
    }

    public static String readSameNameProductList(String product2Name, long productId) {


        return additionInfo(BaseUrl + "api/product/searchByName?proName=" + product2Name + "&productId=" + productId);

    }

    public static String login() {
        return additionInfo(BaseUrl + "api/authority/login2");

    }

    public static String loadModules() {

        return additionInfo(BaseUrl + "api/authority/moduleList");

    }

    public static String saveUsers() {
        return additionInfo(BaseUrl + "api/user/saveList");

    }

    public static String saveModules() {

        return additionInfo(BaseUrl + "api/authority/saveModules");
    }

    public static String loadInitData() {
        return additionInfo(BaseUrl + "api/user/initData");
    }

    public static String updatePassword() {


        return additionInfo(BaseUrl + "api/user/updatePassword");

    }


    /**
     * 读取app 版本信息
     *
     * @return
     */
    public static String readAppVersion() {

        return additionInfo(BaseUrl + "api/authority/loadAppVersion");
    }

    public static String loadApp() {

        return additionInfo(BaseUrl + "api/file/download/app");
    }

    /**
     * 读取报价权限列表
     *
     * @return
     */
    public static String readQuoteAuth() {

        return additionInfo(BaseUrl + "api/authority/quoteAuthList");
    }

    public static String saveQuoteAuthList() {

        return additionInfo(BaseUrl + "api/authority/saveQuoteList");
    }

    public static String readOperationLog(String className, long id) {

        return additionInfo(BaseUrl + "api/operationLog/search?className=" + className + "&recordId=" + id);
    }


    public static String savePackMaterialClass() {

        return additionInfo(BaseUrl + "api/packMaterialClass/saveList");
    }


    /**
     * 设置系统固定参数
     *
     * @return
     */
    public static String setGlobalData() {


        return additionInfo(BaseUrl + "api/application/setGlobal");

    }


    /**
     * 更新咸康数据  数据库结构变动，产生的调整接口 临时使用
     *
     * @return
     */
    public static String updateXiankang() {


        return additionInfo(BaseUrl + "api/application/updateXiankang");

    }

    /**
     * 读取指定ids的产品数据
     *
     * @return
     */
    public static String readProductsByIds() {

        return additionInfo(BaseUrl + "api/product/findByIds");
    }

    public static String uploadTempPicture() {

        return additionInfo(BaseUrl + "api/file/uploadTemp");
    }


    /**
     * 生成读取临时图片的url
     *
     * @param name
     * @return
     */
    public static String loadTempPicture(String name) {
        String url = BaseUrl + "api/file/download/temp/" + name;
        url += ".jpg";
        return url;
    }


    /**
     * 生成读取临时图片的url
     *
     * @param name
     * @return
     */
    public static String loadAttachPicture(String name) {
        String url = BaseUrl + "api/file/download/attach/" + URLEncoder.encode(name);
        url += ".jpg";
        return url;
    }

    public static String loadXiankangDataByProductId(long productId) {

        return additionInfo(BaseUrl + "api/product/findXiankang?productId=" + productId);

    }

    public static String loadTaskList() {
        return additionInfo(BaseUrl + "api/task/list");

    }


    public static String addHdTask() {
        return additionInfo(BaseUrl + "api/task/schedule");

    }


    public static String deleteHdTask(long hdTaskId) {
        return additionInfo(BaseUrl + "api/task/delete?id=" + hdTaskId);

    }

    public static String loadHdTaskLogList(long taskId) {
        return additionInfo(BaseUrl + "api/task/listLog?taskId=" + taskId);
    }

    /**
     * 读取包装材料录入模板
     *
     * @return
     */
    public static String readProductPackTemplate() {

        return additionInfo(BaseUrl + "api/product/listProductPackTemplate");
    }

    /**
     * 读取包装材料录入模板
     *
     * @return
     */
    public static String saveProductPackMaterialTemplate() {

        return additionInfo(BaseUrl + "api/product/saveProductPackTemplate");
    }

    /**
     * /**
     * 随机查询货号   loadProductListByNameRandom 的兼容接口
     *
     * @param prdName
     * @param withCopy
     * @return
     */
    public static String loadProductListByNameRandom(String prdName, boolean withCopy) {
        return additionInfo(BaseUrl + "api/product/loadByNameRandom2?productNames=" + prdName + "&withCopy=" + withCopy);
    }

    /**
     * 读取订单列表
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static String loadOrderList(String key, long salesId, int pageIndex, int pageSize) {

        return additionInfo(BaseUrl + "api/order/list?key=" + key + "&salesId=" + salesId + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);
    }


    public static String loadOrderItemList(String or_no) {

        return additionInfo(BaseUrl + "api/order/findOrderItems?orderNo=" + or_no);

    }


    public static String searchOrderItemList(String key, final int pageIndex, final int pageSize) {

        return additionInfo(BaseUrl + "api/order/searchOrderItems?key=" + key + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);

    }

    /**
     * 根据产品no 读取产品详情
     *
     * @param prdNo
     * @return
     */
    public static String loadProductDetailByPrdNo(String prdNo) {
        return additionInfo(BaseUrl + "api/product/detailByPrdNo?prdNo=" + prdNo);
    }

    /**
     * 读取出库详情
     *
     * @param ck_no
     * @return
     */
    public static String getStockOutDetail(String ck_no) {

        return additionInfo(BaseUrl + "api/stock/findOutDetail?ck_no=" + ck_no);


    }

    /**
     * 读取出库列表
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static String loadStockOutList(String key, long salesId, int pageIndex, int pageSize) {

        return additionInfo(BaseUrl + "api/stock/outList?key=" + key + "&pageIndex=" + pageIndex + "&salesId=" + salesId + "&pageSize=" + pageSize);
    }

    /**
     * 保存出库信息
     *
     * @return
     */
    public static String saveStockOutDetail() {

        return additionInfo(BaseUrl + "api/stock/out/save");
    }

    /**
     * 查询订单详情
     *
     * @param os_no
     * @return
     */
    public static String getOrderDetail(String os_no) {
        return additionInfo(BaseUrl + "api/order/detail?os_no=" + UTF8UrlEncoder.encode(os_no));
    }

    public static String saveOrderDetail() {
        return additionInfo(BaseUrl + "api/order/save");
    }

    /**
     * 读取出库单权限
     *
     * @return
     */
    public static String readStockOutAuth() {

        return additionInfo(BaseUrl + "api/authority/stockOutAuthList");
    }

    /**
     * 读取订单单权限
     *
     * @return
     */
    public static String readOrderAuth() {
        return additionInfo(BaseUrl + "api/authority/orderAuthList");
    }

    public static String saveOrderAuthList() {

        return additionInfo(BaseUrl + "api/authority/saveOrderList");

    }

    public static String saveStockOutAuthList() {
        return additionInfo(BaseUrl + "api/authority/saveStockOutList");
    }

    /**
     * 读取订单报表  验货日期
     *
     * @param userId
     * @param dateStart
     * @param dateEnd
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static String getOrderReportByCheckDate(long userId
            , String dateStart, String dateEnd, int pageIndex, int pageSize) {

        return additionInfo(BaseUrl + "api/order/reportItemByCheckDate?saleId=" + userId + "&dateStart=" + dateStart + "&dateEnd=" + dateEnd + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize);

    }

    public static String getWorkFlowList() {
        return additionInfo(BaseUrl + "api/order/listWorkFlow");

    }

    public static String saveWorkFlowList() {
        return additionInfo(BaseUrl + "api/order/saveWorkFlow");

    }

    public static String startOrderTrack(String os_no) {
        return additionInfo(BaseUrl + "api/order/startOrderTrack?os_no=" + os_no);
    }

    public static String getUnCompleteOrderWorkFlowReport() {

        return additionInfo(BaseUrl + "api/order/unCompleteOrderItem");
    }

    public static String getOrderWorkFlowReport(String key, int pageIndex, int pageSize) {

        return additionInfo(BaseUrl + String.format("api/order/getWorkFlowOrderItem?key=%s&pageIndex=%d&pageSize=%d", key, pageIndex, pageSize));
    }

    public static String getStockInAndSubmitList(String key, String startDate, String endDate) {
        return additionInfo(BaseUrl + "api/stock/stockInAndSubmitList?key=" + key + "&startDate=" + startDate + "&endDate=" + endDate);


    }

    public static String getStockXiaokuItemList(String ps_no) {
        return additionInfo(BaseUrl + "api/stock/xiaokuItemList?ps_no=" + ps_no);

    }


    public static String getStockXiaokuItemList(String key, String dateStart, String dateEnd) {
        return additionInfo(BaseUrl + "api/stock/xiaokuItemSearch?key=" + key + "&dateStart=" + dateStart + "&dateEnd=" + dateEnd);

    }

    public static String getStockXiaokuList(String key, int pageIndex, int pageSize) {
        return additionInfo(BaseUrl + String.format("api/stock/xiaokuList?key=%s&pageIndex=%d&pageSize=%d", key, pageIndex, pageSize));

    }


    /**
     * 流程类型
     *
     * @return
     */
    public static String getWorkFlowTypes() {
        return additionInfo(BaseUrl + String.format("api/workFlow/types"));

    }

    /**
     * 流程二级类型
     *
     * @return
     */
    public static String getWorkFlowSubTypes() {
        return additionInfo(BaseUrl + String.format("api/workFlow/subTypes"));

    }

    public static String getWorkFlowOfProduct(long productId) {
        return additionInfo(BaseUrl + String.format("api/workFlow/findWorkFlowByProductId?productId=%d", productId));
    }

    public static String saveWorkFlowProduct() {

        return additionInfo(BaseUrl + "api/workFlow/saveWorkFlowProduct");

    }

    public static String getOutFactories() {

        return additionInfo(BaseUrl + "api/factory/out/list");
    }

    public static String saveOutFactories() {
        return additionInfo(BaseUrl + "api/factory/out/save");
    }

    public static String startOrderItemWorkFlow() {


        return additionInfo(BaseUrl + "api/workFlow/startOrderItemWorkFlow");
    }

    public static String getOrderItemWorkFlowState(long orderItemId) {

        return additionInfo(BaseUrl + String.format("api/workFlow/orderItemState?orderItemId=%s", orderItemId));
    }

    /**
     * 保存排厂类型列表
     *
     * @return
     */
    public static String saveWorkFlowSubTypeList() {


        return additionInfo(BaseUrl + "api/workFlow/saveSubTypes");

    }

    public static String correctProductThumbnail(long productId) {
        return additionInfo(BaseUrl + "api/product/correctThumbnail?productId=" + productId);
    }

    public static String getOrderItemWorkFlow(long orderItemId) {
        return additionInfo(BaseUrl + "api/order/getOrderItemWorkFlow?orderItemId=" + orderItemId);
    }


    /**
     * 指令单查询
     *
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    public static String searchZhiling(String osName, String startDate, String endDate) {
        return additionInfo(BaseUrl + String.format("api/erpWork/searchZhiling?osName=%s&startDate=%s&endDate=%s", osName, startDate, endDate));
    }


    public static String synchronizeProductOnEquationUpdate() {

        return additionInfo(BaseUrl + "api/product/adjustEquation");
    }

    public static String updateMaterialClass() {

        return additionInfo(BaseUrl + "api/material/updateClass");
    }

    public static String deleteMaterialClass(long materialClassId) {
        return additionInfo(BaseUrl + "api/material/deleteClass?materialClassId=" + materialClassId);
    }

    public static String loadWorkFlowWorks() {
        return additionInfo(BaseUrl + "api/workFlow/workers");
    }

    public static String saveWorkFlowWorker() {

        return additionInfo(BaseUrl + "api/workFlow/saveWorker");
    }

    public static String saveWorkFlowArranger() {

        return additionInfo(BaseUrl + "api/workFlow/saveArranger");
    }

    public static String getWorkFlowArrangers() {
        return additionInfo(BaseUrl + "api/workFlow/arrangers");
    }

    public static String deleteWorkFlowWorker(long workFlowWorkerId) {
        return additionInfo(BaseUrl + "api/workFlow/deleteWorker?id=" + workFlowWorkerId);
    }

    public static String deleteWorkFlowArranger(long workFlowArrangerId) {
        return additionInfo(BaseUrl + "api/workFlow/deleteArranger?id=" + workFlowArrangerId);
    }

    public static String syncRelateProductPicture() {


        return additionInfo(BaseUrl + "api/product/syncRelateProductPicture");
    }

    /**
     * 撤销排厂生产记录
     *
     * @param orderItemWorkFlowId
     * @return
     */
    public static String cancelOrderWorkFlow(long orderItemWorkFlowId) {

        return additionInfo(BaseUrl + "api/order/cancelOrderWorkFlow?orderItemWorkFlowId=" + orderItemWorkFlowId);

    }


    public static String restoreProductDetailFromOperationLog() {

        return additionInfo(BaseUrl + "api/product/restoreFromModify");
    }

    public static String loadWorkFlowEvents() {
        return additionInfo(BaseUrl + "api/workFlow/events");
    }
    public static String loadWorkFlowEventWorkers() {
        return additionInfo(BaseUrl + "api/workFlow/eventWorkers");
    }
}
