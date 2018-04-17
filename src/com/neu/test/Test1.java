package com.neu.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class Test1 {
	
	@Test
	public void test1(){
		String property = System.getProperty("os.name");
		System.out.println(property);
	}
}
