package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.exception.HdException;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Number;
import java.net.URL;

/**
 * Created by davidleen29 on 2015/8/6.
 */
public   class ExcelReportor {


    private QuotationFile file;
    public ExcelReportor(QuotationFile file)
    {this.file=file;}
    public  final void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException, BiffException, WriteException, HdException {






        File outputFile=new File(fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber +"."+ file.appendix );


        InputStream inputStream=new URL(HttpUrl.loadQuotationFile(file.name,file.appendix)).openStream();
        Workbook existingWorkbook = Workbook.getWorkbook(inputStream);
        inputStream.close();
        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, existingWorkbook);
        workbookCopy.write();
        workbookCopy.close();

         doOnLocalFile(quotationDetail, new File(fileOutputDirectory + File.separator + quotationDetail.quotation.qNumber + "." + file.appendix));
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



    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected void attachPicture(WritableSheet sheet,String url,float x, float y, float width,float height) throws IOException {
        //图片


        WritableImage image;

        BufferedImage bufferedImage=null;
        try {
              bufferedImage = ImageIO.read(new URL(url));
        }catch (IOException e)
        {
            e.printStackTrace();
        }


        if(bufferedImage!=null) {
            baos.reset();
            ImageIO.write(bufferedImage, "PNG", baos);
            bufferedImage.flush();
            image = new WritableImage(x, y, width, height, baos.toByteArray());
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
