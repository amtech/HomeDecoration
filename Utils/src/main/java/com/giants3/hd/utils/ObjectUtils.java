package com.giants3.hd.utils;

import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.pools.ObjectPool;

import java.io.*;
import java.util.List;

/**
 * java
 *   对象 工具类。
 */
public class ObjectUtils {


    /**
     * 深度拷贝对象。
     * @param object
     */
    public static   Object   deepCopy(Object object)
    {
        //深度复制对象，
      return   deserialize(serialize(object));


    }



    /**
     * Serialize the given object to a byte array.
     * @param object the object to serialize
     * @return an array of bytes representing the object in a portable fashion
     */
    public static   byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }
        return baos.toByteArray();
    }

    /**
     * Deserialize the byte array into an object.
     * @param bytes a serialized object
     * @return the result of deserializing the bytes
     */
    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return   ois.readObject();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }

}