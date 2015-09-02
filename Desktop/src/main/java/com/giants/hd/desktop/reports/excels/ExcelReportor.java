package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Number;
import java.net.URL;

/**
 * Created by davidleen29 on 2015/8/6.
 */
public   class ExcelReportor {


    protected QuotationFile file;
    public ExcelReportor(QuotationFile file)
    {this.file=file;}
    public     void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException, BiffException, WriteException, HdException {






        String outputFilePath=fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber +"."+ file.appendix;


        operation(quotationDetail,new URL(HttpUrl.loadQuotationFile(file.name, file.appendix)),outputFilePath);


    }



    protected  void operation(QuotationDetail quotationDetail,URL url,String outputFile) throws IOException, BiffException, WriteException, HdException {

        InputStream inputStream=url.openStream();
        Workbook existingWorkbook = Workbook.getWorkbook(inputStream);
        inputStream.close();
        WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(outputFile), existingWorkbook);
        workbookCopy.write();
        workbookCopy.close();

        doOnLocalFile(quotationDetail, new File(outputFile));

    }



    protected  void doOnLocalFile(QuotationDetail quotationDetail, File outputFile) throws IOException, BiffException, HdException, WriteException {

        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, Workbook.getWorkbook(outputFile));
        writeOnExcel(quotationDetail, workbookCopy);
        workbookCopy.write();
        workbookCopy.close();




    }

    protected void writeOnExcel(QuotationDetail detail,WritableSheet  writableSheet) throws WriteException, IOException, HdException {


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
        writeOnExcel(quotationDetail,writableSheet);
    }




    protected void attachPicture(WritableSheet sheet,String url,float x, float y, float width,float height) throws IOException {
        //图片

        sheet.getColumnView((int) y).getSize();

        int cellWidth=0;
        int cellHeight=0;



        //1 character (X) = 8 pixel (X)   1 character (Y) = 16 pixel (Y)
        for(int i=Math.round(x - 0.5f);i<Math.round(x + width + 0.5);i++)
        {
            cellWidth+= sheet.getColumnView(i).getSize() *8/256;;
        }

        for(int i=Math.round(y - 0.5f);i<Math.round(y + height + 0.5);i++)
        {
            cellHeight+= sheet.getRowView(i).getSize() /20/3*4;
        }

        byte[] data=null;
        try {
           // data=  ImageUtils.scale(new URL(url), cellWidth * 4, cellHeight * 4, true);
            data=  ImageUtils.scale(new URL(url), 1280, 1280, true);
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
     * 产品规格转换成inch类型  并分段显示
     * @param spec
     * @return
     */
    public  String[] cmToInchSpec(float[][] spec)
    {



        int length=spec.length;
        String[] result=new String[ ]{"","",""};
        for (int i = 0; i < length; i++) {

            for(int j=0;j<3;j++) {
                result[j] += UnitUtils.cmToInch(spec[i][j]);

                if (i < length-1)
                    result[j] += StringUtils.row_separator;
            }

        }


        return result;

    }


}
