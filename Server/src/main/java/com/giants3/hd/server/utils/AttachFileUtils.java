package com.giants3.hd.server.utils;

import com.giants3.hd.server.entity.StockOut;
import com.giants3.hd.utils.*;
import de.greenrobot.common.io.*;

import java.io.File;
import java.util.Calendar;

/**
 * Created by davidleen29 on 2016/7/23.
 */
public class AttachFileUtils {


    public static final String JPG = ".jpg";
    public static final String TEMP_FILE_PREFIX = "TEMP_";

    /**
     * 附件文件处理   临时文件复制到附件文件夹中， 被移除的文件 删除
     * @param newAttacheFiles    新的附件列表
     * @param oldAttachFiles   旧的附件列表
     * @param filePrefix  新文件名前缀
     *                    @param destFileDirectory  新文件存放位置。
     * @return
     */
    public static  String  updateProductAttaches(String newAttacheFiles, String oldAttachFiles,String filePrefix,String destFileDirectory,String tempFileDirectory) {

         return newAttacheFiles;

//        //修改   附件 不做处理。  开启定时任务 定时进行附件转移
//        String[] oldAttaches = StringUtils.split(oldAttachFiles);
//        String[] newAttaches = StringUtils.split(newAttacheFiles);
//        for (String oldAttach : oldAttaches) {
//
//            boolean find = false;
//            for (String newAttach : newAttaches) {
//                if (oldAttach.equals(newAttach)) {
//                    find = true;
//                    break;
//                }
//            }
//            if (!find) {
//                //移除文件
//                File file = new File(destFileDirectory + oldAttach+ JPG);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//
//        }
//
//
//        int length = newAttaches.length;
//        for (int i = 0; i < length; i++) {
//
//
//            String newAttach = newAttaches[i];
//            if (newAttach.startsWith(TEMP_FILE_PREFIX)) {
//                //移动至附件文件夹
//                File sourceFile = new File(tempFileDirectory + newAttach + JPG);
//                if (sourceFile.exists()) {
//                    String newFileName = filePrefix + "_" + Calendar.getInstance().getTimeInMillis();
//                    com.giants3.hd.utils.FileUtils.copyFile(new File(destFileDirectory + newFileName +JPG), sourceFile);
//                    sourceFile.delete();
//                    newAttaches[i] = newFileName;
//                }
//
//            }
//
//
//        }
//       return StringUtils.combine(newAttaches);

    }


}
