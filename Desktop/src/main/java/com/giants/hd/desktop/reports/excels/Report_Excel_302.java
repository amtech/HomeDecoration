package com.giants.hd.desktop.reports.excels;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.IOException;

/**802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_302 extends ExcelReportor {


    public Report_Excel_302(QuotationFile modelName) throws HdException {
        super(modelName);

    }


    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet, WritableWorkbook writableWorkbook) throws WriteException, IOException, HdException {




        int defaultRowCount=1;
        int startItemRow=30;

        int dataSize=quotationDetail.items.size();
        //实际数据超出范围 插入空行
        duplicateRow(writableSheet,startItemRow,defaultRowCount,dataSize);


        Quotation quotation=quotationDetail.quotation;
        //报价日期
        addString(writableSheet, quotation.qDate, 3, 8);

        float pictureGap=0.1f;
        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startItemRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片


                attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion),6+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);




            //序号
            addNumber(writableSheet, i + 1, 0, rowUpdate);


            //货号
            addString(writableSheet, item.productName.trim(), 3, rowUpdate);


            //   单位
            addString(writableSheet, item.unit, 5, rowUpdate);


            //   产品描述
            addString(writableSheet, item.memo, 9, rowUpdate);


            //产品尺寸

          String[]  specValue= cmToInchSpec(StringUtils.decoupleSpecString(item.spec));
            addString(writableSheet, specValue[0], 10, rowUpdate);


            addString(writableSheet, specValue[1], 11, rowUpdate);

            addString(writableSheet, specValue[2], 12, rowUpdate);



            //   重量
            addNumber(writableSheet, item.weight, 13, rowUpdate);


            //   材质百分比
            addString(writableSheet, item.constitute, 17, rowUpdate);


            //外箱尺寸
            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);


            addNumber(writableSheet,  packValue[0] , 40, rowUpdate);

            addNumber(writableSheet, packValue[1] , 41, rowUpdate);

            addNumber(writableSheet,  packValue[2] , 42, rowUpdate);



            //单价 fob
            addNumber(writableSheet, item.price, 48, rowUpdate);



        }






   }

}
