package com.giants.hd.desktop.local;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/**
 * 配置文件帮助类
 */
public class PropertyWorker {

	/**
	 * 指定property文件
	 */
	private static final String PROPERTY_FILE = "config.properties";

	/**
	 * 根据Key 读取Value
	 * 
	 * @param key
	 * @return
	 */
	public static String readData(String key) {



        //以包起始的地方开始   jar 根目录开始。
		InputStream inputStream=	PropertyWorker.class.getClassLoader().getResourceAsStream(PROPERTY_FILE) ;




		if(inputStream!=null) {





		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(inputStream);


			props.load(in);
			in.close();
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			finally {
			try{
				inputStream.close();
			}catch (Throwable t)
			{}
		}
		}
		return null;


	}

	/**
	 * 修改或添加键值对 如果key存在，修改 反之，添加
	 * 
	 * @param key
	 * @param value
	 */
	public static void writeData(String key, String value) {
		Properties prop = new Properties();
		try {
			File file = new File(PROPERTY_FILE);
			if (!file.exists())
				file.createNewFile();
			InputStream fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();// �?定要在修改�?�之前关闭fis
			OutputStream fos = new FileOutputStream(PROPERTY_FILE);
			prop.setProperty(key, value);
			prop.store(fos, "Update '" + key + "' value");
			fos.close();
		} catch (IOException e) {
			System.err.println("Visit " + PROPERTY_FILE + " for updating "
					+ value + " value error");
		}
	}
}