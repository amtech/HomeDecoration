package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统模块表
 */
@Entity(name="T_Module")
public class Module implements Serializable{


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;



    @Basic
    public String name;

    @Basic
    public String title;

    @Override
    public String toString() {

        return "["+name+"] "+title;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Module)) return false;

        Module module = (Module) o;

        if (id != module.id) return false;
        if (name != null ? !name.equals(module.name) : module.name != null) return false;
        return !(title != null ? !title.equals(module.title) : module.title != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }



    public static List<Module> getInitDataList()
    {



        List<Module> data=new ArrayList<>();
        Module module;

        int size=NAMES.length;
        for (int i = 0; i < size; i++) {


            module=new Module();
            module.name=NAMES[i];
            module.title=TITLES[i];
            data.add(module);
        }

          return data;


    }


    public static final String NAME_MATERIAL="material";
    public static final String TITLE_MATERIAL="材料模块";

    public static final String NAME_PRODUCT="Product";
    public static final String TITLE_PRODUCT="产品模块";

    public static final String NAME_QUOTATION="Quotation";
    public static final String TITLE_QUOTATION="报价模块";

    public static final String NAME_AUTHORITY="Authority";
    public static final String TITLE_AUTHORITY="权限设置";

    public static final String NAME_SYNC_DATA="SyncData";
    public static final String TITLE_SYNC_DATA="数据同步";

    public static final String NAME_PICTURE_UPLOAD="PictureUpload";
    public static final String TITLE_PICTURE_UPLOAD="图片上传";

    public static final String NAME_MATERIAL_CLASS="MaterialClass";
    public static final String TITLE_MATERIAL_CLASS="材料类型";

    public static final String NAME_CUSTOMER="Customer";
    public static final String TITLE_CUSTOMER="客户";

    public static final String NAME_PROCESS="Process";
    public static final String TITLE_PROCESS="工序";

    public static final String NAME_USER ="User";
    public static final String TITLE_USER ="用户";


    public static final String NAME_MODULE = "MODULE";
    public static final String TITLE_MODULE = "模块列表";
    public static String[] NAMES=new String[]{NAME_MATERIAL,NAME_PRODUCT,NAME_QUOTATION,NAME_AUTHORITY,NAME_SYNC_DATA,NAME_PICTURE_UPLOAD,NAME_MATERIAL_CLASS,NAME_CUSTOMER,NAME_PROCESS, NAME_USER

    ,NAME_MODULE};

    public static String[] TITLES=new String[]{TITLE_MATERIAL,TITLE_PRODUCT,TITLE_QUOTATION,TITLE_AUTHORITY,TITLE_SYNC_DATA,TITLE_PICTURE_UPLOAD,TITLE_MATERIAL_CLASS,TITLE_CUSTOMER,TITLE_PROCESS, TITLE_USER

   , TITLE_MODULE};

}
