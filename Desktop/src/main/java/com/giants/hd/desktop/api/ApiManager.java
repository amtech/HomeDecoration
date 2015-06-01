package com.giants.hd.desktop.api;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.RemoteData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.Type;
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
}
