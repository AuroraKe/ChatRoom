package com.neu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileLoad {

	/**
	 * @param properties 
	 * @param path 文件所在路径
	 */
	public Properties loadProperties(String path) {
		File file = new File(path);
		Properties properties = new Properties();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			properties.load(new FileInputStream(file));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static void main(String[] args) {
		Properties properties = new Properties();
		FileLoad fileLoad = new FileLoad();
		fileLoad.loadProperties("client.properties");
		String property = properties.getProperty("chatingRoom");
		System.out.println(property);
	}
}
