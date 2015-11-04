package com.giants.hd.desktop.reports.products;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.ProductDetail;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

/**
 * Created by david on 2015/10/7.
 */
public class Excel_ProductReport {



    /**
     * 产品格式1导出
     */
    public void reportProduct2(List<Product> products,String path) throws IOException, InvalidFormatException {


        URL url=   new URL(HttpUrl.loadQuotationFile("产品批量导出格式2", "xls"));




        Workbook      workbook = open(url);

        Sheet sheet= workbook.getSheetAt(0);




        int fisrtRow=1;

        int  size=products.size();

        for (int i=0;i<size;i++) {

            int rowUpdate = fisrtRow + i;
            Product product = products.get(i);

            setRowHeight(sheet,rowUpdate,1000);


            //货号
            setCellValue(sheet, product.name, 3, rowUpdate);
            //版本
            setCellValue(sheet, product.pVersion, 4, rowUpdate);
            setCellValue(sheet, "S/", 5, rowUpdate);
            //单位
            int unitSize = 1;
            try {
                unitSize = Integer.valueOf(product.pUnitName.substring(product.pUnitName.lastIndexOf("/")+1).trim());
            } catch (Throwable t) {
                t.printStackTrace();

            }
            setCellValue(sheet, unitSize, 6, rowUpdate);
            //fob
            setCellValue(sheet, product.fob, 7, rowUpdate);


            //装箱数
            setCellValue(sheet, product.insideBoxQuantity, 8, rowUpdate);
            setCellValue(sheet, "/", 9, rowUpdate);

            //外箱
            setCellValue(sheet, product.packQuantity, 10, rowUpdate);
            setCellValue(sheet, "/", 11, rowUpdate);


//
            setCellValue(sheet, product.packLong, 12, rowUpdate);
            setCellValue(sheet, "*", 13, rowUpdate);
            setCellValue(sheet, product.packWidth, 14, rowUpdate);
            setCellValue(sheet, "*", 15, rowUpdate);
            setCellValue(sheet, product.packHeight, 16, rowUpdate);

            setCellValue(sheet, product.packVolume, 17, rowUpdate);


            setCellValue(sheet, product.spec, 18, rowUpdate);

            setCellValue(sheet, product.weight, 19, rowUpdate);

            setCellValue(sheet, product.cost, 20, rowUpdate);

            setCellValue(sheet, product.price, 21, rowUpdate);

        }

        String fileName= DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime())+".xls";
        write(workbook,new File(path+File.separator+fileName));





    }

    /**
     * 产品格式2导出
     */
    public void reportProduct1(List<Product> products,String path) throws IOException, InvalidFormatException
    {

        URL url=   new URL(HttpUrl.loadQuotationFile("产品批量导出格式1", "xls"));

        Workbook      workbook =open(url);






        String fileName=path + File.separator + DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime())+".xls";

        write(workbook, new File( fileName));



       workbook= open(new File(fileName));




        Sheet sheet= workbook.getSheetAt(0);







        int fisrtRow=1;

        int  size=products.size();



        String basePath=new File("").getAbsolutePath();



        for (int i=0;i<size;i++) {

            int rowUpdate = fisrtRow + i;
            Product product = products.get(i);


            Row writeRow=    sheet.createRow(rowUpdate);

            setRowHeight( writeRow, 1000);


            //货号
            setCellValue(sheet, product.name, 1, writeRow);
            //版本
            setCellValue(sheet, product.pVersion, 2, writeRow);
            setCellValue(sheet, product.pUnitName, 3, writeRow);
            setCellValue(sheet, product.insideBoxQuantity+"/"+product.packQuantity+"/"+product.packLong+"*"+product.packWidth+"*"+product.packHeight, 4, writeRow);
            setCellValue(sheet, FloatHelper.scale(product.fob,2), 5, writeRow);

            setCellValue(sheet, FloatHelper.scale(product.cost,2), 6, writeRow);
            //白胚综合
            setCellValue(sheet, product.conceptusCost+product.conceptusWage, 7, writeRow);
            setCellValue(sheet, product.assembleCost+product.assembleWage, 8, writeRow);
            setCellValue(sheet, product.paintCost+product.paintWage, 9, writeRow);
            setCellValue(sheet, product.packCost+product.packWage, 10, writeRow);


            try{
                //缓存图片
          String file=  ImageLoader.getInstance().cacheFile(HttpUrl.loadProductPicture(product.url));
            //存放图片绝对路径
            setCellValue(sheet, basePath+ File.separator+file, 100, writeRow);
            }catch (Throwable t)
            {}


         //  boolean added=product.photo==null?false: addPicture(workbook,sheet,HttpUrl.loadProductPicture(product.name,product.pVersion),0,rowUpdate,1,rowUpdate+1,400);



            //  addPicture(workbook,drawing,anchor,product.photo,0,rowUpdate,1,rowUpdate+1);







        }


        write(workbook,new File(fileName));




//        callMacro(fileName,"插图");


    }


//    private void callMacro(String fileName, String macroName)
//    {
//        ActiveXComponent excel=new ActiveXComponent("Excel.Application");
//        excel.setProperty("Visible",new Variant(true));
//        Dispatch workbooks=excel.getProperty("WorkBooks").toDispatch();
//        Dispatch workbook =Dispatch.invoke(workbooks,"Open",Dispatch.Method,new Object[]{fileName},new int[1]).toDispatch();
//
//        //调用excel的宏命令
//        Dispatch.call(workbook,"run",new Variant(macroName));
//
//        Dispatch.call(workbook,"Save");
//        Dispatch.call(workbook,"Close",new Variant(true));
//
//
//    }


    /**
     * 导出材料清单
     */
    public void exportProductDetail(ProductDetail productDetail,String path) throws IOException {

        exportProductDetail(productDetail, path, 0);
        exportProductDetail(productDetail, path, 1);
        exportProductDetail(productDetail, path,3);

    }

    private   void exportProductDetail(ProductDetail detail,String path,int index) throws IOException {


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




        Workbook workbook=new HSSFWorkbook();


        Sheet writableSheet= workbook.createSheet("Sheet1" );



        setCellValue(writableSheet,"子件代码",0,0);
        setCellValue(writableSheet,"用量",1,0);
        setCellValue(writableSheet,"特征",2,0);
        setCellValue(writableSheet,"损耗",3,0);



        if(datas==null) return;




        int startRow=1;


        int size = datas.size();
        for (int i = 0; i < size; i++) {

            ProductMaterial material=datas.get(i);
            int updateRow=i+startRow;

            setCellValue(writableSheet,material.materialCode,0,updateRow);

            setCellValue(writableSheet, FloatHelper.scale(material.quota,3),1,updateRow);
            String value="";
            if(index==3)
            {
                 value= (material.pLong>0?(material.pLong):"")+(material.pWidth>0?("*"+material.pWidth):"")+(material.pHeight>0?("*"+material.pHeight ):"");

            }
            setCellValue(writableSheet,value,2,updateRow);

            setCellValue(writableSheet, FloatHelper.scale(1-material.available,3),3,updateRow);


        }


        String  fileName=path+ File.separator+detail.product.name+ (StringUtils.isEmpty(detail.product.pVersion)?"":("-"+detail.product.pVersion))+"_"+nameAppendix+".xls";

        write(workbook,new File(fileName));



    }




    private Workbook open(URL url) throws IOException {
        InputStream inputStream=url.openStream();

        Workbook      workbook = new HSSFWorkbook(    inputStream );
        inputStream.close();

        return  workbook;

    }


    private Workbook open(File file) throws IOException {
        InputStream inputStream=new FileInputStream(file);
        Workbook      workbook = new HSSFWorkbook(   inputStream );
        inputStream.close();

        return  workbook;

    }


    private void  write(Workbook workbook,File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();
        fos.flush();
        fos.close();
    }



    private void setCellValue(Sheet sheet,String value, int column ,int rowUpdate)
    {
        Row row=sheet.getRow(rowUpdate );
        if(row==null)
        {
            row=  sheet.createRow(rowUpdate);
        }
        Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);


    }


    private void setCellValue(Sheet sheet,String value, int column ,Row writeRow)
    {

        Cell cell = writeRow.getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);


    }

    private void setCellValue(Sheet sheet,double value, int column ,Row writeRow)
    {

        Cell cell = writeRow.getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);

    }


    private void setRowHeight(Sheet sheet,int rowUpdate, int height)
    {

        Row row=   sheet.getRow(rowUpdate);
        if(row==null)
            row=  sheet.createRow(rowUpdate);
        row.setHeight((short) height);
    }


    private void setRowHeight(Row writeRow, int height)
    {


        writeRow.setHeight((short) height);
    }

    private void setCellValue(Sheet sheet,double value, int column,int rowUpdate)
    {

        Row row=sheet.getRow(rowUpdate );
        if(row==null)
        {
            row=  sheet.createRow(rowUpdate);
        }

        Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);
    }






    private boolean addPicture(Workbook workbook,Sheet sheet,String url,int column, int row,int column2, int row2,int maxSize)  {




//        float columnWidth=sheet.getColumnWidthInPixels(column) ;
//        float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4 ;


        byte[] photo=null;


        try {
            photo=   ImageUtils.scale(new URL(url),maxSize, maxSize,true);
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    return   addPicture(workbook,sheet,photo,  column,   row,  column2,   row2);





    }



    private boolean addPicture(Workbook workbook,Sheet sheet,byte[] photo,int column, int row,int column2, int row2 )  {






        if(photo!=null ) {
            int pictureIdx = workbook.addPicture(photo, Workbook.PICTURE_TYPE_JPEG);

            Drawing drawing = sheet.createDrawingPatriarch();

            CreationHelper helper = workbook.getCreationHelper();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(column);
            anchor.setRow1(row);
            anchor.setCol2(column2);
            anchor.setRow2(row2);
            //  anchor.setDx1((int) (columnWidth / 10   ));
            //   anchor.setDx2((int) (rowHeight / 10    ));
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);


            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(1 );

          return true;
            //auto-size picture relative to its top-left corner

//            float columnWidth=sheet.getColumnWidthInPixels(column);
//            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;

//            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
        }else
        {
            return false;
        }



    }




}
