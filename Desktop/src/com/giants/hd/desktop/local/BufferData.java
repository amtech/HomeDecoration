package com.giants.hd.desktop.local;

import com.giants3.hd.utils.entity.*;
import com.google.inject.internal.InternalFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 本地缓存数据
 */
public class BufferData {


    public static List<PClass> pClasses=new ArrayList<>();
    public static List<Customer> customers=new ArrayList<>();
    public static List<PackMaterialType> packMaterialTypes=new ArrayList<>();
    public static List<PackMaterialPosition> packMaterialPositions=new ArrayList<>();
    public static List<PackMaterialClass> packMaterialClasses=new ArrayList<>();

    public static List<Pack> packs=new ArrayList<>();
    public static List<MaterialClass> materialClasses=new ArrayList<>();
    public static List<MaterialType> materialTypes=new ArrayList<>();
    public  static List<MaterialEquation> materialEquations=new ArrayList<>();
    public static List<Salesman> salesmans=new ArrayList<>();
            ;

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
        BufferData.packMaterialClasses.addAll(packMaterialClasses);
    }

    public static void setPacks(List<Pack> packs) {
        BufferData.packs.clear();
        BufferData.packs.addAll( packs);



    }

    public static void setMaterialClasses(List<MaterialClass> materialClasses) {
        BufferData.materialClasses.clear();
        BufferData.materialClasses.addAll(materialClasses);
    }

    public static void setMaterialTypes(List<MaterialType> materialTypes) {
        BufferData.materialTypes.clear();
        BufferData.materialTypes.addAll( materialTypes);
    }

    public static void setMaterialEquations(List<MaterialEquation> materialEquations) {
        BufferData.materialEquations.clear();
        BufferData.materialEquations.addAll( materialEquations);
    }



    public static void setCustomers(List<Customer> customers)
    {
        BufferData.customers.clear();
        BufferData.customers.addAll(customers);
    }

    public static void setSalesmans(List<Salesman> salesmans)
    {
        BufferData.salesmans.clear();
        BufferData.salesmans.addAll(salesmans);
    }
}
