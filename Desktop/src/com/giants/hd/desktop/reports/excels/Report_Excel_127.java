package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.utils.AccumulateMap;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;

/**802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_127 extends ExcelReportor {


    public Report_Excel_127(QuotationFile modelName) {
        super(modelName);
    }




    @Override
    protected  void doOnLocalFile(QuotationDetail quotationDetail, File outputFile) throws IOException, BiffException, HdException, WriteException {


        Workbook workbook=Workbook.getWorkbook(outputFile);
        WritableWorkbook workbookCopy = Workbook.createWorkbook(outputFile, Workbook.getWorkbook(outputFile));
        int size = quotationDetail.items.size();
        AccumulateMap names=new AccumulateMap();
        for(int i=0;i< size;i++) {
            QuotationItem item = quotationDetail.items.get(i);

            names.accumulate(item.productName);


            int duplicateCount = names.get(item.productName).intValue();

            Sheet sampleSheet = workbook.getSheet(0);
            workbookCopy.importSheet(item.productName + (duplicateCount > 1 ? ("_" + (duplicateCount - 1)) : ""), i + 1, sampleSheet);

        }
        workbookCopy.write();
        workbookCopy.close();
        workbook.close();
       super.doOnLocalFile(quotationDetail,outputFile);




    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableWorkbook writableWorkbook) throws WriteException, IOException, HdException {




        int size = quotationDetail.items.size();




        Quotation quotation=quotationDetail.quotation;

        float pictureGap=0.3f;
        for(int i=0;i< size;i++)
        {
            QuotationItem item=quotationDetail.items.get(i);

//            names.accumulate(item.productName);
//
//
//          int duplicateCount=  names.get(item.productName).intValue();
//
//            Sheet sampleSheet=writableWorkbook.getSheet(0);
//            writableWorkbook.importSheet(item.productName + (duplicateCount > 1 ? ("_" + (duplicateCount - 1)) : ""), i + 1, sampleSheet);
            WritableSheet writableSheet = writableWorkbook.getSheet(i+1);


            //报价日期
            addString(writableSheet, quotation.qDate, 5, 2);

            //货号
            addString(writableSheet, item.productName, 1, 3);



            //主要成分
            addString(writableSheet, item.memo, 1, 4);


            //尺寸
            addString(writableSheet, item.spec, 1, 6);


            //重量
            addNumber(writableSheet, item.weight, 6, 18);


            //内盒数
            addNumber(writableSheet, item.inBoxCount, 6, 22);

            //包装数
            addNumber(writableSheet, item.packQuantity, 6, 23);


            //包装规格
            addString(writableSheet, item.packageSize, 6, 26);



            //cbm 体积
            addNumber(writableSheet, item.volumeSize, 6, 27);



            //fob
            float fob=item.price;
            addNumber(writableSheet, fob, 6, 41);


             attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion), 0 + pictureGap/2, 8+ pictureGap, 6 - pictureGap, 40 - pictureGap);



//
//            //覆盖样品数据
//             addString(writableSheet, "", 5, 25);
//            addString(writableSheet, "", 13, 18);
//            addString(writableSheet, "", 13, 19);


        }







   }

}
