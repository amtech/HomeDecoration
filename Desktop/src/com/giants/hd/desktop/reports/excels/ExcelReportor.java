package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.exception.HdException;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by davidleen29 on 2015/8/6.
 */
public   class ExcelReportor {


    private String modelName;
    public ExcelReportor(String modelName)
    {this.modelName=modelName;}
    public  final void   report(QuotationDetail quotationDetail,String fileOutputDirectory) throws IOException, BiffException, WriteException, HdException {



        File outputFile=new File(fileOutputDirectory +File.separator    + quotationDetail.quotation.qNumber + ".xls");
        Workbook existingWorkbook = Workbook.getWorkbook(new URL(HttpUrl.loadQuotationFile(modelName)).openStream());
        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, existingWorkbook);
        WritableSheet writableSheet = workbookCopy.getSheet(0);


        writeOnExcel(quotationDetail,writableSheet);



        workbookCopy.write();
        workbookCopy.close();

    }


    protected void writeOnExcel(QuotationDetail detail,WritableSheet  writableSheet) throws WriteException, IOException, HdException {


    }



}
