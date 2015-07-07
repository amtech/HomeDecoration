package com.giants3.hd.utils;

import com.giants3.hd.utils.exception.HdException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * 辅助类
 * Created by davidleen29 on 2015/7/6.
 */
public class GsonUtils {
    public static Gson gson=new Gson();


    public static String toJson(Object o)
    {
            return gson.toJson(o);
    }


    public   static  <T> T fromJson(String json, Type typeOfT)throws  HdException
    {

        try{

            return gson.fromJson(json,typeOfT);}catch (Throwable  e)
        {

            e.printStackTrace();
            throw HdException.create("error Json String：\n "+json);

        }
    }
}
