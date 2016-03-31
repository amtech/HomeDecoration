package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.DateFormats;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**被删除的产品的简要信息
 * Created by davidleen29 on 2015/8/25.
 */

public class MaterialDelete implements Serializable {


    public long id;

    public byte[] photo;
    public long materialId;
    public String materialCode;
    public String materialName;
    public long userId;
    public String userName;
    public String userCName;
    public long time;
    public String timeString;


    public String toString() {
        return   materialCode + "_"+materialName+ "_"+userName+ "_"+userCName+ "_"+timeString;

    }



    public void setMaterial(Material material,User user)
    {


        photo=material.photo;
        materialId=material.id;
        materialCode= material.code;
        materialName=material.name;


        userId=user.id                ;
        userName=user.name;
        userCName=user.chineseName;

        time= Calendar.getInstance().getTimeInMillis();
        timeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM_CHINESE.format(new Date(time));

    }






}
