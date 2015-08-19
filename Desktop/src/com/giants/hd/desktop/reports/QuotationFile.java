package com.giants.hd.desktop.reports;

import com.giants.hd.desktop.reports.excels.*;
import com.google.inject.internal.util.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * 报价文件导出模板
 */
public class QuotationFile {


    public String name;
    public String appendix;
    public Class aClass;


    public static  final String FILE_NORMAL="通用报价单模板";

    public static  final String FILE_XIANGKANG_1="咸康家具报价格式模板";
    public static  final String FILE_XIANGKANG_2="咸康(灯具)报价格式模板";
    public static  final String FILE_XIANGKANG_3="咸康(镜子 杂)报价模板";
    public static  final String FILE_XIANGKANG_4="咸康(画类)报价模板";
    public static final String FILE_802="820客户报价模板";

    public static final String FILE_314="314客户报价格式";
    public static final String FILE_859="859客户报价格式";
    public static final String FILE_907="907客户报价格式";
    public static final String FILE_977="977客户报价格式";
    public static final String FILE_302="302客户报价格式";
    public static final String FILE_127="127客户报价格式";
    public static  final String[] FORMATS=new String[]{
            FILE_NORMAL,FILE_XIANGKANG_1,FILE_XIANGKANG_2,FILE_XIANGKANG_3,FILE_XIANGKANG_4,
            FILE_802,FILE_314,FILE_859,FILE_907,FILE_977,FILE_302,FILE_127


    };

    public static  final Class[] CLASSES=new Class[]{
            Report_Excel_NORMAL.class, Report_Excel_XK_JIAJU.class, Report_Excel_XK_DENGJU.class, Report_Excel_XK_JINGZI_ZA.class, Report_Excel_XK_HUALEI.class,
            Report_Excel_802.class,Report_Excel_314.class,Report_Excel_859.class,Report_Excel_907.class,Report_Excel_977.class,Report_Excel_302.class,Report_Excel_127.class



    };

    public static final String XLS="xls";
    public static final String XLSX="xlsx";
    public static  final String[] APPENDIXS=new String[]{
            XLS,XLS,XLS,XLS,XLS,
            XLS,XLS,XLS,XLS,XLS,XLS,XLS


    };
    public static List<QuotationFile> defaultFiles;



    static {
        defaultFiles=new ArrayList<>();

        int length=FORMATS.length;
        for (int i = 0; i < length; i++) {


            QuotationFile file=new QuotationFile();
            file.name=FORMATS[i];
            file.appendix=APPENDIXS[i];
            file.aClass= CLASSES[i];
            defaultFiles.add(file);
        }



    }

    @Override
    public String toString() {
        return name;
    }
}
