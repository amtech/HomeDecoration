package com.giants3.hd.server.utils;

import com.giants3.hd.server.entity.Material;
import com.giants3.hd.server.entity.MaterialDelete;
import com.giants3.hd.server.entity.ProductDelete;
import com.giants3.hd.server.entity.QuotationDelete;
import com.giants3.hd.server.noEntity.ProductDetail;
import com.giants3.hd.server.noEntity.QuotationDetail;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.exception.HdException;

import java.io.*;

/**
 * Created by davidleen29 on 2015/8/26.
 */
public class BackDataHelper {


    public static <T> void back(T data, String fileFilePath) {


        //备份数据
        try {


            String gsonString = GsonUtils.toJson(data);
            //文件形式保存
            FileOutputStream fileOutputStream = new FileOutputStream(fileFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(gsonString);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.close();


        } catch (IOException io) {
            io.printStackTrace();
        }


    }


    public static <T> T read(String fileFilePath, Class<T> tClass) {


        //备份产品数据
        T result = null;
        try {

            //文件形式保存
            FileInputStream fileInputStream = new FileInputStream(fileFilePath);
            Reader reader = new InputStreamReader(fileInputStream);
            result = GsonUtils.fromReader(reader, tClass);

            reader.close();
            fileInputStream.close();

            ;


        } catch (IOException io) {
            io.printStackTrace();
        } catch (HdException e) {
            e.printStackTrace();
        }
        return result;


    }

    public static boolean delete(String fileFilePath) {


        return new File(fileFilePath).delete();


    }


    public static void backProduct(ProductDetail data, String path, ProductDelete deleteMessage) {


        //备份产品数据
        back(data, path + deleteMessage.toString());


    }


    public static void backQuotation(QuotationDetail data, String path, QuotationDelete deleteMessage) {


        //备份产品数据
        back(data, path + deleteMessage.toString());


    }


    public static void backMaterial(Material data, String path, MaterialDelete deleteMessage) {


        //备份产品数据
        back(data, path + deleteMessage.toString());


    }


    public static QuotationDetail getQuotationDetail(String fileDirectory, QuotationDelete quotationDelete) {


        return read(fileDirectory + quotationDelete.toString(), QuotationDetail.class);

    }

    public static ProductDetail getProductDetail(String deleteProductPath, ProductDelete productDelete) {

        return read(deleteProductPath + productDelete.toString(), ProductDetail.class);
    }

    public static void deleteProduct(String deleteProductPath, ProductDelete productDelete) {


        delete(deleteProductPath + productDelete.toString());

    }

    public static void deleteQuotation(String deleteQuotationFilePath, QuotationDelete quotationDelete) {

        delete(deleteQuotationFilePath + quotationDelete.toString());
    }
}
