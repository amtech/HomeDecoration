package com.giants.hd.desktop.local;

import com.giants3.hd.utils.entity.PClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地缓存数据
 */
public class BufferData {


    public static List<PClass> pClasses=new ArrayList<>();

    public static void set(List<PClass> classes)
    {
        pClasses.clear();
        pClasses.addAll(classes);
    }
}
