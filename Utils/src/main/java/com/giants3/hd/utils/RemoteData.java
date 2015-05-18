package com.giants3.hd.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络数据交换格式。
 */
public class RemoteData<T> implements Serializable{


    public int pageIndex;
    public int pageCount;
    public int pageSize;
    public List<T> datas=new ArrayList<>();
}
