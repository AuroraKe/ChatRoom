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
	private JPanel roomBackGround; // 聊天室的背景图片
	private Socket clientSocket; // 对用户ip地址和端口号进行封装
	private String username; // 用户的用户名
	// 定义一个对象输出流，向clientSocket的输出流中写入对象，用MessageBean传递消息
	private ObjectOutputStream oos;
	private JTextArea inputMsg; // 输入信息框
	private JTextArea chatingMsg; // 显示聊天的信息
	private static ListModel<Object> listModel; // 用于addressList的ListModel
	private static JList addressList; // 通讯录
	private static Vector<String> onlines; // 存放在线用户

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
	 * 用户要发送的文件的路径
	 */
	// private String path;

	/**
	 * @param username
	 * @param socket
	 *            = new Socket("localhost", 8888)，这里的socket就是这个，
	 *            从登录界面传递过来，每启动一个就创建一个
	 */
	public Client(String uname, Socket socket) {
		Sound.stop();
		// 聊天室的基本设置
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
		// 设置聊天室的背景
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
		// 聊天信息显示区域
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setBounds(10, 10, 410, 300);
		// jScrollPane.
		getContentPane().add(jScrollPane);
		// 聊天历史信息的基本设置
		chatingMsg = new JTextArea(); // 显示聊天的信息
		chatingMsg.setEditable(false); // 不可编辑
		chatingMsg.setLineWrap(true); // 自动换行
		chatingMsg.setWrapStyleWord(true); // 激活断行不断字功能
		chatingMsg.setFont(new Font("仿宋", Font.PLAIN, 14));
		jScrollPane.setViewportView(chatingMsg);

		// 信息输入区域
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setBounds(10, 347, 411, 97);
		getContentPane().add(jScrollPane1);
		// 信息发送框设置
		inputMsg = new JTextArea(); // 信息发送框
		inputMsg.setLineWrap(true);
		inputMsg.setWrapStyleWord(true);
		jScrollPane1.setViewportView(inputMsg);

		// 联系人设置区域
		listModel = new UUListModel(onlines);
		addressList = new JList<>(listModel); // 通讯录
		addressList.setCellRenderer(new CellRenderer());
		addressList.setOpaque(false); // 设置控件是否透明，true表示不透明，false表示透明
		Border etch = BorderFactory.createEtchedBorder();
		addressList.setBorder(BorderFactory.createTitledBorder(etch, "<" + username + ">" + "在线客户:",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("sdf", Font.BOLD, 20), Color.green));
		// 联系人显示列表
		JScrollPane addressListScroll = new JScrollPane(addressList);
		addressListScroll.setBounds(430, 10, 245, 375);
		addressListScroll.setOpaque(false);
		addressListScroll.getViewport().setOpaque(false);
		getContentPane().add(addressListScroll);

		// 文件传输的进度条
		fileBar = new JProgressBar();
		fileBar.setMinimum(1);
		fileBar.setMaximum(100);
		fileBar.setBounds(430, 390, 245, 15);
		getContentPane().add(fileBar);

		// 文件传输提示
		fileLabel = new JLabel("文件传输提示");
		fileLabel.setFont(new Font("宋体", Font.PLAIN, 12));
		fileLabel.setBackground(Color.WHITE);
		fileLabel.setBounds(430, 410, 245, 15);
		getContentPane().add(fileLabel);
		// 发送按钮
		JButton sendMsg = new JButton("发送");
		sendMsg.setBounds(313, 448, 60, 30);
		getRootPane().setDefaultButton(sendMsg);
		getContentPane().add(sendMsg);
		// 开始音乐按钮
		JButton playMusic = new JButton("播放");
		playMusic.setBounds(430, 448, 60, 30);
		playMusic.setToolTipText("播放音乐");
		getContentPane().add(playMusic);

		// 播放按钮
		JButton startMusic = new JButton("开始");
		startMusic.setBounds(430, 448, 60, 30);
		startMusic.setToolTipText("开始音乐");

		// 暂停播放音乐
		JButton pauseMusic = new JButton("暂停");
		pauseMusic.setBounds(430, 448, 60, 30);
		pauseMusic.setToolTipText("暂停音乐");

		// 停止播放音乐按钮
		JButton stopMusic = new JButton("停止");
		stopMusic.setBounds(495, 448, 60, 30);
		stopMusic.setToolTipText("停止音乐");
		getContentPane().add(stopMusic);

		// 在发送按钮上添加事件
		sendMsg.addActionListener(new ActionListener() {
			// 将要发送的信息以及接收人的信息进行封装
			MessageBean mbBtnSend = new MessageBean();

			@Override
			public void actionPerformed(ActionEvent e) {
				// 读取列表中选中的客户
				List<String> selectedValuesList = addressList.getSelectedValuesList();
				String input = inputMsg.getText();
				// 如果没有选择要发送消息的人，则不在往下进行
				if (selectedValuesList.size() <= 0) {
					return;
				}
				if (selectedValuesList.contains("(我)" + username)) {
					JOptionPane.showMessageDialog(getContentPane(), "不能向自己发送消息");
					return;
				}
				if (input == null || input.equals("") || input.length() <= 0) {
					return;
				}
				if (input.contains("go")) {
					Sound.play("go");
				}
				mbBtnSend.setType(MessageType.CHAT); // MessageType.CHAT代表聊天
				mbBtnSend.setMessage(input); // 添加用户输入的信息
				mbBtnSend.setUsername(username); // 添加发送人的姓名
				mbBtnSend.setTime(SimpleDate.getCurrentTime());
				HashSet<String> h = new HashSet<>();
				// list加入到hashset中时，不需要使用迭代器
				h.addAll(selectedValuesList); // 将选中的客户加入到一个hashset中
				mbBtnSend.setOnlines(h); // 将选中的客户加入到消息Bean里
				sendMessage(mbBtnSend); // 将消息发送到服务器
				chatingMsg.append("\t" + SimpleDate.getCurrentTime() + "\n[我]: " + input + "\r\n"); // 在自己的信息栏中显示自己发的信息信息
				chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // 将文本框的位置移动到最后一行
				inputMsg.setText(""); // 发送信息后将自己的输入框置空
				inputMsg.requestFocus(); // 设置焦点集中，不用再用鼠标点一下发送框才能发送消息
			}
		});

		// 点击按钮时，开始播放音乐
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

		// 在暂停音乐按钮上添加事件
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

		// 点击按钮时，播放暂停的音乐
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

		// 在停止音乐按钮上添加事件
		stopMusic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseMusic.setVisible(false);
				startMusic.setVisible(false);
				playMusic.setVisible(true);
				Sound.stop();
			}
		});

		// 在通讯录上添加事件当用户双击列表时，弹出对话框，用于向选中的用户发送文件
		addressList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				List<String> selectedUser = addressList.getSelectedValuesList();
				if (e.getClickCount() == 2) {
					if (selectedUser.contains("(我)" + username)) {
						JOptionPane.showMessageDialog(getContentPane(), "不能向自己发送文件");
						return;
					}
					if (selectedUser.size() <= 0 || selectedUser.isEmpty()) {
						JOptionPane.showMessageDialog(getContentPane(), "请选择用户");
						return;
					}
					// 双加之后打开的对话框
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("请选择文件");
					fileChooser.showDialog(getContentPane(), "选择");
					if (fileChooser.getSelectedFile() != null) {
						// 获取文件路径，发送者发送文件的路径
						/*
						 * 这里的path会出现问题，因为在连续发送两个文件，第二个会把前一个的path
						 * 覆盖掉，导致前一次读取不到想要的文件。
						 */
						// path = fileChooser.getSelectedFile().getPath();
						// 要传输的文件
						File file = new File(fileChooser.getSelectedFile().getPath());
						// 如果文件为空或不存在，则提示
						if (file.length() <= 0 || !file.exists()) {
							JOptionPane.showMessageDialog(getContentPane(), file + "不存在");
							return;
						}
						// 请求发送文件
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.SEND_FILE_REQ); // 设置请求类型
						messageBean.setTime(SimpleDate.getCurrentTime()); // 设置请求时间
						messageBean.setUsername(username); // 请求人
						messageBean.setMessage(username + "请求发送文件:：" + file.getName() + "，大小："
								+ unitConversion.conversion(file.length()));
						HashSet<String> set = new HashSet<>();
						set.addAll(selectedUser);
						messageBean.setOnlines(set); // 设置给谁发送文件

						// 设置发送的文件的属性
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

		// 离开
		this.addWindowListener(new WindowAdapter() {
			// 准备关闭窗口
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int dialog = JOptionPane.showConfirmDialog(getContentPane(), "您确定要离开聊天室么？");
				if (dialog == 0) {
					MessageBean messageBean = new MessageBean();
					messageBean.setType(MessageType.OFFLINE); // MessageType.OFFLINE表示下线
					messageBean.setTime(SimpleDate.getCurrentTime());
					messageBean.setUsername(username); // 设置谁下线了
					sendMessage(messageBean);
				}
			}
		});

		// 将上线人的信息发送给服务器
		MessageBean messageBean = new MessageBean(); // 存储登录用户的信息
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			messageBean.setType(MessageType.ONLINE);
			messageBean.setUsername(username);
			messageBean.setTime(SimpleDate.getCurrentTime());
			oos.writeObject(messageBean);
			oos.flush();
			// 启动客户端信息接收线程
			ClientThread clientThread = new ClientThread();
			new Thread(clientThread).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 将消息发送到服务器
	private void sendMessage(MessageBean messageBean) {
		// 将要发送的信息以及接收人的信息进行封装
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(messageBean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private static Object object = new Object(); // 用于同步代码块加锁

	class ClientThread implements Runnable {
		private ObjectInputStream ois;

		@Override
		public void run() {
			// 不断地从服务器接收消息
			try {
				while (true) {
					ois = new ObjectInputStream(clientSocket.getInputStream());
					MessageBean mb = (MessageBean) ois.readObject();
					switch (mb.getType()) {
					// MessageType.OFFLINE表示下线请求
					case MessageType.OFFLINE: {
						// return表示退出当前函数，返回到函数调用出，但finally语句会被执行
						return;
					}
					// 1表示用户上线
					case MessageType.ONLINE: {
						onlines.clear();
						HashSet<String> set = mb.getOnlines(); // 获取所有在线用户
						Iterator<String> iterator = set.iterator();
						while (iterator.hasNext()) {
							String name = iterator.next();
							if (username.equals(name)) {
								onlines.add("(我)" + name); // 添加用户名
								// onlines.add(name); // 添加用户名
							} else {
								onlines.add(name); // 添加用户名
							}
						}
						listModel = new UUListModel(onlines);
						addressList.setModel(listModel);
						chatingMsg.append(
								"\t" + mb.getTime() + "\n[" + mb.getUsername() + "]: " + mb.getMessage() + "\r\n");
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // 将文本框的位置移动到最后一行
						Sound.play(properties.getProperty("onlineMu")); // 该音效表示好友上线
						break;
					}
					// 2表示聊天
					case MessageType.CHAT: {
						Sound.play(properties.getProperty("chatMu")); // 该音效表示有消息
						chatingMsg.append(
								"\t" + mb.getTime() + "\n[" + mb.getUsername() + "]: " + mb.getMessage() + "\r\n");
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // 将文本框的位置移动到最后一行
						break;
					}

					/**
					 * 发送文件的请求
					 */
					case MessageType.SEND_FILE_REQ: {
						// 这段话表示--时间：A请求发送文件：File，大小：-
						chatingMsg.append(mb.getTime() + ": " + mb.getMessage() + "\n");
						new Thread() {
							@Override
							public void run() {
								// 接受来自who的请求，把消息发送给who（即消息来自A，则把onlines设置为A，告诉他我已接受请求）
								HashSet<String> set = new HashSet<>();
								set.add(mb.getUsername());
								// YES_OPTION = 0, NO_OPTION = 1, CANCEL_OPTION
								// = 2
								int option = JOptionPane.showConfirmDialog(getContentPane(), mb.getMessage());
								switch (option) {
								case 0: {
									JFileChooser fileChooser = new JFileChooser();
									fileChooser.setDialogTitle("保存文件"); // 标题
									fileChooser.setSelectedFile(new File(mb.getFileBean().getFileName()));
									int dialog = fileChooser.showDialog(getContentPane(), "保存");
									// 如果点击取消
									if (1 == dialog) {
										chatingMsg.append("你取消接收文件" + mb.getFileBean().getFileName() + "\n");
										MessageBean messageBean = new MessageBean();
										messageBean.setType(MessageType.REFUSE_FILE_REQ);
										messageBean.setTime(SimpleDate.getCurrentTime());
										messageBean.setUsername(username);
										messageBean.setMessage("取消接收文件" + mb.getFileBean().getFileName() + "\n");
										messageBean.setOnlines(set);
										sendMessage(messageBean);
										return;
									}
									// 用户接收文件的保存路径
									String savePath = fileChooser.getSelectedFile().toString();

									MessageBean messageBean = new MessageBean();
									// ACCEPETFILEREQ代表接受文件请求
									/*
									 * 将接受请求的用户的用户名，IP地址和端口号等信息发送给发送者
									 */
									messageBean.setType(MessageType.ACCEPET_FILE_REQ);
									messageBean.setTime(SimpleDate.getCurrentTime());
									messageBean.setUsername(username); // 接受的用户
									messageBean.setMessage(username + "接收了文件: " + mb.getFileBean().getFileName());
									messageBean.setOnlines(set);
									try {
										// 创建新的TCP通信，用于接收文件
										// 0可以获取空闲端口
										ServerSocket serverSocket = new ServerSocket(0);
										FileBean fileBean = new FileBean();
										// 因为是这个客户同意接受文件，所以将他的IP地址发送给发送者，
										// 发送者就可以按这个地址进行文件传输。
										fileBean.setIp(LocalAddress.getLocalAddress().getHostAddress());
										// 设置接收文件的端口号，IP地址可以通过username从服务器端取得
										fileBean.setPort(serverSocket.getLocalPort());
										fileBean.setFileName(mb.getFileBean().getFileName());
										fileBean.setFileSize(mb.getFileBean().getFileSize());
										fileBean.setFilePath(mb.getFileBean().getFilePath());
										messageBean.setFileBean(fileBean);
										// 告诉发送者，可以发送文件了
										sendMessage(messageBean);

										// 监听来自发送者的数据流
										Socket socketFile = serverSocket.accept();
										chatingMsg.append(SimpleDate.getCurrentTime() + ":开始读取文件 "
												+ mb.getFileBean().getFileName() + "\n");
										// 从网络上获取输入流
										DataInputStream dis = new DataInputStream(
												new BufferedInputStream(socketFile.getInputStream()));
										DataOutputStream dos = new DataOutputStream(
												new BufferedOutputStream(new FileOutputStream(savePath)));

										// 对文件读取，并显示进度
										int count = 0; // 记录当前正在读取的字节，值在0-255之间
										long size = mb.getFileBean().getFileSize(); // 文件大小
										String unit = unitConversion.conversion(size);
										long t = 0; // 记录已经读取的字节数
										int bar = 0; // 进度条
										while ((count = dis.read()) != -1) {
											dos.write(count);
											++t;
											if (t % (size / 100) == 0 && bar < 100) {
												fileBar.setValue(++bar);
											}
											if (t % (1 << 10) == 0 || t == size) {
												fileLabel.setText("当前下载：" + unitConversion.conversion(t) + "/共有" + unit
														+ "，整体" + bar + "%");
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
									chatingMsg.append(SimpleDate.getCurrentTime() + "：文件"
											+ mb.getFileBean().getFileName() + "保存完毕，保存路径为：" + savePath + "\n");
									chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // 将文本框的位置移动到最后一行
									Sound.play(properties.getProperty("uploadMu")); // 下载完毕音效
									break;
								}
								default: {
									MessageBean messageBean = new MessageBean();
									messageBean.setType(MessageType.REFUSE_FILE_REQ);
									messageBean.setTime(SimpleDate.getCurrentTime());
									messageBean.setUsername(username); // who拒绝了
									messageBean.setMessage("拒绝接收文件：" + mb.getFileBean().getFileName() + "\n");
									messageBean.setOnlines(set);
									sendMessage(messageBean);
									chatingMsg.append(
											SimpleDate.getCurrentTime() + "：你拒绝了来自：" + mb.getUsername() + "的请求\n");
									break;
								}
								}
							}
						}.start();
						break;
					}

					/**
					 * 目标用户愿意接收文件，开始发送文件 由于接收文件需要IO操作，所以更能造成阻塞，这里需要新的线程
					 */
					case MessageType.ACCEPET_FILE_REQ: {
						new Thread() {
							@Override
							public void run() {
								try {
									chatingMsg.append(mb.getMessage() + "\n");
									// 创建与目标机相连的socket通信
									Socket socketAcpFile = new Socket(mb.getFileBean().getIp(),
											mb.getFileBean().getPort());
									// 这里的path出问题了，在
									DataInputStream dis = new DataInputStream(
											new FileInputStream(mb.getFileBean().getFilePath()));
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(socketAcpFile.getOutputStream()));
									int count = 0;
									long size = mb.getFileBean().getFileSize(); // 文件大小
									String unit = unitConversion.conversion(size);
									long t = 0; // 记录已经读取的字节数
									int bar = 0; // 进度条
									while ((count = dis.read()) != -1) {
										dos.write(count);
										++t;
										if (t % (size / 100) == 0 && bar < 100) {
											fileBar.setValue(++bar);
										}
										if (t % (1 << 10) == 0 || t == size) {
											fileLabel.setText("当前上传：" + unitConversion.conversion(t) + "/共有" + unit
													+ "，整体" + bar + "%");
										}
									}
									dos.flush();
									dos.close();
									dis.close();
									socketAcpFile.close();
									chatingMsg.append(SimpleDate.getCurrentTime() + "：文件"
											+ mb.getFileBean().getFileName() + "上传完毕\n");
									Sound.play(properties.getProperty("uploadMu")); // 上传完毕音效
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
						break;
					}
					case MessageType.REFUSE_FILE_REQ: {
						// 时间：用户B拒绝了你的请求
						chatingMsg.append(mb.getTime() + "：" + mb.getUsername() + mb.getMessage() + "\n");
						break;
					}

					// 非正常退出
					case MessageType.EXIT: {
						break;
					}
					default:
						break;
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(getContentPane(), "服务器连接异常", "客户端提示", JOptionPane.ERROR_MESSAGE);
				System.out.println("从服务器接收消息失败");
				e.printStackTrace();
			} finally {
				close();
				// 退出当前进程，0表示正常退出
				System.exit(0);
			}
		}

		// 关闭客户端、对象输入流
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
