package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.NumberFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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







        File outputFile=new File(fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber + ".xls");
        Workbook existingWorkbook = Workbook.getWorkbook(new URL(HttpUrl.loadQuotationFile(modelName)).openStream());
        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, existingWorkbook);
        WritableSheet writableSheet = workbookCopy.getSheet(0);

        switch (modelName)
        {
            case  QuotationFile.FILE_NORMAL:

                exportNormal(quotationDetail,writableSheet);
                break;


            case QuotationFile.FILE_XIANGKANG_1:

                exportXiankangDengju(quotationDetail, writableSheet);

                break;

            case QuotationFile.FILE_XIANGKANG_2:

                exportXiankangDengju(quotationDetail,writableSheet);

            case QuotationFile.FILE_XIANGKANG_3:

                exportXiankangDengju(quotationDetail,writableSheet);

                break;
        }


        workbookCopy.write();
        workbookCopy.close();


    }


    /**
     * 导出普通报价单
     * @param quotationDetail

     */
    private static void exportNormal(QuotationDetail quotationDetail,  WritableSheet writableSheet) throws WriteException, IOException, HdException {


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
                    BufferedImage bufferedImage=ImageIO.read(new URL(HttpUrl.loadProductPicture(item.productName,item.pVersion)));
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


    /**
     * 导出咸康灯具模板
     * @param quotationDetail

     * @throws IOException
     * @throws BiffException
     */
    private static void exportXiankangDengju(QuotationDetail quotationDetail, WritableSheet writableSheet) throws IOException, BiffException, WriteException, HdException {



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








    }

}
