package com.neu.test;

public class TestThread {
	
	private String flag = "true";
	
	public void testThread(){
		synchronized(flag){
			flag = "false";
			flag.notify();
		}
		
		synchronized (flag) {
			while(flag != "false"){
				System.out.println("��ʼ�ȴ�");
				
			}
		}
		
	}
	public static void main(String[] args) {
		
		
	}
}
