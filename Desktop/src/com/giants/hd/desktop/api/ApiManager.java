package com.giants.hd.desktop.api;

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
import java.util.List;
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

     @Inject
    Gson gson;




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

        RemoteData<Product> productRemoteData = gson.fromJson(result, generateType);
        return productRemoteData;


    }



    /**
     * 保存产品数据
     * @param productDetail
     * @return
     */
    public RemoteData<ProductDetail>  saveProduct(ProductDetail productDetail) throws HdException {


        String url=HttpUrl.saveProduct();

        String result=     client.postWithStringReturned(url, gson.toJson(productDetail));

        Type   generateType = new TypeToken<RemoteData<ProductDetail>>() {
        }.getType();

        RemoteData<ProductDetail> productRemoteData = gson.fromJson(result, generateType);

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

        RemoteData<ProductDetail> productRemoteData = gson.fromJson(result, generateType);

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
        RemoteData<PClass> remoteData = gson.fromJson(result, generateType);
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
     * 根据物料编码与名称 模糊搜索材料
     * @param value
     * @return
     */
    public RemoteData<Material> loadMaterialByCodeOrName(String value,String classId,int pageIndex,int pageSize) throws HdException {

        String url=HttpUrl.loadMaterialByCodeOrName(value, classId,pageIndex, pageSize);
        String result=     client.getWithStringReturned(url);
        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = gson.fromJson(result, generateType);
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
        RemoteData<PackMaterialType> remoteData = gson.fromJson(result, generateType);
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
        RemoteData<PackMaterialClass> remoteData = gson.fromJson(result, generateType);
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
        RemoteData<PackMaterialPosition> remoteData = gson.fromJson(result, generateType);
        return remoteData;
    }

    /**
     * 保存材料列表
     * @param materials
     */
    public RemoteData<Void> saveMaterials(List<Material> materials) throws HdException {

        String url=HttpUrl.saveMaterials();

        String result=     client.postWithStringReturned(url, gson.toJson(materials));

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
        return remoteData;

    }






    /**
     * 根据材料编码列表 查询材料列表
     * @param codes
     * @return
     */
    public RemoteData<Material> readMaterialListByCodeEquals(List<String> codes) throws HdException{
        String url=HttpUrl.loadMaterialListByCodeEquals();

        String result=     client.postWithStringReturned(url, gson.toJson(codes));

        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = gson.fromJson(result ,generateType);
        return remoteData;
    }

    /**
     * 根据材料名称 查询材料列表
     * @param names
     * @return
     */
    public RemoteData<Material> readMaterialListByNameEquals(List<String> names) throws HdException{
        String url=HttpUrl.loadMaterialListByNameEquals();

        String result=     client.postWithStringReturned(url, gson.toJson(names));

        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<Pack> remoteData = gson.fromJson(result, generateType);
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
        RemoteData<MaterialClass> remoteData = gson.fromJson(result, generateType);
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
        RemoteData<MaterialType> remoteData = gson.fromJson(result, generateType);
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
        RemoteData<MaterialEquation> remoteData = gson.fromJson(result, generateType);
        return remoteData;
    }

    /**
     * 保存材料信息
     * @return
     */
    public RemoteData<Material> saveMaterial(Material material) throws HdException {

        String url=HttpUrl.saveMaterial();

        String result=     client.postWithStringReturned(url, gson.toJson(material));

        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();
        RemoteData<Material> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<ProductDetail> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
        return remoteData;

    }

    public RemoteData<Void> deleteMaterialLogic(long materialId) throws HdException {
        String url=HttpUrl.deleteMaterialLogic(materialId);

        String result=     client.postWithStringReturned(url, null);

        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<Void> remoteData = gson.fromJson(result ,generateType);
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
        RemoteData<ProductProcess> remoteData = gson.fromJson(result, generateType);
        return remoteData;
    }


    /**
     * 保存工序列表数据
     * @param datas
     * @return
     */
    public RemoteData<ProductProcess> saveProductProcess(List<ProductProcess> datas)throws HdException {
        String url=HttpUrl.saveProductProcesses();

        String result=     client.postWithStringReturned(url, gson.toJson(datas));

        Type   generateType = new TypeToken<RemoteData<ProductProcess>>() {
        }.getType();
        RemoteData<ProductProcess> remoteData = gson.fromJson(result ,generateType);
        return remoteData;
    }
}
