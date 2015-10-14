package com.giants.hd.desktop.local;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.FileUtils;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Singleton;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * 文件下载管理器
 */
@Singleton
public class DownloadFileManager {

    private static final String TAG="DownloadFileManager";
    String localFilePath=LocalFileHelper.path+"/temp/files/";

    /**
     * 缓存文件至本地路径
     * @param url
     * @return   文件路径
     */
    public  String cacheFile(String url) throws IOException {
           String fileName=map(url);



        boolean cached=new File(fileName).exists();

        Logger.getLogger(TAG).info("url:"+url+",fileName:"+fileName+",cached:"+cached);
        //找到缓存
        if(cached)
        {

            return fileName;
        }
        download(url,fileName);
        return fileName;

    }


    public DownloadFileManager() {


     File f=  new  File(localFilePath);
        if(!f.exists())
        {
            f.mkdirs();
        }
    }

    public  String  map(String url)
    {



        return localFilePath+url.hashCode();

    }


    /**
     * 下载文件 由url 获取文件流 至文件中
     * @param remoteUrl
     * @param filePath
     * @throws IOException
     */
    private void download(String remoteUrl,String filePath) throws IOException {




        //TODO
        //加上淘汰算法




        InputStream is = null;



        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        File newFile=new File(filePath);

            //打开URL通道
            URL url = new URL(remoteUrl);
            is =   url.openStream();


            byte[] buffer = new byte[1024];
            int size = 0;


            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(newFile);



            //保存文件

        try{
                while ((size = bis.read(buffer)) != -1) {
                    //读取并刷新临时保存文件
                    fos.write(buffer, 0, size);
                    fos.flush();

                }

        } finally {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }






    }


    /**
     * 清楚全部图片缓存
     */
    public void clearCache() {


        File f=new File(localFilePath);
        if(f.isDirectory())
        {
            for(File childFile:f.listFiles())
            {
                childFile.delete();
            }
        }
    }
}
