package com.neu.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import com.neu.bean.Message;
import static com.neu.client.Type.*;

import com.neu.controller.MessageController;
import com.neu.controller.OnlineController;
import com.neu.controller.SqlParam;
import com.neu.controller.SqlThread;
import com.neu.controller.UserController;
import com.neu.server.Reader;
import com.neu.util.FileLoad;
import com.neu.util.LocalAddress;
import com.neu.util.SerializeObject;
import com.neu.util.SimpleDate;
import com.neu.util.Sound;

public class Client extends JFrame {
	private static final long serialVersionUID = 2017092616452201L;
	/**
	 * 控制器
	 */
	private OnlineController onlineController = new OnlineController();
	private UserController userController = new UserController();
	private MessageController messageController = new MessageController();
	
	private static List<String> onlines; // 存放在线用户
	private String username; // 用户的用户名
	private Connect connect; //用于连接服务器的类，并且对收到的数据进行处理
	
	private JPanel roomBackGround; // 聊天室的背景图片
	private JTextArea inputMsg; // 输入信息框
	private JTextArea chatingMsg; // 显示聊天的信息
	private static ListModel<String> listModel; // 用于addressList的ListModel
	private static JList<String> addressList; // 通讯录

	private static JProgressBar fileBar; // 文件传输框

	private static JLabel fileLabel; // 文件传输信息

	private FileLoad load = new FileLoad();
	private Properties properties = load.loadProperties("client.properties");
	private ExecutorService exec = Executors.newCachedThreadPool();
	
	/**
	 * @param username
	 */
	public Client(Connect ct, String uname) {
		this.connect = ct;
		this.username = uname;
		Sound.stop();
		// 聊天室的基本设置
		onlines = new ArrayList<>();
		onlines.add(username);
		
		InetAddress inetAddress = LocalAddress.getLocalAddress();
		
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
		// playMusic.setIcon(new ImageIcon("images\\play.png"));
		getContentPane().add(playMusic);

		// 播放按钮
		JButton startMusic = new JButton("开始");
		startMusic.setBounds(430, 448, 60, 30);
		startMusic.setToolTipText("开始音乐");

		// 暂停播放音乐
		JButton pauseMusic = new JButton("暂停");
		pauseMusic.setBounds(430, 448, 60, 30);
		pauseMusic.setToolTipText("暂停音乐");
		// pauseMusic.setIcon(new ImageIcon(properties.getProperty("pause")));

		// 停止播放音乐按钮
		JButton stopMusic = new JButton("停止");
		stopMusic.setBounds(495, 448, 60, 30);
		stopMusic.setToolTipText("停止音乐");
		getContentPane().add(stopMusic);

		//根据用户名获取用户的id
		int userId = userController.selectByName(username).getId();
		// 用户上线更新在线用户表
		onlineController.online(inetAddress.getHostAddress(), 8888, userId);
		{
			//启用监听，即客户端开始接收消息
//			connect.fireListen(true);
			//向服务发送消息，并记录上线人的信息
			Message message = new Message();
			message.setType(ONLINE);
			message.setTime(SimpleDate.currentTime());
			//只有上线和下线时，这里的用户名才指的是上下线的那个人，因为信息接收者是所有人，
			//所以不需要设置特定的接受对象
			message.setUsername(username);
			message.setUser_id(userId); //上线人的用户id
			message.setMessage(username + "上线了");
			connect.sendMessage(message);
			messageController.insert(message);
		}
		// 在发送按钮上添加事件
		sendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputMsg.getText();
				List<String> valuesList = addressList.getSelectedValuesList();
				if(valuesList.size() <= 0)
					return;
				if (valuesList.contains("(我)" + username)) {
					JOptionPane.showMessageDialog(getContentPane(), "不能向自己发送消息");
					return;
				}
				if (input == null || input.equals("") || input.length() <= 0) {
					return;
				}
				if (input.contains("go")) {
					Sound.play("go");
				}
				
				Message message = new Message();
				message.setTime(SimpleDate.currentTime());
				message.setMessage(input);
				message.setType(CHAT);
				message.setUser_id(userId);
				//这个表示选中的列表
				Iterator<String> it = valuesList.iterator();
				//对列表中的用户进行循环
				while(it.hasNext()){
					String s = it.next();
					message.setUsername(s);
					//如果该用户在线，则将消息发送过去
					if(onlines.contains(s)){
						message.setStatus(0);
						connect.sendMessage(message);
						//SqlParam代表使用反射执行controller层的方法时需要使用的参数的定义
						Class<?> [] mClasses = {Message.class};
						SqlParam sqlParam = new SqlParam("insert", messageController, mClasses, message);
						SqlThread<Object> sqlThread = new SqlThread<>();
						sqlThread.executeSql(sqlParam);
						exec.submit(sqlThread);
					}else{
						
					}
				}
				chatingMsg.append("\t" + SimpleDate.currentTime() + "\n[我]: " + input + "\r\n"); // 在自己的信息栏中显示自己发的信息信息
				chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // 将文本框的位置移动到最后一行
				inputMsg.setText(""); // 发送信息后将自己的输入框置空
				inputMsg.requestFocus(); // 设置焦点集中，不用再用鼠标点一下发送框才能发送消息
			}
		});

		// 离开
		this.addWindowListener(new WindowAdapter() {
			// 准备关闭窗口
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int confirm = JOptionPane.showConfirmDialog(getContentPane(), "您确定要离开聊天室么？");
				if (confirm == 0) {
					Message msg = new Message();
					msg.setType(OFFLINE);
					msg.setTime(SimpleDate.currentTime());
					//只有上线和下线时，这里的用户名才指的是上下线的那个人，因为信息接收者是所有人，
					//所以不需要设置特定的接受对象
					msg.setUsername(username); 
					msg.setMessage(username + "下线了");
					msg.setUser_id(userId);
					connect.sendMessage(msg);
					onlineController.offLine(userId);
					messageController.insert(msg);
				}
			}
		});
		
		new Thread(new ClientThread()).start();
	}
	
	class ClientThread extends Thread {
		Reader reader = new Reader();
		SerializeObject so = new SerializeObject();
		SocketChannel sc = connect.getSocketChannel();
		@Override
		public void run() {
			try {
				while(true){
					byte[] bs = reader.read(sc);
					if(bs == null)
						continue;
					Message msg = (Message) so.deserialize(bs);
					int type = msg.getType();
					switch (type) {
					case OFFLINE:{//下线
						return;
					}
					
					case ONLINE:{//上线
						onlines.clear();
						List<Integer> allUid = onlineController.selectAllUid();
						Iterator<String> it = userController.selectListNameById(allUid).iterator();
						
						String name = msg.getUsername();
						while(it.hasNext()){
							String nm = it.next();
							if(nm.equals(name)){
								onlines.add("(我)" + nm);
							}else{
								onlines.add(nm);
							}
						}
						listModel = new UUListModel(onlines);
						addressList.setModel(listModel);
						chatingMsg.append("\t" + msg.getTime() + 
								"\n[" + name + "]: "
						+ msg.getMessage() + "\r\n");
						// 将文本框的位置移动到最后一行
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); 
						Sound.play(properties.getProperty("onlineMu")); // 该音效表示好友上线
						break;
					}
					case CHAT: {
						Sound.play(properties.getProperty("chatMu")); // 该音效表示有消息
						chatingMsg.append(
								"\t" + msg.getTime() + "\n[" + msg.getUsername() + "]: " + msg.getMessage() + "\r\n");
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // 将文本框的位置移动到最后一行
						break;
					}
					
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connect.close();
				System.exit(0);
			}
		}
	}
	
}
