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
	private JTextField username; //�û���
	private JPasswordField password; //����
	
	/**
	 * Sends a message to the specified IP address, the specified
	 * port number.
	 * The {@code String} represent this host's IP {@code address}.
	 */
	private static String address = "172.28.210.152"; //ָ��IP��ַ
	private static int port = 8888; //��ָ���˿ںŷ�����Ϣ

	//���� �����ļ�
	FileLoad load = new FileLoad();
	Properties properties = load.loadProperties("client.properties");
	
	public ClientLogin() {
		setTitle("��¼");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(350, 250, 450, 300);
		setIconImage(new ImageIcon(properties.getProperty("logo")).getImage());
		JPanel contentPane = new JPanel(){
			//��ǰʱ�������λ�����к�
			private static final long serialVersionUID = 2017092610283401L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//���õ�¼ʱ�ı���
				g.drawImage(new ImageIcon(
						properties.getProperty("login")).getImage(), 
						0, 0, getWidth(), getHeight(), null);
			}
		};
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Sound.play(properties.getProperty("loginMu")); //���ŵ�¼ʱ�ı�������
		JLabel userLab = new JLabel("�û���");
		contentPane.add(userLab);
		userLab.setBounds(80, 140, 104, 21);
		username = new JTextField();
		username.setBounds(128, 140, 104, 21);
		username.setOpaque(false);
		username.setColumns(10);
		contentPane.add(username);

		JLabel pwdLab = new JLabel("��  ��");
		pwdLab.setBounds(80, 189, 104, 21);
		contentPane.add(pwdLab);
		password = new JPasswordField();
		password.setForeground(Color.BLACK);
		password.setEchoChar('*');
		password.setOpaque(false);
		password.setBounds(128, 189, 104, 21);
		contentPane.add(password);

		//��¼��ť
		final JButton login = new JButton();
		login.setIcon(new ImageIcon("images\\\u767B\u5F55.jpg"));
		login.setBounds(246, 227, 50, 25);
		getRootPane().setDefaultButton(login);
		contentPane.add(login);

		//ע�ᰴť
		final JButton register = new JButton();
		register.setIcon(new ImageIcon("images\\\u6CE8\u518C.jpg"));
		register.setBounds(317, 227, 50, 25);
		contentPane.add(register);

		// ��ʾ��Ϣ
		final JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(60, 220, 151, 21);
		lblNewLabel.setForeground(Color.red);
		getContentPane().add(lblNewLabel);
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = username.getText();
				try {
					//���屾��ip��ַ�Ͷ˿ںţ��������պͷ�����Ϣ
					//localhost->localhost:127.0.0.1
					Socket socket = new Socket(address, port);
					//ʹ��¼��ťʧЧ
					login.setEnabled(false);
					//��ʼ�������ҽ���
					Client client = new Client(name, socket);
					//��ʾ�����ҽ���
					client.setVisible(true);
//					Sound.stop();
					//���ص�¼����
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