package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.Number;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by davidleen29 on 2015/8/6.
 */
public   class ExcelReportor {


    protected QuotationFile file;
    WorkbookSettings s;
    public ExcelReportor(QuotationFile file)
    {this.file=file;
       s = new WorkbookSettings();
        s.setUseTemporaryFileDuringWrite(true);

    }
    public     void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException, BiffException, WriteException, HdException {



         s.setTemporaryFileDuringWriteDirectory(new File(fileOutputDirectory ));



        String outputFilePath=fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber +"."+ file.appendix;
        operation(quotationDetail, new URL(HttpUrl.loadQuotationFile(file.name, file.appendix)), outputFilePath);


    }



    protected  void operation(QuotationDetail quotationDetail,URL url,String outputFile) throws IOException, BiffException, WriteException, HdException {

        InputStream inputStream=url.openStream();
        Workbook existingWorkbook = Workbook.getWorkbook(inputStream);
        inputStream.close();
        WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(outputFile), existingWorkbook,s);
        workbookCopy.write();
        workbookCopy.close();

        doOnLocalFile(quotationDetail, new File(outputFile));

    }



    protected  void doOnLocalFile(QuotationDetail quotationDetail, File outputFile) throws IOException, BiffException, HdException, WriteException {



       Workbook inputWorkbook=  Workbook.getWorkbook(outputFile);
        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, inputWorkbook, s);
       inputWorkbook.close();



        writeOnExcel(quotationDetail, workbookCopy);

        workbookCopy.write();
        workbookCopy.close();




    }


    protected WritableWorkbook openWritableBook(File outFile) throws IOException, BiffException {



        Workbook inputWorkbook=  Workbook.getWorkbook(outFile);
        WritableWorkbook workbookCopy = Workbook.createWorkbook(outFile,inputWorkbook,s);
        inputWorkbook.close();
        inputWorkbook=null;
        return workbookCopy;

    }

    protected  void closeWritableBook(WritableWorkbook workbook ) throws IOException, WriteException {



        workbook.write();
        workbook.close();



    }




    protected void writeOnExcel(QuotationDetail detail, WritableSheet writableSheet, WritableWorkbook writableWorkbook) throws WriteException, IOException, HdException {



    }


    /**
     * this method can be override to skip the default operation on sheet[0].
     * @param quotationDetail
     * @param writableWorkbook
     * @throws WriteException
     * @throws IOException
     * @throws HdException
     */
    protected void writeOnExcel(QuotationDetail quotationDetail,WritableWorkbook  writableWorkbook) throws WriteException, IOException, HdException {

        WritableSheet writableSheet = writableWorkbook.getSheet(0);
        writeOnExcel(quotationDetail,writableSheet, writableWorkbook);
    }




    protected void attachPicture(WritableSheet sheet,String url,float x, float y, float width,float height) throws IOException {

        //图片

//        sheet.getColumnView((int) y).getSize();
//
//        int cellWidth=0;
//        int cellHeight=0;
//
//
//
//        //1 character (X) = 8 pixel (X)   1 character (Y) = 16 pixel (Y)
//        for(int i=Math.round(x - 0.5f);i<Math.round(x + width + 0.5);i++)
//        {
//            cellWidth+= sheet.getColumnView(i).getSize() *8/256;;
//        }
//
//        for(int i=Math.round(y - 0.5f);i<Math.round(y + height + 0.5);i++)
//        {
//            cellHeight+= sheet.getRowView(i).getSize() /20/3*4;
//        }

        byte[] data=null;
        try {
           // data=  ImageUtils.scale(new URL(url), cellWidth * 4, cellHeight * 4, true);
          //  data=  ImageUtils.scale(new URL(url), 640, 640, true);

            BufferedImage bufferedImage= ImageLoader.getInstance().loadImage(url );
            if(bufferedImage!=null)
            data=   ImageUtils.scale(bufferedImage,640, 640,true);

        } catch (HdException e) {
            e.printStackTrace();
        }
        WritableImage image;




        if(data!=null) {
            image = new WritableImage(x, y, width, height,data);
            image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
            sheet.addImage(image);

        }
    }



    protected void addString(WritableSheet writableSheet,String value,int x, int y    ) throws WriteException {



       Label  label1 = new Label(x, y, value,writableSheet.getWritableCell(x,y).getCellFormat());
       writableSheet.addCell(label1);
    }


    protected void addNumber(WritableSheet writableSheet,Number value,int x, int y    ) throws WriteException {



        jxl.write.Number number = new  jxl.write.Number(x, y, value.doubleValue(),writableSheet.getWritableCell(x,y).getCellFormat());
        writableSheet.addCell(number);
    }


    /**
     * 复制空行
     * @param writableSheet
     * @param startRow
     * @param defaultRowCount
     * @param dataSize
     */
    protected void  duplicateRow(WritableSheet writableSheet, int startRow,int defaultRowCount,int dataSize) throws WriteException {

        int rowHeight = writableSheet.getRowView(startRow).getSize();

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

    }


    /**
     * 复制空行
     * @param sheet
     * @param startRow
     * @param defaultRowCount
     * @param dataSize
     */
    protected void  duplicateRow(org.apache.poi.ss.usermodel.Workbook workbook,Sheet sheet, int startRow,int defaultRowCount,int dataSize) {




        //实际数据超出范围 插入空行
        if(dataSize>defaultRowCount)
        {
            //插入空行
            int rowNumToInsert=dataSize-defaultRowCount;

            if(startRow+defaultRowCount<sheet.getLastRowNum())
            sheet.shiftRows(startRow+defaultRowCount  , sheet.getLastRowNum(), rowNumToInsert ,true,true);

            Row rowForCopy=sheet.getRow(startRow);
            for(int j=0;j<rowNumToInsert;j++) {

                int rowToInsert=startRow+defaultRowCount+j ;

               Row row= sheet.createRow(rowToInsert);


                POIUtils.copyRow(workbook,rowForCopy,row,true);

            }
        }






    }



    /**
     * 产品规格转换成inch类型  并分段显示
     * @param spec
     * @return
     */
    public  String[] groupSpec(float[][] spec)
    {


        return groupSpec(spec, false);



    }

    /**
     * 产品规格转换成inch类型  并分段显示
     * @param spec
     * @return
     */
    public  String[] groupSpec(float[][] spec, boolean toInch)
    {



        int length=spec.length;
        String[] result=new String[ ]{"","",""};
        for (int i = 0; i < length; i++) {

            for(int j=0;j<3;j++) {
                result[j] +=toInch? UnitUtils.cmToInch(spec[i][j]):spec[i][j];

                if (i < length-1)
                    result[j] += StringUtils.row_separator;
            }

        }


        return result;

    }


    protected void addString(Sheet sheet,String value, int column ,int rowUpdate)
    {
        Cell cell = sheet.getRow(rowUpdate).getCell(column, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);

    }

    protected void addNumber(Sheet sheet,double value, int column,int rowUpdate)
    {
        Row row=sheet.getRow(rowUpdate);
        Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);

        cell.setCellValue(value);

    }



    protected void attachPicture(org.apache.poi.ss.usermodel.Workbook workbook,Sheet sheet,String url,int column, int row,int column2, int row2)  {




        float columnWidth=sheet.getColumnWidthInPixels(column) ;
        float rowHeight=sheet.getRow(row).getHeightInPoints()  ;


        byte[] photo=null;


        try {


            BufferedImage bufferedImage= ImageLoader.getInstance().loadImage(url );
            if(bufferedImage!=null)
                photo=   ImageUtils.scale(bufferedImage,(int)columnWidth*10, (int)rowHeight*10,true);
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(photo!=null ) {
            int pictureIdx = workbook.addPicture(photo, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_PNG);
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

            anchor.setDx1(50);
           anchor.setDy1(50);
            anchor.setDx2(-20);
            anchor.setDy2(-20);
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);


            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(1);

            //auto-size picture relative to its top-left corner

//            float columnWidth=sheet.getColumnWidthInPixels(column);
//            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;

//            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
        }



    }

}
