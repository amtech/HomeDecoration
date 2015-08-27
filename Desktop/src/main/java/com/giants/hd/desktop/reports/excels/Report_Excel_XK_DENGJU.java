package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.write.*;

import java.io.IOException;

/** 报价导出咸康灯具模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_DENGJU extends ExcelReportor {


    public Report_Excel_XK_DENGJU(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {

        int defaultRowCount=10;

        int startRow=4;





        int dataSize=quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(writableSheet,startRow,defaultRowCount,dataSize);






        //填充数据

        Label label1;
        jxl.write.Number num;
        WritableImage  image;

        int rowCount=  writableSheet.getRows();
        int columnCount=  writableSheet.getColumns();
        WritableCellFormat format=new WritableCellFormat();
        format.setAlignment(jxl.format.Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format.setWrap(true);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        //报价日期
        //设计号  版本号
        label1 = new Label(7, 1, quotationDetail.quotation.qDate,format);
        writableSheet.addCell(label1);




        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;

        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片


                attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion),4+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);







            //行号
            //设计号  版本号
            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
            writableSheet.addCell(label1);

            //设计号  版本号
            label1 = new Label(1, rowUpdate, item.pVersion,format);
            writableSheet.addCell(label1);

            //货号
            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
            writableSheet.addCell(label1);

            //读取咸康数据
            ProductDetail productDetail= apiManager.loadProductDetail(item.productId).datas.get(0);



            //材料比重
            //货号
            label1 = new Label(8, rowUpdate,  productDetail.product.constitute);
            writableSheet.addCell(label1);

            if(productDetail.product.xiankang!=null)
            {


                //同款货号
                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
                writableSheet.addCell(label1);

                //甲醛标示
                label1 = new Label(9, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
                writableSheet.addCell(label1);

                //材质
                label1 = new Label(10, rowUpdate,  productDetail.product.xiankang.getCaizhi(),format);
                writableSheet.addCell(label1);
            }

        }





    }
}
