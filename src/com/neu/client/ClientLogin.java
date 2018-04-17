package com.neu.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.neu.util.FileLoad;
import com.neu.util.Sound;


public class ClientLogin extends JFrame{
	
	private static final long serialVersionUID = 2017092610283401L;
	private JTextField username; //用户名
	private JPasswordField password; //密码
	
	/**
	 * Sends a message to the specified IP address, the specified
	 * port number.
	 * The {@code String} represent this host's IP {@code address}.
	 */
	private static String address = "172.28.210.152"; //指定IP地址
	private static int port = 8888; //向指定端口号发送消息

	//加载 配置文件
	FileLoad load = new FileLoad();
	Properties properties = load.loadProperties("client.properties");
	
	public ClientLogin() {
		setTitle("登录");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(350, 250, 450, 300);
		setIconImage(new ImageIcon(properties.getProperty("logo")).getImage());
		JPanel contentPane = new JPanel(){
			//当前时间加上两位的序列号
			private static final long serialVersionUID = 2017092610283401L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//设置登录时的背景
				g.drawImage(new ImageIcon(
						properties.getProperty("login")).getImage(), 
						0, 0, getWidth(), getHeight(), null);
			}
		};
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Sound.play(properties.getProperty("loginMu")); //播放登录时的背景音乐
		JLabel userLab = new JLabel("用户名");
		contentPane.add(userLab);
		userLab.setBounds(80, 140, 104, 21);
		username = new JTextField();
		username.setBounds(128, 140, 104, 21);
		username.setOpaque(false);
		username.setColumns(10);
		contentPane.add(username);

		JLabel pwdLab = new JLabel("密  码");
		pwdLab.setBounds(80, 189, 104, 21);
		contentPane.add(pwdLab);
		password = new JPasswordField();
		password.setForeground(Color.BLACK);
		password.setEchoChar('*');
		password.setOpaque(false);
		password.setBounds(128, 189, 104, 21);
		contentPane.add(password);

		//登录按钮
		final JButton login = new JButton();
		login.setIcon(new ImageIcon("images\\\u767B\u5F55.jpg"));
		login.setBounds(246, 227, 50, 25);
		getRootPane().setDefaultButton(login);
		contentPane.add(login);

		//注册按钮
		final JButton register = new JButton();
		register.setIcon(new ImageIcon("images\\\u6CE8\u518C.jpg"));
		register.setBounds(317, 227, 50, 25);
		contentPane.add(register);

		// 提示信息
		final JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(60, 220, 151, 21);
		lblNewLabel.setForeground(Color.red);
		getContentPane().add(lblNewLabel);
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = username.getText();
				try {
					//定义本机ip地址和端口号，用来接收和发送消息
					//localhost->localhost:127.0.0.1
					Socket socket = new Socket(address, port);
					//使登录按钮失效
					login.setEnabled(false);
					//初始化聊天室界面
					Client client = new Client(name, socket);
					//显示聊天室界面
					client.setVisible(true);
//					Sound.stop();
					//隐藏登录界面
					setVisible(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ClientLogin().setVisible(true);
			}
		});
	}
}
