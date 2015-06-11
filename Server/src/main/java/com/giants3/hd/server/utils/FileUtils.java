package com.giants3.hd.server.utils;

import com.giants3.hd.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

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
    public static  final String getProductPicturePath(String filePath,String productName,String pVersion)
    {

        return getProductPicturePath(filePath,productName,pVersion,"jpg");

    }



    /**获取材料图片路径      默认是jpg文件
     *
     * @param filePath           文件根目录
     * @param code     材料编码
     * @return
     */
    public static  final String getMaterialPicturePath(String filePath,String code )
    {

        return getMaterialPicturePath(filePath ,code,"jpg");

    }

    /**获取材料图片路径   根据规则    默认是jpg文件
     *
     * @param filePath           文件根目录
     * @param code     材料编码
     * @return
     */
    public static  final String getMaterialPicturePath(String filePath,String code,String type )
    {

        return  filePath+code+"."+type;

    }
    /**获取图片路径   根据规则  （第一个英文字母为之前的字符串 作为文件夹）
     *
     * @param filePath    文件根目录
     * @param productName  产品名称
     * @param type   文件类型
     * @return
     */
    public static  final String getProductPicturePath(String filePath,String productName,String pVersion,String type)
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




        String   fullFilePath=filePath+(firstCharIndex>=0?productName.substring(0,firstCharIndex+1):"")+ File.separator+productName+( StringUtils.isEmpty(pVersion)?"":"_"+pVersion) +"."+type;


        return fullFilePath;


    }












    /**
     * 获取文件的最后更新时间  如果文件不存在 返回-1；
     * @param file
     * @return
     */
    public static long getFileLastUpdateTime(File file)
    {

        if(!file.exists())
            return -1;
        BasicFileAttributes attributes =
                null;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        } catch (IOException e) {
            e.printStackTrace();

        }


        if (null != attributes) {

            FileTime lastModifiedTime = attributes.lastModifiedTime();

            return lastModifiedTime.toMillis();

        }
        return 0;
    }
}
