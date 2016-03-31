package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import org.apache.poi.ss.usermodel.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by davidleen29 on 2015/8/6.
 */
public   class ExcelReportor {



    //HSSF 中excel表格 插图片  dx1 dx2  是基于宽以1024为单位，高以256 单位
    private static final int WIDTH_PARAM=1024;
    private static final int HEIGHT_PARAM=256;

    private static final int DEFAULT_PIXEL_A_POINT =1;


    protected QuotationFile file;

    public ExcelReportor(QuotationFile file)
    {this.file=file;

    }
    public     void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException,   HdException {







        String outputFilePath=fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber +"."+ file.appendix;
        operation(quotationDetail, new URL(HttpUrl.loadQuotationFile(file.name, file.appendix)), outputFilePath);


    }



    protected  void operation(QuotationDetail quotationDetail,URL url,String outputFile) throws IOException,   HdException {

//        InputStream inputStream=url.openStream();
//        Workbook existingWorkbook = Workbook.getWorkbook(inputStream);
//        inputStream.close();
//        WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(outputFile), existingWorkbook,s);
//        workbookCopy.write();
//        workbookCopy.close();
//
//        doOnLocalFile(quotationDetail, new File(outputFile));

    }



//    protected  void doOnLocalFile(QuotationDetail quotationDetail, File outputFile) throws IOException, BiffException, HdException, WriteException {
//
//
//
//       Workbook inputWorkbook=  Workbook.getWorkbook(outputFile);
//        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, inputWorkbook, s);
//       inputWorkbook.close();
//
//
//
//        writeOnExcel(quotationDetail, workbookCopy);
//
//        workbookCopy.write();
//        workbookCopy.close();
//
//
//
//
//    }


//    protected WritableWorkbook openWritableBook(File outFile) throws IOException, BiffException {
//
//
//
//        Workbook inputWorkbook=  Workbook.getWorkbook(outFile);
//        WritableWorkbook workbookCopy = Workbook.createWorkbook(outFile,inputWorkbook,s);
//        inputWorkbook.close();
//        inputWorkbook=null;
//        return workbookCopy;
//
//    }

//    protected  void closeWritableBook(WritableWorkbook workbook ) throws IOException, WriteException {
//
//
//
//        workbook.write();
//        workbook.close();
//
//
//
//    }




//    protected void writeOnExcel(QuotationDetail detail, WritableSheet writableSheet, WritableWorkbook writableWorkbook) throws WriteException, IOException, HdException {
//
//
//
//    }

//
//    /**
//     * this method can be override to skip the default operation on sheet[0].
//     * @param quotationDetail
//     * @param writableWorkbook
//     * @throws WriteException
//     * @throws IOException
//     * @throws HdException
//     */
//    protected void writeOnExcel(QuotationDetail quotationDetail,WritableWorkbook  writableWorkbook) throws WriteException, IOException, HdException {
//
//        WritableSheet writableSheet = writableWorkbook.getSheet(0);
//        writeOnExcel(quotationDetail,writableSheet, writableWorkbook);
//    }




//    protected void attachPicture(WritableSheet sheet,String url,float x, float y, float width,float height) throws IOException {
//
//        //图片
//
////        sheet.getColumnView((int) y).getSize();
////
////        int cellWidth=0;
////        int cellHeight=0;
////
////
////
////        //1 character (X) = 8 pixel (X)   1 character (Y) = 16 pixel (Y)
////        for(int i=Math.round(x - 0.5f);i<Math.round(x + width + 0.5);i++)
////        {
////            cellWidth+= sheet.getColumnView(i).getSize() *8/256;;
////        }
////
////        for(int i=Math.round(y - 0.5f);i<Math.round(y + height + 0.5);i++)
////        {
////            cellHeight+= sheet.getRowView(i).getSize() /20/3*4;
////        }
//
//        byte[] data=null;
//        try {
//           // data=  ImageUtils.scale(new URL(url), cellWidth * 4, cellHeight * 4, true);
//          //  data=  ImageUtils.scale(new URL(url), 640, 640, true);
//
//            BufferedImage bufferedImage= ImageLoader.getInstance().loadImage(url );
//            if(bufferedImage!=null)
//            data=   ImageUtils.scale(bufferedImage,640, 640,true);
//
//        } catch (HdException e) {
//            e.printStackTrace();
//        }
//        WritableImage image;
//
//
//
//
//        if(data!=null) {
//            image = new WritableImage(x, y, width, height,data);
//            image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
//            sheet.addImage(image);
//
//        }
//    }
//
//
//
//    protected void addString(WritableSheet writableSheet,String value,int x, int y    ) throws WriteException {
//
//
//
//       Label  label1 = new Label(x, y, value,writableSheet.getWritableCell(x,y).getCellFormat());
//       writableSheet.addCell(label1);
//    }
//
//
//    protected void addNumber(WritableSheet writableSheet,Number value,int x, int y    ) throws WriteException {
//
//
//
//        jxl.write.Number number = new  jxl.write.Number(x, y, value.doubleValue(),writableSheet.getWritableCell(x,y).getCellFormat());
//        writableSheet.addCell(number);
//    }
//
//
//    /**
//     * 复制空行
//     * @param writableSheet
//     * @param startRow
//     * @param defaultRowCount
//     * @param dataSize
//     */
//    protected void  duplicateRow(WritableSheet writableSheet, int startRow,int defaultRowCount,int dataSize) throws WriteException {
//
//        int rowHeight = writableSheet.getRowView(startRow).getSize();
//
//        //实际数据超出范围 插入空行
//        if(dataSize>defaultRowCount)
//        {
//            //插入空行
//            for(int j=0;j<dataSize-defaultRowCount;j++) {
//
//                int rowToInsert=startRow+defaultRowCount+j;
//                writableSheet.insertRow(rowToInsert);
//                writableSheet.setRowView(rowToInsert, rowHeight);
//                //复制表格。
//                for (int i = 0, count = writableSheet.getColumns(); i < count; i++) {
//                    WritableCell cell = (WritableCell) writableSheet.getCell(i, startRow);
//                    cell = cell.copyTo(i, rowToInsert);
//                    writableSheet.addCell(cell);
//
//                }
//            }
//        }
//
//    }


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


                POIUtils.copyRow(workbook,rowForCopy,row,true,true);

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





        int columnWidth=0;

        for(int i=column;i<=column2;i++)
        {
            columnWidth+=  sheet.getColumnWidthInPixels(column);
        }

        int rowHeight=0;
        for(int i=row;i<=row2;i++)
        {
            rowHeight+= sheet.getRow(row).getHeightInPoints() * DEFAULT_PIXEL_A_POINT ;
        }



        float  padding=0;





        int maxPictureWidth=columnWidth*10;
        int maxPictureHeight=rowHeight*10;


        //计算图片大小






        //图片右上角在第一个cell的宽的百分比
        float fractionX1=0;




        byte[] photo=null;
        //计算在cell表格中的占位参数
        int destCol1=-1;
        int destRow1=-1;
        int destCol2=-1;
        int destRow2=-1;
        int destDx1=-1;
        int destDy1=-1;
        int destDx2=-1;
        int destDy2=-1;

        //图片比例
        float pictureRatio;
        try {


            BufferedImage bufferedImage= ImageLoader.getInstance().loadImage(url );

            if(bufferedImage!=null)
            {



                float destWidth=bufferedImage.getWidth();
                float destHeight=bufferedImage.getHeight();




                float  destCellWidth;
                float  destCellHeight;
                if(columnWidth>=destWidth&&rowHeight>=destHeight)
                {
                    destCellWidth=destWidth;
                    destCellHeight=destHeight;
                }
                else
                {
                    pictureRatio=destWidth/destHeight;
                   destCellWidth=    Math.min(columnWidth,pictureRatio* rowHeight ) ;
                   destCellHeight=destCellWidth/pictureRatio;
                }
                float left=(columnWidth-destCellWidth)/2;
                float top=(rowHeight-destCellHeight)/2;
                float right=left+destCellWidth;
                float bottom=top+destCellHeight;




                float widthPixel=padding;
                for(int columnIndex=column;columnIndex<=column2;columnIndex++)
                {

                   float thisColumnPixel= sheet.getColumnWidthInPixels(column);
                    float stepPixel=widthPixel+thisColumnPixel;
                     if(stepPixel>=left&&destCol1<0)
                    {
                        destCol1=columnIndex;
                        destDx1= (int) ((left-widthPixel+padding)/thisColumnPixel* WIDTH_PARAM);
                    }


                    if(stepPixel>=right&&destCol2<0)
                    {
                        destCol2=columnIndex;
                        destDx2= (int) ((right-widthPixel+padding)/thisColumnPixel*WIDTH_PARAM);
                    }



                    widthPixel=stepPixel;
                }


                float heightPixel=padding;
                for(int rowIndex=row;rowIndex<=row2;rowIndex++)
                {

                    float thisRowHeight= sheet.getRow(rowIndex).getHeightInPoints()* DEFAULT_PIXEL_A_POINT;
                    float stepPixel=heightPixel+thisRowHeight;
                     if(stepPixel>=top&&destRow1<0)
                    {
                        destRow1=rowIndex;
                        destDy1= (int) ((top-heightPixel+padding)/thisRowHeight* HEIGHT_PARAM);
                    }


                    if(stepPixel>=bottom&&destRow2<0)
                    {
                        destRow2=rowIndex;
                        destDy2= (int) ((bottom-heightPixel+padding)/thisRowHeight*HEIGHT_PARAM);
                    }



                    heightPixel=stepPixel;
                }











                photo = ImageUtils.scale(bufferedImage, maxPictureWidth, maxPictureHeight, true);







            }
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




            anchor.setCol1(destCol1);
            anchor.setRow1(destRow1);

            anchor.setCol2(destCol2);
            anchor.setRow2(destRow2);

            anchor.setDx1(destDx1);
           anchor.setDy1(destDy1);
            anchor.setDx2(destDx2);
            anchor.setDy2(destDy2);
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);


            Picture pict = drawing.createPicture(anchor, pictureIdx);
           // pict.resize(1);

            //auto-size picture relative to its top-left corner

//            float columnWidth=sheet.getColumnWidthInPixels(column);
//            float rowHeight=sheet.getRow(row).getHeightInPoints()/3*4;

//            pict.resize( columnWidth/pictureWidth,(float)rowHeight/pictureHeight);
        }



    }

}
