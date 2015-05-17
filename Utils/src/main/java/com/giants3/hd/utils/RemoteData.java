package com.giants3.hd.utils;

import java.util.List;

/**
 * 网络数据交换格式。
 */
public class RemoteData<T> {


    public int pageIndex;
    public int pageCount;
    public int pageSize;
    public List<T> datas;
}
