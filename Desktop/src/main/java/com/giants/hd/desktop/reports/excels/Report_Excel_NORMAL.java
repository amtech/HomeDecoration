package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.reports.QuotationFile;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import jxl.write.*;

import java.io.IOException;

/** 通用报价模板
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_NORMAL extends ExcelReportor {


    public Report_Excel_NORMAL(QuotationFile modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException {


        int defaultRowCount=7;
        int startItemRow=9;

        int dataSize=quotationDetail.items.size();

        //实际数据超出范围 插入空行
        duplicateRow(writableSheet,startItemRow,defaultRowCount,dataSize);



        Quotation quotation=quotationDetail.quotation;
        //表头
        //注入报价单号
       addString(writableSheet, quotation.qNumber, 2, 1);



        //报价日期
        addString(writableSheet, quotation.qDate, 14, 1);






        //TO
        addString(writableSheet, quotation.customerName, 2, 7);


        //业务员代码
        addString(writableSheet, quotation.salesman, 11, 7);






        float pictureGap=0.1f;


        for (int i = 0; i <dataSize; i++) {



            int rowUpdate=startItemRow+i;
            QuotationItem item=quotationDetail.items.get(i);

            //图片


                attachPicture(writableSheet, HttpUrl.loadProductPicture(item.productName, item.pVersion),0+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap);



            //读取咸康数据
            //  ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);







            //货号
            addString(writableSheet, item.productName.trim(), 2, rowUpdate);






            //材料比重
            addString(writableSheet, item.constitute.trim(), 4, rowUpdate);



            //单位
            int lastIndex=item.unit.lastIndexOf("/");
            addString(writableSheet, lastIndex == -1 ? "1" : item.unit.substring(lastIndex + 1), 6, rowUpdate);

            //FOb
            addNumber(writableSheet, item.price, 7, rowUpdate);

            //包装尺寸
            //内盒数
            addNumber(writableSheet, item.inBoxCount, 8, rowUpdate);


            //包装数
            addNumber(writableSheet, item.packQuantity, 9, rowUpdate);



            //解析出长宽高

            float[] result=     StringUtils.decouplePackageString(item.packageSize);


            //包装长
            addNumber(writableSheet, result[0], 11, rowUpdate);

            //包装宽
            addNumber(writableSheet, result[1], 13, rowUpdate);


            //包装高
            addNumber(writableSheet, result[2], 15, rowUpdate);



            //包装体积
            addNumber(writableSheet, item.volumeSize, 16, rowUpdate);



            //产品规格
            addString(writableSheet, item.spec.trim(), 17, rowUpdate);


            //镜面尺寸
            addString(writableSheet, item.mirrorSize.trim(), 19, rowUpdate);


            //净重
            addNumber(writableSheet, item.weight, 20, rowUpdate);


            //备注
            addString(writableSheet, item.memo.trim(), 22, rowUpdate);


        }






    }
}
