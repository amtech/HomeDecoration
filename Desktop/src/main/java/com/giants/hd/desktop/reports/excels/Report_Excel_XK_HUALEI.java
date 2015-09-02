package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationXKItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.write.*;
import jxl.write.Number;

import java.io.IOException;

/**咸康 画类  类模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_HUALEI extends ExcelReportor {


    public Report_Excel_XK_HUALEI(QuotationFile modelName) {
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

                attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion), 4 + pictureGap / 2, rowUpdate + pictureGap / 2, 1 - pictureGap, 1 - pictureGap);


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
            label1 = new Label(7, rowUpdate,  item.unit ,format);
            writableSheet.addCell(label1);

            if(productDetail.product.xiankang!=null)
            {


                //同款货号
                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
                writableSheet.addCell(label1);
                //材料比重

                label1 = new Label(5, rowUpdate,  productDetail.product.xiankang.getCaizhibaifenbi(),format);
                writableSheet.addCell(label1);
                //甲醛标示
                label1 = new Label(6, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
                writableSheet.addCell(label1);

                //边框
                label1 = new Label(11, rowUpdate,  productDetail.product.xiankang.getBiankuang(),format);
                writableSheet.addCell(label1);


                //槽宽

                label1 = new Label(12, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
                writableSheet.addCell(label1);

                //槽深
                label1 = new Label(13, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
                writableSheet.addCell(label1);

                //画尺寸	宽
                label1 = new Label(16, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

                //画尺寸	高
                label1 = new Label(17, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

                //玻璃尺寸	宽
                label1 = new Label(18, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

                //玻璃尺寸	高
                label1 = new Label(19, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);


                //正面开口尺寸	宽
                label1 = new Label(20, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

                //正面开口尺寸	高
                label1 = new Label(21, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

//                //磨边
//                label1 = new Label(17, rowUpdate,  productDetail.product.xiankang.getMobian(),format);
//                writableSheet.addCell(label1);
            }



            //折盒价格
            num = new Number(8, rowUpdate,  item.price,format);
            writableSheet.addCell(num);

            //加强价格
            num = new Number(9, rowUpdate,  item.price2 ,format);
            writableSheet.addCell(num);



            float[] specValue=  StringUtils.decouplePackageString(productDetail.product.spec);

            //总长
            label1 = new Label(22, rowUpdate, String.valueOf(specValue[0]),format);
            writableSheet.addCell(label1);


            //总宽
            label1 = new Label(23, rowUpdate, String.valueOf(specValue[1]),format);
            writableSheet.addCell(label1);

            //总深
            label1 = new Label(24, rowUpdate,  String.valueOf(specValue[2]) ,format);
            writableSheet.addCell(label1);


            //重量
            label1 = new Label(25, rowUpdate,  String.valueOf(item.weight) ,format);
            writableSheet.addCell(label1);



            //折盒包装描述
            label1 = new Label(26, rowUpdate,  String.valueOf(productDetail.product.xiankang.pack_memo) ,format);
            writableSheet.addCell(label1);




            //几个装
            label1 = new Label(32, rowUpdate,  String.valueOf(item.packQuantity) ,format);
            writableSheet.addCell(label1);

            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //折盒包装l
            label1 = new Label(33, rowUpdate,  String.valueOf(packValue[0]) ,format);
            writableSheet.addCell(label1);
            //折盒包装w
            label1 = new Label(34, rowUpdate,  String.valueOf(packValue[1]) ,format);
            writableSheet.addCell(label1);

            //折盒包装h
            label1 = new Label(35, rowUpdate,  String.valueOf(packValue[2]) ,format);
            writableSheet.addCell(label1);


            //折盒包装l cm
            label1 = new Label(37, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[0])) ,format);
            writableSheet.addCell(label1);
            //折盒包装w cm
            label1 = new Label(38, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[1])) ,format);
            writableSheet.addCell(label1);

            //折盒包装h cm
            label1 = new Label(39, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue[2])) ,format);
            writableSheet.addCell(label1);

           // PF Pack G.W. (KGS)
            label1 = new Label(40, rowUpdate, "" ,format);
            writableSheet.addCell(label1);



                //加强包装描述
            label1 = new Label(41, rowUpdate,  String.valueOf(productDetail2.product.xiankang.pack_memo) ,format);
            writableSheet.addCell(label1);

            //几个装
            label1 = new Label(42, rowUpdate,  String.valueOf(item.packQuantity2) ,format);
            writableSheet.addCell(label1);

            float[] packValue2=  StringUtils.decouplePackageString(item.packageSize2);

            //加强包装包装l
            label1 = new Label(43, rowUpdate,  String.valueOf(packValue2[0]) ,format);
            writableSheet.addCell(label1);
            //加强包装w
            label1 = new Label(44, rowUpdate,  String.valueOf(packValue2[1]) ,format);
            writableSheet.addCell(label1);

            //加强包装h
            label1 = new Label(45, rowUpdate,  String.valueOf(packValue2[2]) ,format);
            writableSheet.addCell(label1);


            //加强包装l cm
            label1 = new Label(49, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[0])) ,format);
            writableSheet.addCell(label1);
            //加强包装w cm
            label1 = new Label(50, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[1])) ,format);
            writableSheet.addCell(label1);

            //加强包装h cm
            label1 = new Label(51, rowUpdate,  String.valueOf(UnitUtils.cmToInch(packValue2[2])) ,format);
            writableSheet.addCell(label1);

            //Reshipper Pack N.W. (KGS)

            label1 = new Label(52, rowUpdate, "" ,format);
            writableSheet.addCell(label1);




            //挂钩距离
            label1 = new Label(53, rowUpdate,  String.valueOf(productDetail.product.xiankang.getGuaju()) ,format);
            writableSheet.addCell(label1);
            //画芯号
            label1 = new Label(57, rowUpdate,  String.valueOf(productDetail.product.xiankang.getHuaxinbianhao()) ,format);
            writableSheet.addCell(label1);
                      //画芯厂商
            label1 = new Label(58, rowUpdate,  String.valueOf(productDetail.product.xiankang.getHuaxinchangshang()) ,format);
            writableSheet.addCell(label1);
            //画芯效果
            label1 = new Label(59, rowUpdate,  String.valueOf(productDetail.product.xiankang.getHuaxinxiaoguo()) ,format);
            writableSheet.addCell(label1);


            //备注
            label1 = new Label(64, rowUpdate,  String.valueOf(item.memo) ,format);
            writableSheet.addCell(label1);

        }





    }
}
