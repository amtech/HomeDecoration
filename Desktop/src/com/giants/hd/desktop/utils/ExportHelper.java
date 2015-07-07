package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.CellFeatures;
import jxl.Image;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Alignment;
import jxl.write.Boolean;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.biff.RowsExceededException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 导出功能帮助类。
 */
public class ExportHelper {


    /**
     * 导出材料清单至指定路劲下。
     * @param detail
     * @param path
     */

    public static void export(ProductDetail detail,String path) throws IOException, WriteException {








        export(detail, path, 0);

        export(detail, path,1);
        export(detail, path,3);




















    }


    private static void export(ProductDetail detail,String path,int index) throws IOException, WriteException {


        String nameAppendix="";
        List<ProductMaterial>  datas = null;
        switch (index)
        {
            case 0:
                nameAppendix="白胚";
                datas=detail.conceptusMaterials;
                break;
            case 1:
                nameAppendix="组装";
                datas=detail.assembleMaterials;
            break;
            case 2:



                break;
            case 3:
                nameAppendix="包装";
                datas=detail.packMaterials;
                break;
        }

        String  fileName=path+ File.separator+detail.product.name+ (StringUtils.isEmpty(detail.product.pVersion)?"":("-"+detail.product.pVersion))+"_"+nameAppendix+".xls";
        WritableWorkbook wwb= Workbook.createWorkbook(new File(fileName));
        WritableSheet writableSheet= wwb.createSheet(detail.product.name, 0);


        writableSheet.setColumnView(0,50);
        writableSheet.setColumnView(1,20);

        writableSheet.setColumnView(2,40);
        writableSheet.setColumnView(3,20);
        //Create Cells with contents of different data types.
        //Also specify the Cell coordinates in the constructor
        Label label1 = new Label(0, 0, "子件代码");
        Label label2 = new Label(1, 0, "用量");
        Label label3 = new Label(02, 0, "特征");
        Label label4 = new Label(3, 0, "损耗");
//        DateTime date = new DateTime(1, 0, new Date());
//        Boolean bool = new Boolean(2, 0, true);
//        Number num = new Number(3, 0, 9.99);

        //Add the created Cells to the sheet
        writableSheet.addCell(label1);
        writableSheet.addCell(label2);
        writableSheet.addCell(label3);
        writableSheet.addCell(label4);

        if(datas==null) return;

        WritableCellFormat discountFormat = new WritableCellFormat(new NumberFormat("0.00"));
        WritableCellFormat quotaFormat = new WritableCellFormat(new NumberFormat("0.00000"));

        Number num;
        int startRow=1;

        //白胚
        for(ProductMaterial productMaterial:datas)
        {


            label1 = new Label(0, startRow, productMaterial.materialCode);
            writableSheet.addCell(label1);

            num = new Number(1, startRow,  productMaterial.quota ,quotaFormat);
            writableSheet.addCell(num);
            if(index==3)
            {
                label1 = new Label(2, startRow, (productMaterial.pLong>0?(productMaterial.pLong):"")+(productMaterial.pWidth>0?("*"+productMaterial.pWidth):"")+(productMaterial.pHeight>0?("*"+productMaterial.pHeight ):""));
                writableSheet.addCell(label1);
            }



            num = new Number(3, startRow, 1-productMaterial.available,discountFormat);
            writableSheet.addCell(num);


            startRow++;
        }


        //Write and close the workbook
        wwb.write();
        wwb.close();

    }


    /**
     * 导出excel 表格
     * @param quotationDetail
     * @param modelName
     * @param fileOutputDirectory
     * @throws IOException
     * @throws BiffException
     */
    public  static final void exportQuotation(QuotationDetail quotationDetail,String modelName,String fileOutputDirectory) throws IOException, BiffException, WriteException, HdException {

//        modelName="F://quotation/咸康(灯具)报价格式.xls";
//        fileOutputDirectory="F:\\quotation\\output\\";


        File outputFile=new File(fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber + ".xls");
        switch (modelName)
        {
            case QuotationFile.FILE_XIANGKANG_1:

                exportXiankangDengju(quotationDetail,new URL(HttpUrl.loadQuotationFile(modelName)).openStream(),outputFile);

                break;

            case QuotationFile.FILE_XIANGKANG_2:

                exportXiankangDengju(quotationDetail,new URL(HttpUrl.loadQuotationFile(modelName)).openStream(),outputFile);


                break;
        }


    }


    /**
     * 导出咸康灯具模板
     * @param quotationDetail
     * @param inputStream
     * @param output
     * @throws IOException
     * @throws BiffException
     */
    private static void exportXiankangDengju(QuotationDetail quotationDetail,InputStream inputStream,File output) throws IOException, BiffException, WriteException, HdException {

        Workbook existingWorkbook = Workbook.getWorkbook(inputStream);
        WritableWorkbook workbookCopy = Workbook.createWorkbook(output, existingWorkbook);
        WritableSheet writableSheet = workbookCopy.getSheet(0);

        int defaultRowCount=10;

        int startRow=4;


        int rowHeight = writableSheet.getRowHeight(startRow);



        int dataSize=quotationDetail.items.size();



        //实际数据超出范围 插入空行
        if(dataSize>defaultRowCount)
        {
            //插入空行
            for(int j=0;j<dataSize-defaultRowCount;j++) {

                int rowToInsert=startRow+defaultRowCount+j;
                writableSheet.insertRow(rowToInsert);
                writableSheet.setRowView(rowToInsert, rowHeight);
                //复制表格。
                for (int i = 0, count = writableSheet.getColumns(); i < count; i++) {
                    WritableCell cell = (WritableCell) writableSheet.getCell(i, startRow);
                    cell = cell.copyTo(i, rowToInsert);
                    writableSheet.addCell(cell);

                }
            }
        }




        //填充数据

        Label label1;
        Number num;
        WritableImage  image;

        int rowCount=  writableSheet.getRows();
        int columnCount=  writableSheet.getColumns();
        WritableCellFormat format=new WritableCellFormat();
        format.setAlignment(jxl.format.Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

        //报价日期
        //设计号  版本号
        label1 = new Label(7, 1, quotationDetail.quotation.qDate,format);
        writableSheet.addCell(label1);




        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片
            if(item.productPhoto!=null) {




                baos.reset();
                BufferedImage bufferedImage=ImageIO.read(new URL(HttpUrl.loadProductPicture(item.productName,item.pVersion)));
                ImageIO.write(bufferedImage, "PNG", baos);
                image = new WritableImage(4+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap, baos.toByteArray());
                image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
                writableSheet.addImage(image);
            }


            //读取咸康数据
            ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);



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







        //Write and close the workbook
        workbookCopy.write();
        workbookCopy.close();


    }

}
