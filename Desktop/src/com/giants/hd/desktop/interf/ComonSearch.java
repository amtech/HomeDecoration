package com.giants.hd.desktop.interf;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;

/**
 *  普通查询接口
 */
public interface ComonSearch<T> {


    public RemoteData<T> search(String value,int pageIndex, int pageCount) throws HdException;
}
