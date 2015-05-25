package com.giants.hd.desktop.local;

import com.giants3.hd.utils.entity.PClass;
import com.giants3.hd.utils.entity.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地缓存数据
 */
public class BufferData {


    public static List<PClass> pClasses=new ArrayList<>();

    public static void setPClasses(List<PClass> classes)
    {
        pClasses.clear();
        pClasses.addAll(classes);
    }



    public static List<Unit> units=new ArrayList<>();

    public static void setUnits(List<Unit> classes)
    {
        units.clear();
        units.addAll(classes);
    }

}
