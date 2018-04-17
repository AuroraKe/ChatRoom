package com.neu.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * @author 千古传奇 此类用于播放音乐
 */
public class Sound implements Runnable {

	private static String musicFile; // 要播放的音乐的路径

	public Sound(String soundsName) {
		if (soundsName == null)
			return;
		String os = System.getProperty("os.name"); //判断当前的操作系统
		if(os.equals("Linux"))
			musicFile = System.getProperty("user.dir") + "/sound/" + soundsName + ".wav";
		else
			musicFile = System.getProperty("user.dir") + "\\sound\\" + soundsName + ".wav";
	}

	public Sound() {
		// this(null);
	}

	static boolean tag = true; // 停止音乐时使用，true表示播放音乐，false停止播放
	static boolean tag2 = false; // 暂停音乐时使用，false表示播放，true暂停播放
	private SourceDataLine dataLine;

	@Override
	public void run() {
		tag = true;
		File file = new File(musicFile);
		if (!file.exists()) {
			System.out.println("该音乐文件不存在");
			return;
		}
		AudioInputStream audioInputStream = null;
		try {
			// 其实就相当于FileInputStream
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		AudioFormat format = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			dataLine = (SourceDataLine) AudioSystem.getLine(info);
			dataLine.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		}

		dataLine.start();
		int count = 0; // 用于标记读入bytes的字节数
		byte[] bytes = new byte[1024]; // 用于暂时存储audioInputStream的一段字节
		// 一般情况下，while写到try里面比较好，不会陷入死循环
		try {
			while (count != -1 && tag) {
				while (tag2) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				count = audioInputStream.read(bytes, 0, bytes.length);
				if (count >= 0)
					dataLine.write(bytes, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			dataLine.drain();
			dataLine.close();
		}
	}

	// 开始音乐播放
	/**
	 * 此方法用于开始播放音乐
	 * 
	 * @param soundsName
	 *            要播放音乐的名字
	 */
	public static void play(String soundsName) {
		tag = true; //由于点击停止按钮时，会把tag置为false
		Sound sound = new Sound(soundsName);
//		Thread thread = new Thread(sound);
//		if (thread == null || !thread.isAlive()) {
//			thread = new Thread(sound);
//			thread.start();
//		}
		new Thread(sound).start();
	}

	// 暂停音乐播放
	/**
	 * @param miunute
	 *            暂停音乐播放，这里以分为单位
	 */
	public static synchronized void pause(long miunutes) {
		long second = miunutes * 60;
		final long millis = second << 10; // 相当于乘以1024，实际上应该乘1000
		if (millis <= 0 || Float.isNaN(millis)) {
			throw new IllegalArgumentException("参数异常" + miunutes);
		}
		// if(millis > Long.MAX_VALUE)
		// millis = Long.MAX_VALUE;
//		long currentTime = System.currentTimeMillis();
		tag2 = true;
		new Thread() {
			public void run() {
				if (tag2) {
					try {
						Thread.sleep(millis);
						tag2 = false;
						return;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 
	 */
	public static synchronized void start() {
		if (tag2) {
			tag2 = false;
			return;
		}
	}
	
	/**
	 * 停止音乐的播放
	 */
	public static void stop() {
		tag = false;
		return;
	}

	public static void main(String[] args) {
		Sound.play("dream");
	}
}
