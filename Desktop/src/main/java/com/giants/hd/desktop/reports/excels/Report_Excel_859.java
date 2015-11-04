package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_859 extends ExcelReportor {

    Workbook workbook;
    public Report_Excel_859(QuotationFile modelName) {
        super(modelName);
    }
    @Override
    protected void operation(QuotationDetail quotationDetail, URL url, String outputFile) throws IOException, HdException {




        //Create Workbook instance holding reference to .xlsx file
        InputStream inputStream=url.openStream();
        workbook = new HSSFWorkbook(inputStream);




        writeOnExcel(quotationDetail, workbook.getSheetAt(0));


        FileOutputStream fos=    new FileOutputStream(outputFile);

        workbook.write(fos);
        workbook.close();

        fos.close();






    }

    protected void writeOnExcel(QuotationDetail quotationDetail, Sheet writableSheet ) throws   IOException, HdException {





        int defaultRowCount=9;
        int startItemRow=2;

        int dataSize=quotationDetail.items.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook,writableSheet,startItemRow,defaultRowCount,dataSize);


        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startItemRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片


                attachPicture(workbook,writableSheet, HttpUrl.loadProductPicture(item.photoUrl),1 , rowUpdate ,2, rowUpdate);






            //货号
            addString(writableSheet, item.productName.trim(), 0, rowUpdate);


            //   材质百分比
            addString(writableSheet, item.constitute, 2, rowUpdate);

            //产品尺寸

          String[]  specValue= groupSpec(StringUtils.decoupleSpecString(item.spec));
            addString(writableSheet, specValue[0], 3, rowUpdate);


            addString(writableSheet, specValue[1], 4, rowUpdate);

            addString(writableSheet, specValue[2], 5, rowUpdate);


            //产品重量
            addNumber(writableSheet, item.weight, 6, rowUpdate);

            //单价
            addNumber(writableSheet, item.price, 9, rowUpdate);



            //装箱数目
            addNumber(writableSheet, item.packQuantity, 26, rowUpdate);

            //外箱尺寸
            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);


            addNumber(writableSheet,  UnitUtils.cmToInch(packValue[0]), 27, rowUpdate);

            addNumber(writableSheet, UnitUtils.cmToInch(packValue[1]), 28, rowUpdate);

            addNumber(writableSheet, UnitUtils.cmToInch(packValue[2]), 29, rowUpdate);




        }






   }

}
