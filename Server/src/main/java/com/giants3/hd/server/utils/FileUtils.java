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


    public static final String IMAGE_JPG="jpg";
    public static final String  DOWNLOAD_PATH="api/file/download/";
    public static final String DOWNLOAD_MATERIAL_CODE =DOWNLOAD_PATH+"material/%s/%s.%s?type=%s&mClass=%s";
    public static final String DOWNLOAD_PRODUCT_NAME_P_VERSION = DOWNLOAD_PATH+"product/%s/%s/%s.%s?type=%s";
    public static final String DOWNLOAD_TEMP_NAME = "/download/temp/{name}";
    public static final String DOWNLOAD_ATTACH_NAME = "/download/attach/{name}";



    /**
     * /**获取图片路径 url  相对路径 容器根目录
     * @param productName
     * @param pVersion
     * @param updateTime 更新时间
     * @return
     */
    public static  final String getProductPictureURL( String productName,String pVersion,long updateTime)
    {


       return updateTime<=0?"": String.format(DOWNLOAD_PRODUCT_NAME_P_VERSION,productName,pVersion,updateTime,IMAGE_JPG,IMAGE_JPG);
    //   return updateTime<=0?"":API+DOWNLOAD_PRODUCT_NAME_P_VERSION.replace("{name}",productName).replace("{pVersion}", StringUtils.isEmpty(pVersion)?"":pVersion).replace("{updateTime}",String.valueOf(updateTime));

    }

    /**
     * /**获取材料图片路径 url  相对路径 容器根目录
     * @param materialCode
     * @param lastUpdateTime 更新时间
     * @return
     */
    public static  final String getMaterialPictureURL( String materialCode,String mClass,long lastUpdateTime)
    {
        return lastUpdateTime<=0?"": String.format(DOWNLOAD_MATERIAL_CODE,materialCode,lastUpdateTime,IMAGE_JPG,IMAGE_JPG,mClass);
       // return lastUpdateTime<=0?"":DOWNLOAD_MATERIAL_CODE.replace("{materialCode}", materialCode).replace("{updateTime}", String.valueOf(lastUpdateTime));

    }

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
     *                      @param mClass    材料类型 即子文件夹
     * @return
     */
    public static  final String getMaterialPicturePath(String filePath,String code,String mClass )
    {

        return getMaterialPicturePath(filePath, code, mClass, "jpg");

    }

    /**获取材料图片路径   根据规则    默认是jpg文件
     *
     * @param filePath           文件根目录
     * @param code     材料编码
     *                 @param mClass    材料类型 即子文件夹
     * @return
     */
    public static  final String getMaterialPicturePath(String filePath,String code,String mClass,String type )
    {




        if(code.startsWith("C")||code.startsWith("c"))
        {

            mClass="C";
        }
        if(StringUtils.isEmpty(mClass))
        {

            return  filePath +code+"."+type;
        }
        else
            return  filePath+mClass+File.separator+code+"."+type;

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


        //找到文件夹
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


        //文件夹
      String   directory=(firstCharIndex>=0?productName.substring(0,firstCharIndex+1):"");






        //匹配的文件名
        String productFileName ;

        if(!StringUtils.isEmpty(pVersion))
        {
            //第四五位 如果为0 直接匹配原来的货号。
             if(pVersion.length()==6&&pVersion.charAt(3)=='0'&&pVersion.charAt(4)=='0')
             {




                     productFileName= productName;

             }else
             {
                 productFileName= productName+ "-"+pVersion ;
             }


        }else
        {
            productFileName=productName;
        }




        String   fullFilePath=filePath+directory+ File.separator+productFileName+"."+type;


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


    /**
     * 根据文件名 获取路径
     * @param materialFilePath
     * @param fileName  文件名 带后缀
     */
    public static String getMaterialPicturePath(String materialFilePath, String fileName) {


        int indexOfDot= fileName.indexOf(".");
        return   getMaterialPicturePath(materialFilePath, fileName.substring(0, indexOfDot), fileName.substring(0, 4), fileName.substring(indexOfDot + 1));

    }


    /**
     * 根据文件名 获取模板文件。
     * @param quotationfilepath
     * @param name
     * @return
     */
    public static String getQuotationFile(String quotationfilepath, String name,String appendix) {

        return quotationfilepath+File.separator+name+"."+appendix;
    }
}
