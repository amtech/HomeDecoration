package com.giants.hd.desktop.local;

import com.giants3.hd.utils.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地缓存数据
 */
public class BufferData {


    public static List<PClass> pClasses=new ArrayList<>();
    public static List<PackMaterialType> packMaterialTypes=new ArrayList<>();
    public static List<PackMaterialPosition> packMaterialPositions=new ArrayList<>();
    public static List<PackMaterialClass> packMaterialClasses=new ArrayList<>();

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

    public static void setPackMaterialTypes(List<PackMaterialType> packMaterialTypes) {

        BufferData.packMaterialTypes.clear();
        BufferData.packMaterialTypes .addAll(packMaterialTypes);
    }

    public static void setPackMaterialPositions(List<PackMaterialPosition> packMaterialPositions) {

        BufferData.packMaterialPositions.clear();
        BufferData.packMaterialPositions .addAll(packMaterialPositions);

    }

    public static void setPackMaterialClasses(List<PackMaterialClass> packMaterialClasses) {
        BufferData.packMaterialClasses.clear();
        BufferData.packMaterialClasses.addAll( packMaterialClasses);
    }
}
