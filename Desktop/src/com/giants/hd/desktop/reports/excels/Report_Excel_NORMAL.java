package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.google.inject.Guice;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/** 通用报价模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_NORMAL extends ExcelReportor {


    public Report_Excel_NORMAL(String modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException {


        int defaultRowCount=7;
        int startItemRow=9;
        int rowHeight = writableSheet.getRowHeight(startItemRow);


        int dataSize=quotationDetail.items.size();
        //实际数据超出范围 插入空行
        if(dataSize>defaultRowCount)
        {
            //插入空行
            for(int j=0;j<dataSize-defaultRowCount;j++) {

                int rowToInsert=startItemRow+defaultRowCount+j;
                writableSheet.insertRow(rowToInsert);
                writableSheet.setRowView(rowToInsert, rowHeight);
                //复制表格。
                for (int i = 0, count = writableSheet.getColumns(); i < count; i++) {
                    WritableCell cell = (WritableCell) writableSheet.getCell(i, startItemRow);
                    cell = cell.copyTo(i, rowToInsert);
                    writableSheet.addCell(cell);

                }
            }
        }

        //格式
        WritableCellFormat format=new WritableCellFormat();
        format.setAlignment(jxl.format.Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format.setWrap(true);
        format.setBorder(Border.ALL, BorderLineStyle.NONE);

//格式
        WritableCellFormat BorderFormat=new WritableCellFormat();
        BorderFormat.setAlignment(jxl.format.Alignment.CENTRE);
        BorderFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        BorderFormat.setWrap(true);
        BorderFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

        Label label1;

        //表头
        //注入报价单号
        //报价日期

        label1 = new Label(2, 1, quotationDetail.quotation.qNumber,BorderFormat);
        writableSheet.addCell(label1);


        //报价日期

        label1 = new Label(14, 1, quotationDetail.quotation.qDate,BorderFormat);
        writableSheet.addCell(label1);





        //TO

        label1 = new Label(2, 7, quotationDetail.quotation.customerName,format);
        writableSheet.addCell(label1);

        //业务员代码

        label1 = new Label(11, 7, quotationDetail.quotation.salesman,format);
        writableSheet.addCell(label1);




        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableImage image;
        for (int i = 0; i <dataSize; i++) {



            int rowUpdate=startItemRow+i;
            QuotationItem item=quotationDetail.items.get(i);

            //图片
            if(item.productPhoto!=null) {
                baos.reset();
                BufferedImage bufferedImage= ImageIO.read(new URL(HttpUrl.loadProductPicture(item.productName, item.pVersion)));
                ImageIO.write(bufferedImage, "PNG", baos);
                image = new WritableImage(0+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap, baos.toByteArray());
                image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
                writableSheet.addImage(image);
            }


            //读取咸康数据
            //  ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);







            //货号
            label1 = new Label(2, rowUpdate, item.productName.trim()+"-"+item.pVersion.trim(),format);
            writableSheet.addCell(label1);





            //材料比重
            label1 = new Label(4, rowUpdate,  item.constitute,format);
            writableSheet.addCell(label1);


            //单位

            int lastIndex=item.unit.lastIndexOf("/");
            label1 = new Label(6, rowUpdate,  lastIndex==-1?"1":item.unit.substring(lastIndex+1),format);
            writableSheet.addCell(label1);

            //包装尺寸
            //内盒数
            label1 = new Label(8, rowUpdate,  String.valueOf(item.inBoxCount),format);
            writableSheet.addCell(label1);

            //包装数
            label1 = new Label(9, rowUpdate,  String.valueOf(item.packQuantity),format);
            writableSheet.addCell(label1);

            //解析出长宽高

            float[] result=     StringUtils.decouplePackageString(item.packageSize);


            //包装长
            label1 = new Label(11, rowUpdate,  String.valueOf(result[0]),format);
            writableSheet.addCell(label1);
            //包装宽
            label1 = new Label(13, rowUpdate,  String.valueOf(result[1]),format);
            writableSheet.addCell(label1);

            //包装高
            label1 = new Label(15, rowUpdate,  String.valueOf(result[2]),format);
            writableSheet.addCell(label1);


            //包装体积
            label1 = new Label(16, rowUpdate,  String.valueOf(item.volumeSize),format);
            writableSheet.addCell(label1);


            //产品规格

            label1 = new Label(17, rowUpdate,  String.valueOf(item.spec),format);
            writableSheet.addCell(label1);

            //镜面尺寸

            label1 = new Label(19, rowUpdate,  String.valueOf(item.mirrorSize),format);
            writableSheet.addCell(label1);

            //净重
            label1 = new Label(20, rowUpdate,  String.valueOf( item.weight),format);
            writableSheet.addCell(label1);

            //备注
            label1 = new Label(22, rowUpdate,  String.valueOf( item.memo),format);
            writableSheet.addCell(label1);

        }






    }
}
