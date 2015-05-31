package com.giants3.hd.utils.exception;

/**
 * 所有自定义异常的基类
 *
 */
public class HdException extends  Exception {


    public static final int FAIL_ASYNC_CLIENT = 1000;
    public static final int FAIL_READ_RESPONSE_IO = 1001;
    public static final int FAIL_SCALE_IMAGE=1002;






    public String message;
    public int errorCode;

    public static HdException create(int errorCode,Throwable cause)
    {
        HdException hdException= new HdException();
        hdException.errorCode=errorCode;
        return hdException;
    }

    public static HdException create(String message
    )
    {
        HdException hdException= new HdException();
        hdException.errorCode=-1;
        hdException.message=message;
        return hdException;
    }
}
