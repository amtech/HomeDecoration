package com.giants.hd.desktop.reports;

import com.giants.hd.desktop.reports.excels.ExcelReportor;
import com.giants.hd.desktop.reports.excels.Report_Excel_NORMAL;
import com.giants.hd.desktop.reports.excels.Report_Excel_XK_DENGJU;
import com.giants.hd.desktop.reports.excels.Report_Excel_XK_HUA_ZA_JINGZI;
import com.giants3.hd.utils.entity.QuotationFile;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Singleton;

/**
 * Created by davidleen29 on 2015/8/6.
 */

@Singleton
public class ReportFactory {


    public ExcelReportor createExcelReportor(String modelName) throws HdException {

        ExcelReportor report=null;
        switch (modelName)
        {
            case  QuotationFile.FILE_NORMAL:


                report= new Report_Excel_NORMAL(modelName);

                break;


            case QuotationFile.FILE_XIANGKANG_1:



                break;

            case QuotationFile.FILE_XIANGKANG_2:

                report=  new Report_Excel_XK_DENGJU(modelName);
                break;

            case QuotationFile.FILE_XIANGKANG_3:
                report=  new Report_Excel_XK_HUA_ZA_JINGZI(modelName);

                break;
        }


        if(report==null)
        {

            throw   HdException.create("未定义相应导出处理与模板" + modelName + "相关联");
        }
        return report;
    }
}
