package com.giants3.hd.utils.entity;

/**
 * Created by davidleen29 on 2015/8/8.
 */

import java.util.ArrayList;
import java.util.List;






/**
 * 本地缓存数据
 */
public class BufferData {

    public   List<PClass> pClasses;
    public   List<Customer> customers;
    public   List<PackMaterialType> packMaterialTypes;
    public   List<PackMaterialPosition> packMaterialPositions;
    public   List<PackMaterialClass> packMaterialClasses;

    public   List<Pack> packs=new ArrayList<>();
    public   List<MaterialClass> materialClasses;
    public   List<MaterialType> materialTypes;
    //public    List<MaterialEquation> materialEquations=new ArrayList<>();
    public   List<User> salesmans;
    public List<Authority> authorities;

    public  User loginUser;
}
