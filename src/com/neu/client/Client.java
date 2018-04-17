package com.neu.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.neu.bean.FileBean;
import com.neu.bean.MessageBean;
import com.neu.server.Server;
import com.neu.util.FileLoad;
import com.neu.util.LocalAddress;
import com.neu.util.MessageType;
import com.neu.util.SimpleDate;
import com.neu.util.Sound;
import com.neu.util.UnitConversion;

public class Client extends JFrame {
	private static final long serialVersionUID = 2017092616452201L;
	private JPanel roomBackGround; // �����ҵı���ͼƬ
	private Socket clientSocket; // ���û�ip��ַ�Ͷ˿ںŽ��з�װ
	private String username; // �û����û���
	// ����һ���������������clientSocket���������д�������MessageBean������Ϣ
	private ObjectOutputStream oos;
	private JTextArea inputMsg; // ������Ϣ��
	private JTextArea chatingMsg; // ��ʾ�������Ϣ
	private static ListModel<Object> listModel; // ����addressList��ListModel
	private static JList addressList; // ͨѶ¼
	private static Vector<String> onlines; // ��������û�

	/**
	 * The progress of the file transfer.
	 */
	private static JProgressBar fileBar;
	/**
	 * The message of the file transfer.
	 */
	private static JLabel fileLabel;

	private static UnitConversion unitConversion;
	static {
		unitConversion = new UnitConversion(1);
	}

	FileLoad load = new FileLoad();
	Properties properties = load.loadProperties("client.properties");
	/**
	 * �û�Ҫ���͵��ļ���·��
	 */
	// private String path;

	/**
	 * @param username
	 * @param socket
	 *            = new Socket("localhost", 8888)�������socket���������
	 *            �ӵ�¼���洫�ݹ�����ÿ����һ���ʹ���һ��
	 */
	public Client(String uname, Socket socket) {
		Sound.stop();
		// �����ҵĻ�������
		this.clientSocket = socket; //
		this.username = uname;

		onlines = new Vector<>();

		SwingUtilities.updateComponentTreeUI(this);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		setTitle(username);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(200, 100, 688, 510);
		setIconImage(new ImageIcon(properties.getProperty("logo")).getImage());
		// ���������ҵı���
		roomBackGround = new JPanel() {
			private static final long serialVersionUID = 2017092616452202L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(properties.getProperty("chatingRoom")).getImage(), 0, 0, getWidth(),
						getHeight(), null);
			}
		};
		setContentPane(roomBackGround);
		roomBackGround.setLayout(null);
		// ������Ϣ��ʾ����
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setBounds(10, 10, 410, 300);
		// jScrollPane.
		getContentPane().add(jScrollPane);
		// ������ʷ��Ϣ�Ļ�������
		chatingMsg = new JTextArea(); // ��ʾ�������Ϣ
		chatingMsg.setEditable(false); // ���ɱ༭
		chatingMsg.setLineWrap(true); // �Զ�����
		chatingMsg.setWrapStyleWord(true); // ������в����ֹ���
		chatingMsg.setFont(new Font("����", Font.PLAIN, 14));
		jScrollPane.setViewportView(chatingMsg);

		// ��Ϣ��������
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setBounds(10, 347, 411, 97);
		getContentPane().add(jScrollPane1);
		// ��Ϣ���Ϳ�����
		inputMsg = new JTextArea(); // ��Ϣ���Ϳ�
		inputMsg.setLineWrap(true);
		inputMsg.setWrapStyleWord(true);
		jScrollPane1.setViewportView(inputMsg);

		// ��ϵ����������
		listModel = new UUListModel(onlines);
		addressList = new JList<>(listModel); // ͨѶ¼
		addressList.setCellRenderer(new CellRenderer());
		addressList.setOpaque(false); // ���ÿؼ��Ƿ�͸����true��ʾ��͸����false��ʾ͸��
		Border etch = BorderFactory.createEtchedBorder();
		addressList.setBorder(BorderFactory.createTitledBorder(etch, "<" + username + ">" + "���߿ͻ�:",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("sdf", Font.BOLD, 20), Color.green));
		// ��ϵ����ʾ�б�
		JScrollPane addressListScroll = new JScrollPane(addressList);
		addressListScroll.setBounds(430, 10, 245, 375);
		addressListScroll.setOpaque(false);
		addressListScroll.getViewport().setOpaque(false);
		getContentPane().add(addressListScroll);

		// �ļ�����Ľ�����
		fileBar = new JProgressBar();
		fileBar.setMinimum(1);
		fileBar.setMaximum(100);
		fileBar.setBounds(430, 390, 245, 15);
		getContentPane().add(fileBar);

		// �ļ�������ʾ
		fileLabel = new JLabel("�ļ�������ʾ");
		fileLabel.setFont(new Font("����", Font.PLAIN, 12));
		fileLabel.setBackground(Color.WHITE);
		fileLabel.setBounds(430, 410, 245, 15);
		getContentPane().add(fileLabel);
		// ���Ͱ�ť
		JButton sendMsg = new JButton("����");
		sendMsg.setBounds(313, 448, 60, 30);
		getRootPane().setDefaultButton(sendMsg);
		getContentPane().add(sendMsg);
		// ��ʼ���ְ�ť
		JButton playMusic = new JButton("����");
		playMusic.setBounds(430, 448, 60, 30);
		playMusic.setToolTipText("��������");
		getContentPane().add(playMusic);

		// ���Ű�ť
		JButton startMusic = new JButton("��ʼ");
		startMusic.setBounds(430, 448, 60, 30);
		startMusic.setToolTipText("��ʼ����");

		// ��ͣ��������
		JButton pauseMusic = new JButton("��ͣ");
		pauseMusic.setBounds(430, 448, 60, 30);
		pauseMusic.setToolTipText("��ͣ����");

		// ֹͣ�������ְ�ť
		JButton stopMusic = new JButton("ֹͣ");
		stopMusic.setBounds(495, 448, 60, 30);
		stopMusic.setToolTipText("ֹͣ����");
		getContentPane().add(stopMusic);

		// �ڷ��Ͱ�ť������¼�
		sendMsg.addActionListener(new ActionListener() {
			// ��Ҫ���͵���Ϣ�Լ������˵���Ϣ���з�װ
			MessageBean mbBtnSend = new MessageBean();

			@Override
			public void actionPerformed(ActionEvent e) {
				// ��ȡ�б���ѡ�еĿͻ�
				List<String> selectedValuesList = addressList.getSelectedValuesList();
				String input = inputMsg.getText();
				// ���û��ѡ��Ҫ������Ϣ���ˣ��������½���
				if (selectedValuesList.size() <= 0) {
					return;
				}
				if (selectedValuesList.contains("(��)" + username)) {
					JOptionPane.showMessageDialog(getContentPane(), "�������Լ�������Ϣ");
					return;
				}
				if (input == null || input.equals("") || input.length() <= 0) {
					return;
				}
				if (input.contains("go")) {
					Sound.play("go");
				}
				mbBtnSend.setType(MessageType.CHAT); // MessageType.CHAT��������
				mbBtnSend.setMessage(input); // ����û��������Ϣ
				mbBtnSend.setUsername(username); // ��ӷ����˵�����
				mbBtnSend.setTime(SimpleDate.getCurrentTime());
				HashSet<String> h = new HashSet<>();
				// list���뵽hashset��ʱ������Ҫʹ�õ�����
				h.addAll(selectedValuesList); // ��ѡ�еĿͻ����뵽һ��hashset��
				mbBtnSend.setOnlines(h); // ��ѡ�еĿͻ����뵽��ϢBean��
				sendMessage(mbBtnSend); // ����Ϣ���͵�������
				chatingMsg.append("\t" + SimpleDate.getCurrentTime() + "\n[��]: " + input + "\r\n"); // ���Լ�����Ϣ������ʾ�Լ�������Ϣ��Ϣ
				chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // ���ı����λ���ƶ������һ��
				inputMsg.setText(""); // ������Ϣ���Լ���������ÿ�
				inputMsg.requestFocus(); // ���ý��㼯�У�������������һ�·��Ϳ���ܷ�����Ϣ
			}
		});

		// �����ťʱ����ʼ��������
		playMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Sound.play(properties.getProperty("musicMu"));
				getContentPane().add(pauseMusic);
				startMusic.setVisible(false);
				playMusic.setVisible(false);
				pauseMusic.setVisible(true);
			}
		});

		// ����ͣ���ְ�ť������¼�
		pauseMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().add(startMusic);
				pauseMusic.setVisible(false);
				playMusic.setVisible(false);
				startMusic.setVisible(true);
				Sound.pause(1);
			}
		});

		// �����ťʱ��������ͣ������
		startMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().add(pauseMusic);
				startMusic.setVisible(false);
				playMusic.setVisible(false);
				pauseMusic.setVisible(true);
				Sound.start();
			}
		});

		// ��ֹͣ���ְ�ť������¼�
		stopMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseMusic.setVisible(false);
				startMusic.setVisible(false);
				playMusic.setVisible(true);
				Sound.stop();
			}
		});

		// ��ͨѶ¼������¼����û�˫���б�ʱ�������Ի���������ѡ�е��û������ļ�
		addressList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				List<String> selectedUser = addressList.getSelectedValuesList();
				if (e.getClickCount() == 2) {
					if (selectedUser.contains("(��)" + username)) {
						JOptionPane.showMessageDialog(getContentPane(), "�������Լ������ļ�");
						return;
					}
					if (selectedUser.size() <= 0 || selectedUser.isEmpty()) {
						JOptionPane.showMessageDialog(getContentPane(), "��ѡ���û�");
						return;
					}
					// ˫��֮��򿪵ĶԻ���
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("��ѡ���ļ�");
					fileChooser.showDialog(getContentPane(), "ѡ��");
					if (fileChooser.getSelectedFile() != null) {
						// ��ȡ�ļ�·���������߷����ļ���·��
						/*
						 * �����path��������⣬��Ϊ���������������ļ����ڶ������ǰһ����path
						 * ���ǵ�������ǰһ�ζ�ȡ������Ҫ���ļ���
						 */
						// path = fileChooser.getSelectedFile().getPath();
						// Ҫ������ļ�
						File file = new File(fileChooser.getSelectedFile().getPath());
						// ����ļ�Ϊ�ջ򲻴��ڣ�����ʾ
						if (file.length() <= 0 || !file.exists()) {
							JOptionPane.showMessageDialog(getContentPane(), file + "������");
							return;
						}
						// �������ļ�
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.SEND_FILE_REQ); // ������������
						messageBean.setTime(SimpleDate.getCurrentTime()); // ��������ʱ��
						messageBean.setUsername(username); // ������
						messageBean.setMessage(username + "�������ļ�:��" + file.getName() + "����С��"
								+ unitConversion.conversion(file.length()));
						HashSet<String> set = new HashSet<>();
						set.addAll(selectedUser);
						messageBean.setOnlines(set); // ���ø�˭�����ļ�

						// ���÷��͵��ļ�������
						FileBean fileBean = new FileBean();
						fileBean.setFileName(file.getName());
						fileBean.setFileSize(file.length());
						fileBean.setFilePath(fileChooser.getSelectedFile().getPath());
						messageBean.setFileBean(fileBean);

						sendMessage(messageBean);
					}
				}
			}
		});

		// �뿪
		this.addWindowListener(new WindowAdapter() {
			// ׼���رմ���
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int dialog = JOptionPane.showConfirmDialog(getContentPane(), "��ȷ��Ҫ�뿪������ô��");
				if (dialog == 0) {
					MessageBean messageBean = new MessageBean();
					messageBean.setType(MessageType.OFFLINE); // MessageType.OFFLINE��ʾ����
					messageBean.setTime(SimpleDate.getCurrentTime());
					messageBean.setUsername(username); // ����˭������
					sendMessage(messageBean);
				}
			}
		});

		// �������˵���Ϣ���͸�������
		MessageBean messageBean = new MessageBean(); // �洢��¼�û�����Ϣ
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			messageBean.setType(MessageType.ONLINE);
			messageBean.setUsername(username);
			messageBean.setTime(SimpleDate.getCurrentTime());
			oos.writeObject(messageBean);
			oos.flush();
			// �����ͻ�����Ϣ�����߳�
			ClientThread clientThread = new ClientThread();
			new Thread(clientThread).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ����Ϣ���͵�������
	private void sendMessage(MessageBean messageBean) {
		// ��Ҫ���͵���Ϣ�Լ������˵���Ϣ���з�װ
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(messageBean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private static Object object = new Object(); // ����ͬ����������

	class ClientThread implements Runnable {
		private ObjectInputStream ois;

		@Override
		public void run() {
			// ���ϵشӷ�����������Ϣ
			try {
				while (true) {
					ois = new ObjectInputStream(clientSocket.getInputStream());
					MessageBean mb = (MessageBean) ois.readObject();
					switch (mb.getType()) {
					// MessageType.OFFLINE��ʾ��������
					case MessageType.OFFLINE: {
						// return��ʾ�˳���ǰ���������ص��������ó�����finally���ᱻִ��
						return;
					}
					// 1��ʾ�û�����
					case MessageType.ONLINE: {
						onlines.clear();
						HashSet<String> set = mb.getOnlines(); // ��ȡ���������û�
						Iterator<String> iterator = set.iterator();
						while (iterator.hasNext()) {
							String name = iterator.next();
							if (username.equals(name)) {
								onlines.add("(��)" + name); // ����û���
								// onlines.add(name); // ����û���
							} else {
								onlines.add(name); // ����û���
							}
						}
						listModel = new UUListModel(onlines);
						addressList.setModel(listModel);
						chatingMsg.append(
								"\t" + mb.getTime() + "\n[" + mb.getUsername() + "]: " + mb.getMessage() + "\r\n");
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // ���ı����λ���ƶ������һ��
						Sound.play(properties.getProperty("onlineMu")); // ����Ч��ʾ��������
						break;
					}
					// 2��ʾ����
					case MessageType.CHAT: {
						Sound.play(properties.getProperty("chatMu")); // ����Ч��ʾ����Ϣ
						chatingMsg.append(
								"\t" + mb.getTime() + "\n[" + mb.getUsername() + "]: " + mb.getMessage() + "\r\n");
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // ���ı����λ���ƶ������һ��
						break;
					}

					/**
					 * �����ļ�������
					 */
					case MessageType.SEND_FILE_REQ: {
						// ��λ���ʾ--ʱ�䣺A�������ļ���File����С��-
						chatingMsg.append(mb.getTime() + ": " + mb.getMessage() + "\n");
						new Thread() {
							@Override
							public void run() {
								// ��������who�����󣬰���Ϣ���͸�who������Ϣ����A�����onlines����ΪA�����������ѽ�������
								HashSet<String> set = new HashSet<>();
								set.add(mb.getUsername());
								// YES_OPTION = 0, NO_OPTION = 1, CANCEL_OPTION
								// = 2
								int option = JOptionPane.showConfirmDialog(getContentPane(), mb.getMessage());
								switch (option) {
								case 0: {
									JFileChooser fileChooser = new JFileChooser();
									fileChooser.setDialogTitle("�����ļ�"); // ����
									fileChooser.setSelectedFile(new File(mb.getFileBean().getFileName()));
									int dialog = fileChooser.showDialog(getContentPane(), "����");
									// ������ȡ��
									if (1 == dialog) {
										chatingMsg.append("��ȡ�������ļ�" + mb.getFileBean().getFileName() + "\n");
										MessageBean messageBean = new MessageBean();
										messageBean.setType(MessageType.REFUSE_FILE_REQ);
										messageBean.setTime(SimpleDate.getCurrentTime());
										messageBean.setUsername(username);
										messageBean.setMessage("ȡ�������ļ�" + mb.getFileBean().getFileName() + "\n");
										messageBean.setOnlines(set);
										sendMessage(messageBean);
										return;
									}
									// �û������ļ��ı���·��
									String savePath = fileChooser.getSelectedFile().toString();

									MessageBean messageBean = new MessageBean();
									// ACCEPETFILEREQ��������ļ�����
									/*
									 * ������������û����û�����IP��ַ�Ͷ˿ںŵ���Ϣ���͸�������
									 */
									messageBean.setType(MessageType.ACCEPET_FILE_REQ);
									messageBean.setTime(SimpleDate.getCurrentTime());
									messageBean.setUsername(username); // ���ܵ��û�
									messageBean.setMessage(username + "�������ļ�: " + mb.getFileBean().getFileName());
									messageBean.setOnlines(set);
									try {
										// �����µ�TCPͨ�ţ����ڽ����ļ�
										// 0���Ի�ȡ���ж˿�
										ServerSocket serverSocket = new ServerSocket(0);
										FileBean fileBean = new FileBean();
										// ��Ϊ������ͻ�ͬ������ļ������Խ�����IP��ַ���͸������ߣ�
										// �����߾Ϳ��԰������ַ�����ļ����䡣
										fileBean.setIp(LocalAddress.getLocalAddress().getHostAddress());
										// ���ý����ļ��Ķ˿ںţ�IP��ַ����ͨ��username�ӷ�������ȡ��
										fileBean.setPort(serverSocket.getLocalPort());
										fileBean.setFileName(mb.getFileBean().getFileName());
										fileBean.setFileSize(mb.getFileBean().getFileSize());
										fileBean.setFilePath(mb.getFileBean().getFilePath());
										messageBean.setFileBean(fileBean);
										// ���߷����ߣ����Է����ļ���
										sendMessage(messageBean);

										// �������Է����ߵ�������
										Socket socketFile = serverSocket.accept();
										chatingMsg.append(SimpleDate.getCurrentTime() + ":��ʼ��ȡ�ļ� "
												+ mb.getFileBean().getFileName() + "\n");
										// �������ϻ�ȡ������
										DataInputStream dis = new DataInputStream(
												new BufferedInputStream(socketFile.getInputStream()));
										DataOutputStream dos = new DataOutputStream(
												new BufferedOutputStream(new FileOutputStream(savePath)));

										// ���ļ���ȡ������ʾ����
										int count = 0; // ��¼��ǰ���ڶ�ȡ���ֽڣ�ֵ��0-255֮��
										long size = mb.getFileBean().getFileSize(); // �ļ���С
										String unit = unitConversion.conversion(size);
										long t = 0; // ��¼�Ѿ���ȡ���ֽ���
										int bar = 0; // ������
										while ((count = dis.read()) != -1) {
											dos.write(count);
											++t;
											if (t % (size / 100) == 0 && bar < 100) {
												fileBar.setValue(++bar);
											}
											if (t % (1 << 10) == 0 || t == size) {
												fileLabel.setText("��ǰ���أ�" + unitConversion.conversion(t) + "/����" + unit
														+ "������" + bar + "%");
											}
										}
										dos.flush();
										dos.close();
										dis.close();
										socketFile.close();
										serverSocket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
									chatingMsg.append(SimpleDate.getCurrentTime() + "���ļ�"
											+ mb.getFileBean().getFileName() + "������ϣ�����·��Ϊ��" + savePath + "\n");
									chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // ���ı����λ���ƶ������һ��
									Sound.play(properties.getProperty("uploadMu")); // ���������Ч
									break;
								}
								default: {
									MessageBean messageBean = new MessageBean();
									messageBean.setType(MessageType.REFUSE_FILE_REQ);
									messageBean.setTime(SimpleDate.getCurrentTime());
									messageBean.setUsername(username); // who�ܾ���
									messageBean.setMessage("�ܾ������ļ���" + mb.getFileBean().getFileName() + "\n");
									messageBean.setOnlines(set);
									sendMessage(messageBean);
									chatingMsg.append(
											SimpleDate.getCurrentTime() + "����ܾ������ԣ�" + mb.getUsername() + "������\n");
									break;
								}
								}
							}
						}.start();
						break;
					}

					/**
					 * Ŀ���û�Ը������ļ�����ʼ�����ļ� ���ڽ����ļ���ҪIO���������Ը������������������Ҫ�µ��߳�
					 */
					case MessageType.ACCEPET_FILE_REQ: {
						new Thread() {
							@Override
							public void run() {
								try {
									chatingMsg.append(mb.getMessage() + "\n");
									// ������Ŀ���������socketͨ��
									Socket socketAcpFile = new Socket(mb.getFileBean().getIp(),
											mb.getFileBean().getPort());
									// �����path�������ˣ���
									DataInputStream dis = new DataInputStream(
											new FileInputStream(mb.getFileBean().getFilePath()));
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(socketAcpFile.getOutputStream()));
									int count = 0;
									long size = mb.getFileBean().getFileSize(); // �ļ���С
									String unit = unitConversion.conversion(size);
									long t = 0; // ��¼�Ѿ���ȡ���ֽ���
									int bar = 0; // ������
									while ((count = dis.read()) != -1) {
										dos.write(count);
										++t;
										if (t % (size / 100) == 0 && bar < 100) {
											fileBar.setValue(++bar);
										}
										if (t % (1 << 10) == 0 || t == size) {
											fileLabel.setText("��ǰ�ϴ���" + unitConversion.conversion(t) + "/����" + unit
													+ "������" + bar + "%");
										}
									}
									dos.flush();
									dos.close();
									dis.close();
									socketAcpFile.close();
									chatingMsg.append(SimpleDate.getCurrentTime() + "���ļ�"
											+ mb.getFileBean().getFileName() + "�ϴ����\n");
									Sound.play(properties.getProperty("uploadMu")); // �ϴ������Ч
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
						break;
					}
					case MessageType.REFUSE_FILE_REQ: {
						// ʱ�䣺�û�B�ܾ����������
						chatingMsg.append(mb.getTime() + "��" + mb.getUsername() + mb.getMessage() + "\n");
						break;
					}

					// �������˳�
					case MessageType.EXIT: {
						break;
					}
					default:
						break;
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(getContentPane(), "�����������쳣", "�ͻ�����ʾ", JOptionPane.ERROR_MESSAGE);
				System.out.println("�ӷ�����������Ϣʧ��");
				e.printStackTrace();
			} finally {
				close();
				// �˳���ǰ���̣�0��ʾ�����˳�
				System.exit(0);
			}
		}

		// �رտͻ��ˡ�����������
		private void close() {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void start() {
		ClientThread clientThread = new ClientThread();
		Thread thread = new Thread(clientThread);
		thread.start();

	}

	public static void main(String[] args) {
		// new Client().start();
	}
}
