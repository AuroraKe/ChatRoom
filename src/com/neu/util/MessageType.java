package com.neu.util;

public class MessageType {
	/**
	 * The {@code int} value representing the {@code online}
	 */
	public static final int ONLINE = 0x00000001;
	
	/**
	 * The {@code int} value representing the {@code chat}
	 */
	public static final int CHAT = 0x00000002;
	
	/**
	 * The {@code int} value representing the {@code the request of sending file.}
	 */
	public static final int SEND_FILE_REQ = 0x00000004;
	
	/**
	 * The {@code int} value representing the request is accepted.
	 * The user can begin to transfer the file.
	 */
	public static final int ACCEPET_FILE_REQ = 0x00000008;
	
	/**
	 * The {@code int} value representing the request who transfers 
	 * the file is refused.
	 */
	public static final int REFUSE_FILE_REQ = 0x00000010;
	
	/**
	 * The {@code int} value representing the {@code offline.}
	 */
	public static final int OFFLINE = 0x00000020;
	
	/**
	 * Due to unexpectedly exit.
	 */
	public static final int EXIT = 0x00000040;
}
