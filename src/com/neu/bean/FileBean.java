package com.neu.bean;

import java.io.Serializable;

public class FileBean implements Serializable{
	/**
	 * use serialVersionUID for interoperability.
	 */
	private static final long serialVersionUID = 2017100214020601L;

	/**
	 * The IP to use when transferring file.
	 * The client needs to receive the file from this IP.
	 */
	private String ip;

	/**
	 * The port number to use when transferring file.
	 * The client needs to receive the file from this port number.
	 */
	private int port;
	
	/**
	 * The fileName is used for recording the name of the file what
	 * will transfer.
	 */
	private String fileName;
	
	/**
	 * The size of the file to be transfered.
	 */
	private long fileSize;
	
	/**
	 * The path of the file need to upload.
	 */
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
