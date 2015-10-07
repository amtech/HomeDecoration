package com.giants.hd.desktop.utils;

import javax.swing.*;
import java.io.File;

/**
 * Created by david on 2015/10/7.
 */
public class SwingFileUtils {


    public static File getSelectedDirectory()
    {



        return getSelectedFile(JFileChooser.DIRECTORIES_ONLY);
    }



    private static File getSelectedFile(int mode)
    {



        JFileChooser fileChooser = new JFileChooser(".");
        //下面这句是去掉显示所有文件这个过滤器。
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(mode);
        int result = fileChooser.showOpenDialog(null);
        File file=null;
        if (result == JFileChooser.APPROVE_OPTION) {

            file= fileChooser.getSelectedFile();

        }
        return file;

    }
}
