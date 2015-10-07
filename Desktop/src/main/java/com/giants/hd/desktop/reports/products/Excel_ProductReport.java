package com.giants.hd.desktop.reports.products;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.ProductDetail;
import jxl.write.WriteException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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



        Sheet asheet;
        int fisrtRow=1;

        int  size=products.size();

        for (int i=0;i<size;i++) {

            int rowUpdate = fisrtRow + i;
            Product product = products.get(i);
            sheet.getRow(rowUpdate).setHeight((short) 800);


            //货号
            setCellValue(sheet, product.name, 3, rowUpdate);
            //版本
            setCellValue(sheet, product.pVersion, 4, rowUpdate);
            setCellValue(sheet, "S/", 5, rowUpdate);
            //单位
            int unitSize = 1;
            try {
                unitSize = Integer.valueOf(product.pUnitName.substring(product.pUnitName.lastIndexOf("/")));
            } catch (Throwable t) {

            }
            setCellValue(sheet, unitSize, 6, rowUpdate);
            //fob
            setCellValue(sheet, product.fob, 7, rowUpdate);

            //外箱
            setCellValue(sheet, product.packQuantity, 8, rowUpdate);
            setCellValue(sheet, "/", 9, rowUpdate);
            //装箱数
            setCellValue(sheet, product.insideBoxQuantity, 10, rowUpdate);
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

        Workbook      workbook = open(url);

        Sheet sheet= workbook.getSheetAt(0);

        int fisrtRow=1;

        int  size=products.size();

        for (int i=0;i<size;i++) {

            int rowUpdate = fisrtRow + i;
            Product product = products.get(i);


         Row row=   sheet.getRow(rowUpdate);
            if(row==null)
                row=  sheet.createRow(rowUpdate);
            row.setHeight((short) 1000);


            //货号
            setCellValue(sheet, product.name, 1, rowUpdate);
            //版本
            setCellValue(sheet, product.pVersion, 2, rowUpdate);
            setCellValue(sheet, product.pUnitName, 3, rowUpdate);
            setCellValue(sheet, product.insideBoxQuantity+"/"+product.packQuantity+"/"+product.packLong+"*"+product.packWidth+"*"+product.packHeight, 4, rowUpdate);

            setCellValue(sheet, product.cost, 5, rowUpdate);
            //白胚综合
            setCellValue(sheet, product.conceptusCost+product.conceptusWage, 6, rowUpdate);
            setCellValue(sheet, product.assembleCost+product.assembleWage, 7, rowUpdate);
            setCellValue(sheet, product.paintCost+product.paintWage, 8, rowUpdate);
            setCellValue(sheet, product.packCost+product.packWage, 9, rowUpdate);


            addPicture(workbook,sheet,HttpUrl.loadProductPicture(product.name,product.pVersion),0,rowUpdate,1,rowUpdate+1);

        }




        String fileName= DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime())+".xls";
        write(workbook,new File(path+File.separator+fileName));

    }


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


        Sheet writableSheet= workbook.createSheet(detail.product.name );



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

        Workbook      workbook = new HSSFWorkbook(inputStream );
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




    private void addPicture(Workbook workbook,Sheet sheet,String url,int column, int row,int column2, int row2)  {




        float columnWidth=sheet.getColumnWidthInPixels(column) ;
        float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4 ;


        byte[] photo=null;


        try {
            photo=   ImageUtils.scale(new URL(url),1280, 1280,true);
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        if(photo!=null ) {
            int pictureIdx = workbook.addPicture(photo, Workbook.PICTURE_TYPE_PNG);
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
            pict.resize(0.9);

            //auto-size picture relative to its top-left corner

//            float columnWidth=sheet.getColumnWidthInPixels(column);
//            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;

//            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
        }



    }
}
