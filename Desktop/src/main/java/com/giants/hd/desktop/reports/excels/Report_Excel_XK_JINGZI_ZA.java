package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.write.*;

import java.io.IOException;

/**咸康 画杂  镜子类模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_JINGZI_ZA extends ExcelReportor {


    public Report_Excel_XK_JINGZI_ZA(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {






        int defaultRowCount=10;
        int startRow=5;
        int dataSize=quotationDetail.XKItems.size();
        //实际数据超出范围 插入空行
        duplicateRow(writableSheet,startRow,defaultRowCount,dataSize);







        //填充数据

        Label label1;
        jxl.write.Number num;
        WritableImage image;

        int rowCount=  writableSheet.getRows();
        int columnCount=  writableSheet.getColumns();
        WritableCellFormat format=new WritableCellFormat();
        format.setAlignment(jxl.format.Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format.setWrap(true);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        //报价日期
        label1 = new Label(5, 1, quotationDetail.quotation.qDate,format);
        writableSheet.addCell(label1);



                //报价日期
                label1 = new Label(14, 1, "Verdoer YUNFEI",format);
        writableSheet.addCell(label1);



        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;

        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationXKItem item=quotationDetail.XKItems.get(i);



            //图片
            attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion),4+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);





            //读取咸康数据
            ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);

            //读取咸康数据
            ProductDetail productDetail2=    apiManager.loadProductDetail(item.productId2).datas.get(0);

            //行号
            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
            writableSheet.addCell(label1);

            //设计号  版本号
            label1 = new Label(1, rowUpdate, item.pVersion,format);
            writableSheet.addCell(label1);

            //货号
            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
            writableSheet.addCell(label1);







            //单位
            label1 = new Label(8, rowUpdate,  item.unit ,format);
            writableSheet.addCell(label1);

            if(productDetail.product.xiankang!=null)
            {


                //同款货号
                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
                writableSheet.addCell(label1);
                //材料比重

                label1 = new Label(6, rowUpdate,  productDetail.product.xiankang.getCaizhibaifenbi(),format);
                writableSheet.addCell(label1);
                //甲醛标示
                label1 = new Label(7, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
                writableSheet.addCell(label1);

                //边框
                label1 = new Label(12, rowUpdate,  productDetail.product.xiankang.getBiankuang(),format);
                writableSheet.addCell(label1);


                //槽宽

                label1 = new Label(13, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
                writableSheet.addCell(label1);

                //槽深
                label1 = new Label(14, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
                writableSheet.addCell(label1);

                //镜子尺寸	宽
                label1 = new Label(15, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

                //镜子尺寸	高
                label1 = new Label(16, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);


                //磨边
                label1 = new Label(17, rowUpdate,  productDetail.product.xiankang.getMobian(),format);
                writableSheet.addCell(label1);
            }



            //折盒价格
            label1 = new Label(10, rowUpdate, String.valueOf(item.price),format);
            writableSheet.addCell(label1);

            //加强价格
            label1 = new Label(11, rowUpdate,  String.valueOf(item.price2),format);
            writableSheet.addCell(label1);





            //总长
            label1 = new Label(18, rowUpdate, String.valueOf(item.spec),format);
            writableSheet.addCell(label1);


            //总宽
            label1 = new Label(19, rowUpdate, String.valueOf(item.spec),format);
            writableSheet.addCell(label1);

            //总深
            label1 = new Label(20, rowUpdate,  String.valueOf(item.spec) ,format);
            writableSheet.addCell(label1);


            //重量
            label1 = new Label(21, rowUpdate,  String.valueOf(item.weight) ,format);
            writableSheet.addCell(label1);



            //折盒包装描述
            label1 = new Label(22, rowUpdate,  String.valueOf(productDetail.product.xiankang.pack_memo) ,format);
            writableSheet.addCell(label1);

            //几个装
            label1 = new Label(28, rowUpdate,  String.valueOf(item.packQuantity) ,format);
            writableSheet.addCell(label1);

            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //折盒包装l
            label1 = new Label(29, rowUpdate,  String.valueOf(packValue[0]) ,format);
            writableSheet.addCell(label1);
            //折盒包装w
            label1 = new Label(30, rowUpdate,  String.valueOf(packValue[1]) ,format);
            writableSheet.addCell(label1);

            //折盒包装h
            label1 = new Label(31, rowUpdate,  String.valueOf(packValue[2]) ,format);
            writableSheet.addCell(label1);


            //折盒包装l cm
            label1 = new Label(33, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[0])) ,format);
            writableSheet.addCell(label1);
            //折盒包装w cm
            label1 = new Label(34, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[1])) ,format);
            writableSheet.addCell(label1);

            //折盒包装h cm
            label1 = new Label(35, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[2])) ,format);
            writableSheet.addCell(label1);





            //加强包装描述
            label1 = new Label(37, rowUpdate,  String.valueOf(productDetail2.product.xiankang.pack_memo) ,format);
            writableSheet.addCell(label1);

            //几个装
            label1 = new Label(38, rowUpdate,  String.valueOf(item.packQuantity2) ,format);
            writableSheet.addCell(label1);

            float[] packValue2=  StringUtils.decouplePackageString(item.packageSize2);

            //加强包装包装l
            label1 = new Label(39, rowUpdate,  String.valueOf(packValue2[0]) ,format);
            writableSheet.addCell(label1);
            //加强包装w
            label1 = new Label(40, rowUpdate,  String.valueOf(packValue2[1]) ,format);
            writableSheet.addCell(label1);

            //加强包装h
            label1 = new Label(41, rowUpdate,  String.valueOf(packValue2[2]) ,format);
            writableSheet.addCell(label1);


            //加强包装l cm
            label1 = new Label(45, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[0])) ,format);
            writableSheet.addCell(label1);
            //加强包装w cm
            label1 = new Label(46, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[1])) ,format);
            writableSheet.addCell(label1);

            //加强包装h cm
            label1 = new Label(47, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[2])) ,format);
            writableSheet.addCell(label1);



            //挂钩距离
            label1 = new Label(49, rowUpdate,  String.valueOf(productDetail.product.xiankang.getGuaju()) ,format);
            writableSheet.addCell(label1);
            //挂钩距离
            label1 = new Label(57, rowUpdate,  String.valueOf(productDetail.product.memo) ,format);
            writableSheet.addCell(label1);


        }





    }
}
