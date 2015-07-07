package com.giants.hd.desktop.api;

import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ning.http.client.*;
import com.ning.http.client.multipart.FilePart;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 *  提供远程请求的客户端类
 */
@Singleton
public class Client {


    public static final String BODY_ENCODING="UTF-8";
    private static final String BASE_URL = "http://api.twitter.com/1/";
    private static final String TAG = "HTTPCLIENT";
    //客户端连接
    public AsyncHttpClient client;
    @Inject
    public Client() {//设置链接参数   默认超时时间6秒
        client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setConnectTimeout(3000).setRequestTimeout(Integer.MAX_VALUE).setReadTimeout(Integer.MAX_VALUE).build());
    }

    @Inject
    MimetypesFileTypeMap mimetypesFileTypeMap;


    public void put()
    {}



    public String postWithStringReturned(String url,String body) throws HdException {
        Logger.getLogger(TAG).info(url);

       String result= post(url,body,     new AsyncCompletionHandler<String>()
            {
                @Override
                public String onCompleted(Response response) throws Exception {

                    String result= response.getResponseBody(BODY_ENCODING);
                    return result;


                }
            });


        Logger.getLogger(TAG).info(result);

        return result;

    }



    public String getWithStringReturned(String url ) throws HdException {

        Logger.getLogger(TAG).info(url);
        String result= get(url, new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {

                String result = response.getResponseBody(BODY_ENCODING);
                return result;


            }
        });




        Logger.getLogger(TAG).info(result);

        return result;
    }


    /**
     * 上传文件
     * @param url
     * @param file
     * @return
     */
    public String uploadWidthStringReturned(String url,File file ) throws HdException {
        Logger.getLogger(TAG).info(url);
        String result= upload(url,file, new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {

                String result = response.getResponseBody(BODY_ENCODING);
                return result;


            }
        });

        Logger.getLogger(TAG).info(result);

        return result;

    }


    public <T> T get(String url,   AsyncHandler<T> handler)throws HdException
    {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.prepareGet(url);
        builder.addHeader("Content-Type","application/json");
        return execute(handler, builder);
    }

    public <T> T post(String url, String body, AsyncHandler<T> handler)throws HdException
    {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type","application/json");
        builder.setBodyEncoding(BODY_ENCODING);


        if (null != body)
            builder.setBody(body);

        return execute(handler, builder);

    }


    /**
     * 上传文件
     * @param url
     * @param file
     * @param handler
     * @param <T>
     * @return
     * @throws HdException
     */
    public <T> T upload(String url,File file,AsyncHandler<T> handler) throws HdException {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type","multipart/*");
        builder.addBodyPart(new FilePart("file",file,mimetypesFileTypeMap.getContentType(file)));

        return execute(handler, builder);

    }

    private <T> T execute(AsyncHandler<T> handler, AsyncHttpClient.BoundRequestBuilder builder) throws HdException {
        try {
            T result = builder.execute(handler).get();


          //  Log.d(TAG, "接收数据 ：     " + result);
            return result
                    ;
        } catch (InterruptedException e) {
            throw HdException.create(HdException.FAIL_ASYNC_CLIENT, e);
        } catch (ExecutionException e) {
            Throwable cause
                    = e.getCause();
            if (cause != null && cause instanceof HdException) {
                throw (HdException) cause;
            } else
                throw HdException.create(HdException.FAIL_ASYNC_CLIENT, e);

        }
    }

    public void get()
    {}

    public void delete()
    {

    }


}
