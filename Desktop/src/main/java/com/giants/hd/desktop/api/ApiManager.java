package com.giants.hd.desktop.api;

import com.giants.hd.desktop.exceptions.HdException;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import sun.rmi.runtime.Log;

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

    public RemoteData<Product>  readProductList(String productName,int pageIndex,int pageSize) throws HdException {


        String url=HttpUrl.readProductList(productName, pageIndex, pageSize);
        Logger.getLogger("TEST").info(url);
        String result=     client.postWithStringReturned(url, null);
        Type   generateType = new TypeToken<RemoteData<Product>>() {
        }.getType();

        RemoteData<Product> productRemoteData = gson.fromJson(result, generateType);
        return productRemoteData;


    }


}
