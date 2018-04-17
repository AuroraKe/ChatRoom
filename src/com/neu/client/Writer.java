package com.neu.client;

import java.util.LinkedList;
import java.util.List;

import com.neu.bean.Message;

public final class Writer implements Runnable{
	private static List<Message> pool = new LinkedList<>();
	
	@Override
	public void run() {
		while(true){
			try {
				Message message;
				synchronized(pool){
					while(pool.isEmpty())
						pool.wait();
					message = pool.remove(0);
				}
				write(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void write(Message message) {
		
	}

}
