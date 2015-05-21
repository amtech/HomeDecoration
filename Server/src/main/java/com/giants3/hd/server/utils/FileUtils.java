package com.giants3.hd.server.utils;

import java.io.File;

/**
 *  文件处理类
 */

public class FileUtils {


    /**获取图片路径   根据规则  （第一个英文字母为之前的字符串 作为文件夹）   默认是jpg文件
     *
     * @param filePath           文件根目录
     * @param productName     产品名称
     * @return
     */
    public static  final String getProductPicturePath(String filePath,String productName)
    {

        return getProductPicturePath(filePath,productName,"jpg");

    }

    /**获取图片路径   根据规则  （第一个英文字母为之前的字符串 作为文件夹）
     *
     * @param filePath    文件根目录
     * @param productName  产品名称
     * @param type   文件类型
     * @return
     */
    public static  final String getProductPicturePath(String filePath,String productName,String type)
    {


        //找到第一个英文单词。
        int len=productName.length();
        int firstCharIndex=-1;
        for (int i = 0; i < len; i++) {
            if(Character.isLetter(productName.charAt(i)))
            {
                firstCharIndex=i;
                break;
            }
        }

        String   fullFilePath=filePath+(firstCharIndex>=0?productName.substring(0,firstCharIndex+1):"")+ File.separator+productName+"."+type;


        return fullFilePath;


    }
}
