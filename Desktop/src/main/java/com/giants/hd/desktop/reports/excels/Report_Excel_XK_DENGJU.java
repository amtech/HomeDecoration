package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.write.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/** 报价导出咸康灯具模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_DENGJU extends ExcelReportor {

    Workbook workbook;

    public Report_Excel_XK_DENGJU(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {




        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream=url.openStream();
        workbook = new HSSFWorkbook(inputStream);




        writeOnExcel(workbook.getSheetAt(0), quotationDetail);


        FileOutputStream fos=    new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();






    }

    protected void writeOnExcel(Sheet writableSheet,QuotationDetail quotationDetail) throws IOException, HdException {
        int defaultRowCount=10;

        int startRow=4;





        int dataSize=quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(workbook,writableSheet,startRow,defaultRowCount,dataSize);






        //填充数据

        Label label1;
        jxl.write.Number num;
        WritableImage  image;


        //报价日期
        //设计号  版本号
//        label1 = new Label(7, 1, quotationDetail.quotation.qDate,format);
//        writableSheet.addCell(label1);
        addString(writableSheet,quotationDetail.quotation.qDate,7,1);




        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;

        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片


                attachPicture(workbook,writableSheet, HttpUrl.loadProductPicture(item.photoUrl),4 , rowUpdate ,5, rowUpdate);







            //行号
            //设计号  版本号
//            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
//            writableSheet.addCell(label1);

            addNumber(writableSheet, i + 1, 0, rowUpdate);


            //设计号  版本号
//            label1 = new Label(1, rowUpdate, item.pVersion,format);
//            writableSheet.addCell(label1);

            addString(writableSheet, item.pVersion, 1, rowUpdate);

            //货号
//            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
//            writableSheet.addCell(label1);
            addString(writableSheet,item.productName,2,rowUpdate);
            //读取咸康数据
            ProductDetail productDetail= apiManager.loadProductDetail(item.productId).datas.get(0);



            //材料比重
            //货号
//            label1 = new Label(8, rowUpdate,  productDetail.product.constitute);
//            writableSheet.addCell(label1);
            addString(writableSheet,productDetail.product.constitute,8,rowUpdate);

            if(productDetail.product.xiankang!=null)
            {


                //同款货号
//                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
//                writableSheet.addCell(label1);

                addString(writableSheet,productDetail.product.xiankang.getQitahuohao(),3,rowUpdate);

                //甲醛标示
//                label1 = new Label(9, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
//                writableSheet.addCell(label1);

                addString(writableSheet,productDetail.product.xiankang.getJiaquan(),9,rowUpdate);

                //材质
//                label1 = new Label(10, rowUpdate,  productDetail.product.xiankang.getCaizhi(),format);
//                writableSheet.addCell(label1);
                addString(writableSheet,productDetail.product.xiankang.getCaizhi(),10,rowUpdate);
            }

        }





    }
}
