package com.giants.hd.desktop.exceptions;

/**
 * 所有自定义异常的基类
 *
 */
public class HdException extends  Exception {


    public static final int FAIL_ASYNC_CLIENT = 1000;
    public static final int FAIL_READ_RESPONSE_IO = 1001;

    public static HdException create(int errorCode,Throwable cause)
    {
        return new HdException();
    }
}
