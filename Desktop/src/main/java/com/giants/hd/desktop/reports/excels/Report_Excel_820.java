package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.reports.QuotationFile;
import com.giants.hd.desktop.utils.AccumulateMap;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import jxl.write.*;

import java.io.IOException;

/**802 客户 模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_820 extends ExcelReportor {


    public Report_Excel_820(QuotationFile modelName) {
        super(modelName);
    }


    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableWorkbook writableWorkbook) throws WriteException, IOException, HdException {


        int size = quotationDetail.items.size();

        AccumulateMap names=new AccumulateMap();


        Quotation quotation=quotationDetail.quotation;

        float pictureGap=0.1f;
        for(int i=0;i< size;i++)
        {
            QuotationItem item=quotationDetail.items.get(i);

            names.accumulate(item.productName);


          int duplicateCount=  names.get(item.productName).intValue();
            writableWorkbook.copySheet(0,item.productName+(duplicateCount>1?("_"+(duplicateCount-1)):""),i+1);
            WritableSheet writableSheet=  writableWorkbook.getSheet(i+1);




//            //移除照片
//
//            for(int imageIndex=0,count=writableSheet.getNumberOfImages();imageIndex<count;imageIndex++)
//            {
//                WritableImage writableImage=null;
//                try {
//                      writableImage = writableSheet.getImage(imageIndex);
//                }catch (Throwable t)
//                {
//
//                }
//                if(writableImage!=null)
//                   writableSheet.removeImage(writableImage);
//            }



//            Label       label;
//
//            jxl.write.Number num ;
//            WritableImage image;
            // 插入日期

                    //报价日期
            addString(writableSheet, quotation.qDate, 10, 0);

            //货号
            addString(writableSheet, item.productName, 6, 11);



            //主要成分
            addString(writableSheet, item.constitute, 6, 12);


            //尺寸   英寸
            addString(writableSheet, item.spec, 6, 15);


            //包装尺寸   英寸
            addString(writableSheet, item.packageSize, 6, 16);

            //每箱数目
            int unitSize=1;
            try {
                unitSize= Integer.valueOf(item.unit.substring(item.unit.lastIndexOf("/")));
            }catch (Throwable t)
            {

            }

            addNumber(writableSheet, unitSize, 6, 19);

            //cbm 体积

            addNumber(writableSheet, item.volumeSize, 6, 20);



            //fob
            float fob=item.price;
            addNumber(writableSheet, fob, 9, 18);
            addNumber(writableSheet, fob, 10, 18);





                attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion), 0 + pictureGap, 10 + pictureGap, 5 - pictureGap, 13 - pictureGap);




            //覆盖样品数据
             addString(writableSheet,"",5,25);
            addString(writableSheet,"",13, 18);
            addString(writableSheet,"",13, 19);


        }







   }

}
