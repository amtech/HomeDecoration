package com.giants3.hd.utils;

import java.io.*;

/**
 * Created by davidleen29 on 2015/8/14.
 */
public class FileUtils {



    public static void copyFile(File destFile, File sourceFile)
    {

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            if(destFile.exists()){
                destFile.delete();
            }
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024 * 5];
            int size;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
                out.flush();
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException ex1) {
            }
        }



    }
}
