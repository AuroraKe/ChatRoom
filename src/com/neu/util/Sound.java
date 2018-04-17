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
 * @author ǧ�Ŵ��� �������ڲ�������
 */
public class Sound implements Runnable {

	private static String musicFile; // Ҫ���ŵ����ֵ�·��

	public Sound(String soundsName) {
		if (soundsName == null)
			return;
		String os = System.getProperty("os.name"); //�жϵ�ǰ�Ĳ���ϵͳ
		if(os.equals("Linux"))
			musicFile = System.getProperty("user.dir") + "/sound/" + soundsName + ".wav";
		else
			musicFile = System.getProperty("user.dir") + "\\sound\\" + soundsName + ".wav";
	}

	public Sound() {
		// this(null);
	}

	static boolean tag = true; // ֹͣ����ʱʹ�ã�true��ʾ�������֣�falseֹͣ����
	static boolean tag2 = false; // ��ͣ����ʱʹ�ã�false��ʾ���ţ�true��ͣ����
	private SourceDataLine dataLine;

	@Override
	public void run() {
		tag = true;
		File file = new File(musicFile);
		if (!file.exists()) {
			System.out.println("�������ļ�������");
			return;
		}
		AudioInputStream audioInputStream = null;
		try {
			// ��ʵ���൱��FileInputStream
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
		int count = 0; // ���ڱ�Ƕ���bytes���ֽ���
		byte[] bytes = new byte[1024]; // ������ʱ�洢audioInputStream��һ���ֽ�
		// һ������£�whileд��try����ȽϺã�����������ѭ��
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

	// ��ʼ���ֲ���
	/**
	 * �˷������ڿ�ʼ��������
	 * 
	 * @param soundsName
	 *            Ҫ�������ֵ�����
	 */
	public static void play(String soundsName) {
		tag = true; //���ڵ��ֹͣ��ťʱ�����tag��Ϊfalse
		Sound sound = new Sound(soundsName);
//		Thread thread = new Thread(sound);
//		if (thread == null || !thread.isAlive()) {
//			thread = new Thread(sound);
//			thread.start();
//		}
		new Thread(sound).start();
	}

	// ��ͣ���ֲ���
	/**
	 * @param miunute
	 *            ��ͣ���ֲ��ţ������Է�Ϊ��λ
	 */
	public static synchronized void pause(long miunutes) {
		long second = miunutes * 60;
		final long millis = second << 10; // �൱�ڳ���1024��ʵ����Ӧ�ó�1000
		if (millis <= 0 || Float.isNaN(millis)) {
			throw new IllegalArgumentException("�����쳣" + miunutes);
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
	 * ֹͣ���ֵĲ���
	 */
	public static void stop() {
		tag = false;
		return;
	}

	public static void main(String[] args) {
		Sound.play("dream");
	}
}
