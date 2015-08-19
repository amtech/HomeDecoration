package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.utils.AccumulateMap;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import jxl.write.*;
import jxl.write.Number;

import java.io.IOException;

/**802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_314 extends ExcelReportor {


    public Report_Excel_314(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {




        int defaultRowCount=6;
        int startItemRow=1;

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

                attachPicture(writableSheet,HttpUrl.loadProductPicture(item.productName, item.pVersion),4+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);


            }




            addNumber(writableSheet, i + 1, 0, rowUpdate);

            //设计号  版本号
            // addString(writableSheet, item.productName.trim(),2,rowUpdate);

            //货号
            addString(writableSheet, item.productName.trim(), 3, rowUpdate);


            //产品总尺寸  材质百分比
            addString(writableSheet, item.spec+"\n" + item.constitute, 5, rowUpdate);

            //单价
            addNumber(writableSheet, item.price, 6, rowUpdate);

            //产品重量
            addNumber(writableSheet, item.weight, 8, rowUpdate);

            //装箱数目
            addNumber(writableSheet, item.packQuantity, 9, rowUpdate);

            //外箱尺寸
            addString(writableSheet, item.packageSize.trim() ,10,rowUpdate);



        }






   }

}
