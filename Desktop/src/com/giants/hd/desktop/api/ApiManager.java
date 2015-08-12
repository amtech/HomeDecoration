package com.giants.hd.desktop.api;

import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.RemoteData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 远程操作接口
 */
@Singleton
public class ApiManager {



    @Inject
    public ApiManager()
    {}
    @Inject
    Client client;






    /**
     * 读取产品列表
     * @param productName
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<Product>  readProductList(String productName,int pageIndex,int pageSize) throws HdException {


        String url=HttpUrl.loadProductList(productName, pageIndex, pageSize);

        String result=     client.postWithStringReturned(url, null);

        Type   generateType = new TypeToken<RemoteData<Product>>() {
        }.getType();

        RemoteData<Product> productRemoteData = GsonUtils.fromJson(result, generateType);
        return productRemoteData;


    }



    /**
     * 保存产品数据
     * @param productDetail
     * @return
     */
    public RemoteData<ProductDetail>  saveProduct(ProductDetail productDetail) throws HdException {


        String url=HttpUrl.saveProduct();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(productDetail));

        Type   generateType = new TypeToken<RemoteData<ProductDetail>>() {
        }.getType();

        RemoteData<ProductDetail> productRemoteData = GsonUtils.fromJson(result, generateType);

        return productRemoteData;


    }

    /**
     * 读取产品信息详情
     * @param id
     * @return
     */
    public RemoteData<ProductDetail> loadProductDetail(long id) throws HdException {


        String url=HttpUrl.loadProductDetail(id);

        String result=     client.postWithStringReturned(url,null);

        Type   generateType = new TypeToken<RemoteData<ProductDetail>>() {
        }.getType();

        RemoteData<ProductDetail> productRemoteData = GsonUtils.fromJson(result, generateType);

        return productRemoteData;

    }


    /**
     * \读取产品类别列表
     * @return
     */

    public RemoteData<PClass> readProductClass() throws HdException {


        String url=HttpUrl.loadProductClass();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<PClass>>() {
        }.getType();
        RemoteData<PClass> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;


    }


    /**
     * \读取客户列表
     * @return
     */

    public RemoteData<Customer> readCustomers() throws HdException {


        String url=HttpUrl.loadCustomers();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Customer>>() {
        }.getType();
        RemoteData<Customer> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;


    }


    /**
     * 保存客户列表
     * @return
     */

    public RemoteData<Customer> saveCustomers(List<Customer> customers) throws HdException {



        String url=HttpUrl.saveCustomers();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(customers));

        Type   generateType = new TypeToken<RemoteData<Customer>>() {
        }.getType();
        RemoteData<Customer> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }


    /**
     * \读取业务员列表
     * @return
     */

    public RemoteData<User> readSalesmans() throws HdException {


        String url=HttpUrl.loadSalesmans();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<User>>() {
        }.getType();
        RemoteData<User> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;


    }


    /**
     * 保存业务员列表
     * @return
     */

    public RemoteData<User> saveSalesmans(List<User> salesmans) throws HdException {



        String url=HttpUrl.saveSalesmans();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(salesmans));

        Type   generateType = new TypeToken<RemoteData<User>>() {
        }.getType();
        RemoteData<User> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }




    /**
     * \读取用户员列表
     * @return
     */

    public RemoteData<User> readUsers() throws HdException {


        String url=HttpUrl.loadUsers();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<User>>() {
        }.getType();
        RemoteData<User> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;


    }


    /**
     * 保存用户列表
     * @return
     */

    public RemoteData<User> saveUsers(List<User> users
    ) throws HdException {



        String url=HttpUrl.saveUsers();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(users));

        Type   generateType = new TypeToken<RemoteData<User>>() {
        }.getType();
        RemoteData<User> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }


    /**
     * \读取用户员列表
     * @return
     */

    public RemoteData<Module> readModules() throws HdException {


        String url=HttpUrl.loadModules();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Module>>() {
        }.getType();
        RemoteData<Module> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;


    }


    /**
     * 保存模块
     * @return
     */

    public RemoteData<Module> saveModules(List<Module> users
    ) throws HdException {



        String url=HttpUrl.saveModules();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(users));

        Type   generateType = new TypeToken<RemoteData<Module>>() {
        }.getType();
        RemoteData<Module> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }
    /**
     * 根据物料编码与名称 模糊搜索材料
     * @param value
     * @return
     */
    public RemoteData<Material> loadMaterialByCodeOrName(String value,int pageIndex,int pageSize) throws HdException {


        return loadMaterialByCodeOrName(  value,  "",  pageIndex,  pageSize);
    }

    /**
     * 根据工序编码名称 模糊搜索工序
     * @param value
     * @return
     */
    public RemoteData<ProductProcess> loadProcessByCodeOrName
    (String value,int pageIndex,int pageSize) throws HdException {

        String url=HttpUrl.loadProcessByCodeOrName(value, pageIndex, pageSize);
        String result=     client.getWithStringReturned(url);
        Type   generateType = new TypeToken<RemoteData<ProductProcess>>() {
        }.getType();
        RemoteData<ProductProcess> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }

    /**
     * 根据物料编码与名称 模糊搜索材料
     * @param value
     * @return
     */
    public RemoteData<Material> loadMaterialByCodeOrName(String value,String classId,int pageIndex,int pageSize) throws HdException {

        String url=HttpUrl.loadMaterialByCodeOrName(value, classId, pageIndex, pageSize);
        String result=     client.getWithStringReturned(url);
        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;

    }


    /**
     * 读取包装材料类型列表
     * @return
     */
    public RemoteData<PackMaterialType> readPackMaterialType()throws HdException {

        String url=HttpUrl.loadPackMaterialType();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<PackMaterialType>>() {
        }.getType();
        RemoteData<PackMaterialType> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }


    /**
     * 读取包装材料大类别列表
     * @return
     */
    public RemoteData<PackMaterialClass> readPackMaterialClass()throws HdException {

        String url=HttpUrl.loadPackMaterialClass();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<PackMaterialClass>>() {
        }.getType();
        RemoteData<PackMaterialClass> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }




    /**
     * 读取包装材料类型列表
     * @return
     */
    public RemoteData<PackMaterialPosition> readPackMaterialPosition()throws HdException {

        String url=HttpUrl.loadPackMaterialPosition();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<PackMaterialPosition>>() {
        }.getType();
        RemoteData<PackMaterialPosition> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }

    /**
     * 保存材料列表
     * @param materials
     */
    public RemoteData<Void> saveMaterials(List<Material> materials) throws HdException {

        String url=HttpUrl.saveMaterials();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(materials));

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }






    /**
     * 根据材料编码列表 查询材料列表
     * @param codes
     * @return
     */
    public RemoteData<Material> readMaterialListByCodeEquals(List<String> codes) throws HdException{
        String url=HttpUrl.loadMaterialListByCodeEquals();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(codes));

        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }

    /**
     * 根据材料名称 查询材料列表
     * @param names
     * @return
     */
    public RemoteData<Material> readMaterialListByNameEquals(List<String> names) throws HdException{
        String url=HttpUrl.loadMaterialListByNameEquals();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(names));

        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 读取包装列表
     * @return
     */
    public RemoteData<Pack> readPacks()throws HdException {

        String url=HttpUrl.loadPacks();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Pack>>() {
        }.getType();
        RemoteData<Pack> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }



    /**
     * 读取材料分类
     * @return
     */
    public RemoteData<MaterialClass> readMaterialClasses()throws HdException {

        String url=HttpUrl.loadMaterialClasses();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<MaterialClass>>() {
        }.getType();
        RemoteData<MaterialClass> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }



    /**
     *  材料分类保存
     * @return
     */
    public RemoteData<MaterialClass> saveMaterialClasses(List<MaterialClass> materialClasses)throws HdException {

        String url=HttpUrl.saveMaterialClasses();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(materialClasses));

        Type   generateType = new TypeToken<RemoteData<MaterialClass>>() {
        }.getType();
        RemoteData<MaterialClass> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }


    /**
     * 读取材料类型
     * @return
     */
    public RemoteData<MaterialType> readMaterialTypes()throws HdException {

        String url=HttpUrl.loadMaterialTypes();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<MaterialType>>() {
        }.getType();
        RemoteData<MaterialType> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }


    /**
     * 读取材料计算公式列表
     * @return
     */
    public RemoteData<MaterialEquation> readMaterialEquations()throws HdException {

        String url=HttpUrl.loadMaterialEquations();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<MaterialEquation>>() {
        }.getType();
        RemoteData<MaterialEquation> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }

    /**
     * 保存材料信息
     * @return
     */
    public RemoteData<Material> saveMaterial(Material material) throws HdException {

        String url=HttpUrl.saveMaterial();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(material));

        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }

    /**
     * 翻新产品
     * @param id
     * @return
     */
    public RemoteData<ProductDetail> copyProductDetail(long id,String productName, String version) throws HdException {

        String url=HttpUrl.copyProductDetail(id, productName, version);

        String result=     client.postWithStringReturned(url, null);

        Type   generateType = new TypeToken<RemoteData<ProductDetail>>() {
        }.getType();
        RemoteData<ProductDetail> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }

    /**
     * 逻辑删除产品数据
     * @return
     */
    public RemoteData<Void> deleteProductLogic(long productId) throws HdException {
        String url=HttpUrl.deleteProductLogic(productId);

        String result=     client.postWithStringReturned(url, null);

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }

    public RemoteData<Void> deleteMaterialLogic(long materialId) throws HdException {
        String url=HttpUrl.deleteMaterialLogic(materialId);

        String result=     client.postWithStringReturned(url, null);

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 同步更新材料图片
     * @return
     */
    public RemoteData<Void>  syncMaterialPhoto() throws HdException {
        String url=HttpUrl.syncMaterialPhoto();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }

    /**
     * 同步更新产品图片
     * @return
     */
    public RemoteData<Void> syncProductPhoto() throws HdException{

        String url=HttpUrl.syncProductPhoto();
        String result=     client.getWithStringReturned(url);
        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 上传产品图片
     * @param file
     * @return
     * @throws HdException
     */
    public RemoteData<Void> uploadProductPicture(File file,boolean doesOverride) throws HdException {



        String productName=file.getName();
        String url=HttpUrl.uploadProductPicture(productName, doesOverride);



        String result=  client.uploadWidthStringReturned(url, file);
        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }
    /**
     * 上传材料图片
     * @param file
     * @return
     * @throws HdException
     */
    public RemoteData<Void> uploadMaterialPicture(File file,boolean doesOverride) throws HdException {



        String materialName=file.getName();
        String url=HttpUrl.uploadMaterialPicture(materialName, doesOverride);

        String result=  client.uploadWidthStringReturned(url,file);
        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 读取工序列表
     * @return
     */
    public RemoteData<ProductProcess> loadProductProcess()throws HdException {

        String url=HttpUrl.loadProductProcess();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<ProductProcess>>() {
        }.getType();
        RemoteData<ProductProcess> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;
    }


    /**
     * 保存工序列表数据
     * @param datas
     * @return
     */
    public RemoteData<ProductProcess> saveProductProcess(List<ProductProcess> datas)throws HdException {
        String url=HttpUrl.saveProductProcesses();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(datas));

        Type   generateType = new TypeToken<RemoteData<ProductProcess>>() {
        }.getType();
        RemoteData<ProductProcess> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 同步erp材料
     * @return
     */
    public RemoteData<Void> syncErpMaterial() throws HdException{
        String url=HttpUrl.syncErpMaterial();

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 读取报价历史记录
     * @param searchValue
     * @return
     */
    public RemoteData<Quotation> loadQuotation(String searchValue,int pageIndex, int pageSize) throws HdException {

        String url=HttpUrl.loadQuotation(searchValue, pageIndex, pageSize);

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Quotation>>() {
        }.getType();
        RemoteData<Quotation> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;

    }



    /**
     * 读取报价详细
     * @param id
     * @return
     */
    public RemoteData<QuotationDetail> loadQuotationDetail(long id) throws HdException {

        String url=HttpUrl.loadQuotationDetail(id);

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<QuotationDetail>>() {
        }.getType();
        RemoteData<QuotationDetail> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;

    }

    /**
     * 保存报价详情数据
     * @param detail
     * @return
     */
    public RemoteData<QuotationDetail> saveQuotationDetail(QuotationDetail detail) throws HdException {

        String url=HttpUrl.saveQuotationDetail();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(detail));

        Type   generateType = new TypeToken<RemoteData<QuotationDetail>>() {
        }.getType();

        RemoteData<QuotationDetail> productRemoteData = GsonUtils.fromJson(result, generateType);

        return productRemoteData;

    }

    /**
     * 删除报价单
     * @param quotationId
     * @return
     */
    public RemoteData<Void> deleteQuotationLogic(long quotationId) throws HdException {


        String url=HttpUrl.deleteQuotationLogic(quotationId);

        String result=     client.postWithStringReturned(url, null);

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }


    /**
     * 读取用户权限
     * @param id
     * @return
     */
    public RemoteData<Authority> readAuthorityByUser(long id) throws HdException {

        String url=HttpUrl.loadAuthorityByUser(id);

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Authority>>() {
        }.getType();
        RemoteData<Authority> remoteData = GsonUtils.fromJson(result, generateType);
        return remoteData;

    }


    /**
     * 保存用户权限
     */
    public RemoteData<Authority> saveAuthorities(long userId,List<Authority> authorities) throws HdException {

        String url=HttpUrl.saveAuthorities(userId);

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(authorities));

        Type   generateType = new TypeToken<RemoteData<Authority>>() {
        }.getType();
        RemoteData<Authority> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }


    /**
     *
     * @param product2Name  货号名称
     * @param productId      排除的id
     * @return
     */
    public RemoteData<Product> readSameNameProductList(String product2Name, long productId) throws HdException {

        String url=HttpUrl.readSameNameProductList(product2Name, productId);

        String result=     client.getWithStringReturned(url);

        Type   generateType = new TypeToken<RemoteData<Product>>() {
        }.getType();
        RemoteData<Product> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }


    /**
     * 登录
     * @param userName
     * @param password
     * @return
     * @throws HdException
     */
    public RemoteData<User> login(String userName, String password) throws HdException {

        String url=HttpUrl.login();
        User user=new User();
        user.name=userName;
        user.password=password;

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(user));

        Type   generateType = new TypeToken<RemoteData<User>>() {
        }.getType();
        RemoteData<User> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }



    /**
     * 获取初始数据
     * @param user
     */
    public RemoteData<BufferData> loadInitData(User user) throws HdException {

        String url=HttpUrl.loadInitData();

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(user));

        Type   generateType = new TypeToken<RemoteData<BufferData>>() {
        }.getType();
        RemoteData<BufferData> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;

    }


    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws HdException
     */
    public RemoteData<Void> updatePassword(long userId, String oldPassword, String newPassword) throws HdException {


        String url=HttpUrl.updatePassword();
        String[] body=new String[3];
        body[0]=String.valueOf(userId
        );
        body[1]=oldPassword;
        body[2]=newPassword;

        String result=     client.postWithStringReturned(url, GsonUtils.toJson(body));

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = GsonUtils.fromJson(result ,generateType);
        return remoteData;
    }
}
