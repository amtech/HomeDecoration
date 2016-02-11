package com.giants3.hd.server.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/**
 * 所有请求封装类，   对于所有远程请求
 */
public class HttpResponseWrapper extends HttpServletResponseWrapper {
    private static  final String TAG="HttpResponseWrapper";
    private MyPrintWriter tmpWriter;

    private ByteArrayOutputStream output;
    public HttpResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
        tmpWriter = new MyPrintWriter(output);
    }
    @Override
    public void finalize() throws Throwable
    {
        super.finalize();
        output.close();
        tmpWriter.close();
    }


    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        ServletOutputStream outputStream= super.getOutputStream();

      //Logger.getLogger(TAG).info(outputStream.toString());
        return outputStream;
    }


    public byte[] toByteArray(){

    return    output.toByteArray();
    }

    public  String  getContent()
    {

        try
        {
            tmpWriter.flush(); //刷新该流的缓冲，详看java.io.Writer.flush()
            String s = tmpWriter.getByteArrayOutputStream().toString("UTF-8");
            //此处可根据需要进行对输出流以及Writer的重置操作
            //比如tmpWriter.getByteArrayOutputStream().reset()
            return s;
        } catch (UnsupportedEncodingException e)
        {
            return "UnsupportedEncoding";
        }

    }



    //覆盖getWriter()方法，使用我们自己定义的Writer
    @Override
    public PrintWriter getWriter() throws IOException
    {
        return tmpWriter;
    }

    public void close() throws IOException
    {
        tmpWriter.close();
    }

    //自定义PrintWriter，为的是把response流写到自己指定的输入流当中
    //而非默认的ServletOutputStream
    private static class MyPrintWriter extends PrintWriter
    {
        ByteArrayOutputStream myOutput; //此即为存放response输入流的对象

        public MyPrintWriter(ByteArrayOutputStream output)
        {
            super(output);
            myOutput = output;
        }




        public ByteArrayOutputStream getByteArrayOutputStream()
        {
            return myOutput;
        }
    }

}