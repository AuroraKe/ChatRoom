package com.neu.exception;

public class IllegalArgException extends RuntimeException{

	private String detailMessage;
	/**
	 * @param s ÏêÏ¸µÄÐÅÏ¢
	 */
	public IllegalArgException(String message) {
		fillInStackTrace();
		detailMessage = message;
	}
	
//	@Override
//	public synchronized Throwable fillInStackTrace(){
//		
//		return this;
//	}
	
	private static final long serialVersionUID = 5112840049504904943L;
}
