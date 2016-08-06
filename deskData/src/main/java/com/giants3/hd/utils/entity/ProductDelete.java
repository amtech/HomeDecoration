package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.DateFormats;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**被删除的产品的简要信息
 * Created by davidleen29 on 2015/8/25.
 */

public class ProductDelete implements Serializable {


    public long id;


    public long productId;
    public String productName;
    public String pVersion;
    public long userId;
    public String userName;
    public String userCName;
    public long time;
    public String timeString;
    public String url;


    public String toString() {
        return   productId + "_"+productName+ "_"+pVersion+ "_"+userId+ "_"+userName+ "_"+userCName+ "_"+timeString;

    }



    public void setProductAndUser(Product product,User user)
    {



        productId=product.id;
        productName=product.name;
        pVersion=product.pVersion;

        url=product.url;
        userId=user.id
                ;
        userName=user.name;
        userCName=user.chineseName;

        time= Calendar.getInstance().getTimeInMillis();
        timeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM_CHINESE.format(new Date(time));

    }






}
