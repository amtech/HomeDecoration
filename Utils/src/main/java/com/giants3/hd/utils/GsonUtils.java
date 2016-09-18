package com.giants3.hd.utils;

import com.giants3.hd.utils.exception.HdException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.sun.corba.se.impl.orbutil.ObjectWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 辅助类
 * Created by davidleen29 on 2015/7/6.
 */
public class GsonUtils {

    public static Gson gson=new Gson();


    public static String toJson(Object o)
    {
       return gson.toJson(o);
      // return toJson(o,DEFAULT_CHARSET);
    }

    public static String toIOs(Object o )
    {


       return toJson(o,CHARSET_ISO);

    }

    public static String toJson
            (Object o,Charset charSet)
    {


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(out,
                charSet ) ;
        JsonWriter jsonWriter=new JsonWriter(writer);
        gson.toJson(gson.toJsonTree(o),jsonWriter);
        try {
            jsonWriter.flush();
            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String result=out.toString()  ;
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result ;

    }



    public   static  <T> T fromJson(String json, Type typeOfT)
    {
          return   gson.fromJson(json,typeOfT);
    }

    public static <T> T  fromReader( Reader reader, Type typeOfT)throws  HdException
    {
        try{
            return gson.fromJson(reader,typeOfT);}catch (Throwable  e)
        {

            e.printStackTrace();
            throw HdException.create("error Json String：\n ");

        }
//        Reader json = new InputStreamReader(inputMessage.getBody(),
//                 CHARSET_ISO );
    }

    public static final String UTF_8="UTF-8";

    public static final Charset DEFAULT_CHARSET = Charset.forName(UTF_8);

    public static final Charset  CHARSET_ISO = Charset.forName("ISO-8859-1");


}
