package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.UnitUtils;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

import java.io.IOException;

/**802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_859 extends ExcelReportor {


    public Report_Excel_859(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {




        int defaultRowCount=9;
        int startItemRow=2;

        int dataSize=quotationDetail.items.size();
        //实际数据超出范围 插入空行
        duplicateRow(writableSheet,startItemRow,defaultRowCount,dataSize);

        float pictureGap=0.1f;
        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startItemRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //图片
            if(item.productPhoto!=null) {

                attachPicture(writableSheet,HttpUrl.loadProductPicture(item.productName, item.pVersion),1+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);


            }





            //货号
            addString(writableSheet, item.productName.trim(), 0, rowUpdate);


            //   材质百分比
            addString(writableSheet, item.constitute, 2, rowUpdate);

            //产品尺寸

          String[]  specValue= cmToInchSpec(StringUtils.decoupleSpecString(item.spec));
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
