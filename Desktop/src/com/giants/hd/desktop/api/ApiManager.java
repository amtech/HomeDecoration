package com.giants.hd.desktop.api;

import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.RemoteData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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


    public static final String TAG = ApiManager.class.getSimpleName();

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
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, null);
        Logger.getLogger(TAG).info(result);
        Type   generateType = new TypeToken<RemoteData<Product>>() {
        }.getType();

        RemoteData<Product> productRemoteData = gson.fromJson(result, generateType);
        return productRemoteData;


    }

    /**
     * 读出材料列表
     * @param materialName
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws HdException
     */
    public RemoteData<Material>  readMaterialList(String materialName,int pageIndex,int pageSize) throws HdException {


        String url=HttpUrl.loadProductList(materialName, pageIndex, pageSize);
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, null);
        Logger.getLogger(TAG).info(result);
        Type   generateType = new TypeToken<RemoteData<Material>>() {
        }.getType();

        RemoteData<Material> productRemoteData = gson.fromJson(result, generateType);

        return productRemoteData;


    }

    /**
     * 保存产品数据
     * @param productDetail
     * @return
     */
    public RemoteData  saveProduct(ProductDetail productDetail) throws HdException {


        String url=HttpUrl.saveProduct();
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, gson.toJson(productDetail));
        Logger.getLogger(TAG).info(result);
        Type   generateType = new TypeToken<RemoteData<Void>>() {
        }.getType();

        RemoteData<Void> productRemoteData = gson.fromJson(result, generateType);

        return productRemoteData;


    }

    /**
     * 读取产品信息详情
     * @param id
     * @return
     */
    public RemoteData<ProductDetail> loadProductDetail(long id) throws HdException {


        String url=HttpUrl.loadProductDetail(id);
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url,null);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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

        String url=HttpUrl.loadMaterialByCodeOrName(value, pageIndex, pageSize);
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, gson.toJson(materials));
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, gson.toJson(codes));
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, gson.toJson(names));
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.getWithStringReturned(url);
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, gson.toJson(material));
        Logger.getLogger(TAG).info(result);
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
        Logger.getLogger(TAG).info(url);
        String result=     client.postWithStringReturned(url, null);
        Logger.getLogger(TAG).info(result);
        Type   generateType = new TypeToken<RemoteData<ProductDetail>>() {
        }.getType();
        RemoteData<ProductDetail> remoteData = gson.fromJson(result ,generateType);
        return remoteData;

    }
}
