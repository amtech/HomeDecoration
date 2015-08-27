package com.giants.hd.desktop.local;

import java.io.*;

/**
 *
 * 本地缓存文件
 * Created by davidleen29 on 2015/8/26.
 */
public class LocalFileHelper {
    public static final String path="localFile";



    static
    {

        File file=new File(path);

        file.mkdirs();

    }


    public static   <T>  void set(T object)
    {

        try{

            FileOutputStream fos=new FileOutputStream(new File(path,object.getClass().getName()));
            ObjectOutputStream obs=new ObjectOutputStream(fos);
            obs.writeObject(object);
            obs.close();
            fos.close();


        }catch (IOException io)
        {
            io.printStackTrace();
        }


    }


    public static final<T> T get(Class<T> tClass)
    {

        T  result=null;
        try{

            FileInputStream fos=new FileInputStream(new File(path,tClass.getName()));
            ObjectInputStream ois=new ObjectInputStream(fos);
          result= (T) ois.readObject( );
            ois.close();
            fos.close();


        }catch (IOException io)
        {
            io.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return result;
    }



}
