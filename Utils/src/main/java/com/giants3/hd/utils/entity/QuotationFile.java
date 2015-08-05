package com.giants3.hd.utils.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 报价文件导出模板
 */
public class QuotationFile {


    public String name;


    public static  final String FILE_NORMAL="通用报价单模板";

    public static  final String FILE_XIANGKANG_1="咸康家具报价格式";
    public static  final String FILE_XIANGKANG_2="咸康(灯具)报价格式";
    public static  final String FILE_XIANGKANG_3="咸康(镜子 杂+画)报价";
    public static List<QuotationFile> defaultFiles;



    static {
        defaultFiles=new ArrayList<>();

        QuotationFile file;

        file=new QuotationFile();
        file.name
                =FILE_NORMAL;
        defaultFiles.add(file);
        file=new QuotationFile();
        file.name
                =FILE_XIANGKANG_1;
        defaultFiles.add(file);

        file=new QuotationFile();
        file.name=FILE_XIANGKANG_2;
        defaultFiles.add(file);

        file=new QuotationFile();
        file.name=FILE_XIANGKANG_3;
        defaultFiles.add(file);

    }

    @Override
    public String toString() {
        return name;
    }
}
