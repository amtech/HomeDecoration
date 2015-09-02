package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.write.*;

import java.io.IOException;

/** 报价导出咸康灯具模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_JIAJU extends ExcelReportor {


    public Report_Excel_XK_JIAJU(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {

        int defaultRowCount=10;

        int startRow=4;
        int dataSize=quotationDetail.items.size();
        duplicateRow(writableSheet,startRow,defaultRowCount,dataSize);







        //填充数据




        //报价日期
        addString(writableSheet,quotationDetail.quotation.qDate,5,1);

        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;

        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片


                attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion),4+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);






            addNumber(writableSheet, i + 1, 0, rowUpdate);

            //设计号  版本号
           // addString(writableSheet, item.productName.trim(),2,rowUpdate);

            //货号
            addString(writableSheet, item.productName.trim(), 2, rowUpdate);


            //产品描述
            addString(writableSheet, item.memo, 5, rowUpdate);

            //产品总尺寸
            addString(writableSheet, item.spec, 6, rowUpdate);

            //产品总尺寸
            addNumber(writableSheet, item.price, 7, rowUpdate);

             //材料比重
            addString(writableSheet, item.constitute.trim() ,27,rowUpdate);

            //读取咸康数据
            ProductDetail productDetail= apiManager.loadProductDetail(item.productId).datas.get(0);
            if(productDetail.product.xiankang!=null)
            {


                //同款货号
                addString(writableSheet, productDetail.product.xiankang.getQitahuohao().trim(), 3, rowUpdate);

                //甲醛标示
                addString(writableSheet, productDetail.product.xiankang.getJiaquan().trim(), 28, rowUpdate);



            }


            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //包装尺寸 cm
            addNumber(writableSheet, packValue[0], 25, rowUpdate);
            addNumber(writableSheet, packValue[1], 26, rowUpdate);
            addNumber(writableSheet, packValue[2], 27, rowUpdate);

            //折盒包装h inch
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[0]), 22, rowUpdate);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[1]), 23, rowUpdate);
            addNumber(writableSheet, UnitUtils.cmToInch(packValue[2]), 24, rowUpdate);



            //体积
            addNumber(writableSheet, item.volumeSize, 28, rowUpdate);


            //净重
            addNumber(writableSheet, item.weight, 29, rowUpdate);


            //备注
            addString(writableSheet, item.memo, 39, rowUpdate);

        }





    }
}
