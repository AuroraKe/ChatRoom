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
	 * ������
	 */
	private OnlineController onlineController = new OnlineController();
	private UserController userController = new UserController();
	private MessageController messageController = new MessageController();
	
	private static List<String> onlines; // ��������û�
	private String username; // �û����û���
	private Connect connect; //�������ӷ��������࣬���Ҷ��յ������ݽ��д���
	
	private JPanel roomBackGround; // �����ҵı���ͼƬ
	private JTextArea inputMsg; // ������Ϣ��
	private JTextArea chatingMsg; // ��ʾ�������Ϣ
	private static ListModel<String> listModel; // ����addressList��ListModel
	private static JList<String> addressList; // ͨѶ¼

	private static JProgressBar fileBar; // �ļ������

	private static JLabel fileLabel; // �ļ�������Ϣ

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
		// �����ҵĻ�������
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
		// playMusic.setIcon(new ImageIcon("images\\play.png"));
		getContentPane().add(playMusic);

		// ���Ű�ť
		JButton startMusic = new JButton("��ʼ");
		startMusic.setBounds(430, 448, 60, 30);
		startMusic.setToolTipText("��ʼ����");

		// ��ͣ��������
		JButton pauseMusic = new JButton("��ͣ");
		pauseMusic.setBounds(430, 448, 60, 30);
		pauseMusic.setToolTipText("��ͣ����");
		// pauseMusic.setIcon(new ImageIcon(properties.getProperty("pause")));

		// ֹͣ�������ְ�ť
		JButton stopMusic = new JButton("ֹͣ");
		stopMusic.setBounds(495, 448, 60, 30);
		stopMusic.setToolTipText("ֹͣ����");
		getContentPane().add(stopMusic);

		//�����û�����ȡ�û���id
		int userId = userController.selectByName(username).getId();
		// �û����߸��������û���
		onlineController.online(inetAddress.getHostAddress(), 8888, userId);
		{
			//���ü��������ͻ��˿�ʼ������Ϣ
//			connect.fireListen(true);
			//���������Ϣ������¼�����˵���Ϣ
			Message message = new Message();
			message.setType(ONLINE);
			message.setTime(SimpleDate.currentTime());
			//ֻ�����ߺ�����ʱ��������û�����ָ���������ߵ��Ǹ��ˣ���Ϊ��Ϣ�������������ˣ�
			//���Բ���Ҫ�����ض��Ľ��ܶ���
			message.setUsername(username);
			message.setUser_id(userId); //�����˵��û�id
			message.setMessage(username + "������");
			connect.sendMessage(message);
			messageController.insert(message);
		}
		// �ڷ��Ͱ�ť������¼�
		sendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputMsg.getText();
				List<String> valuesList = addressList.getSelectedValuesList();
				if(valuesList.size() <= 0)
					return;
				if (valuesList.contains("(��)" + username)) {
					JOptionPane.showMessageDialog(getContentPane(), "�������Լ�������Ϣ");
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
				//�����ʾѡ�е��б�
				Iterator<String> it = valuesList.iterator();
				//���б��е��û�����ѭ��
				while(it.hasNext()){
					String s = it.next();
					message.setUsername(s);
					//������û����ߣ�����Ϣ���͹�ȥ
					if(onlines.contains(s)){
						message.setStatus(0);
						connect.sendMessage(message);
						//SqlParam����ʹ�÷���ִ��controller��ķ���ʱ��Ҫʹ�õĲ����Ķ���
						Class<?> [] mClasses = {Message.class};
						SqlParam sqlParam = new SqlParam("insert", messageController, mClasses, message);
						SqlThread<Object> sqlThread = new SqlThread<>();
						sqlThread.executeSql(sqlParam);
						exec.submit(sqlThread);
					}else{
						
					}
				}
				chatingMsg.append("\t" + SimpleDate.currentTime() + "\n[��]: " + input + "\r\n"); // ���Լ�����Ϣ������ʾ�Լ�������Ϣ��Ϣ
				chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // ���ı����λ���ƶ������һ��
				inputMsg.setText(""); // ������Ϣ���Լ���������ÿ�
				inputMsg.requestFocus(); // ���ý��㼯�У�������������һ�·��Ϳ���ܷ�����Ϣ
			}
		});

		// �뿪
		this.addWindowListener(new WindowAdapter() {
			// ׼���رմ���
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int confirm = JOptionPane.showConfirmDialog(getContentPane(), "��ȷ��Ҫ�뿪������ô��");
				if (confirm == 0) {
					Message msg = new Message();
					msg.setType(OFFLINE);
					msg.setTime(SimpleDate.currentTime());
					//ֻ�����ߺ�����ʱ��������û�����ָ���������ߵ��Ǹ��ˣ���Ϊ��Ϣ�������������ˣ�
					//���Բ���Ҫ�����ض��Ľ��ܶ���
					msg.setUsername(username); 
					msg.setMessage(username + "������");
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
					case OFFLINE:{//����
						return;
					}
					
					case ONLINE:{//����
						onlines.clear();
						List<Integer> allUid = onlineController.selectAllUid();
						Iterator<String> it = userController.selectListNameById(allUid).iterator();
						
						String name = msg.getUsername();
						while(it.hasNext()){
							String nm = it.next();
							if(nm.equals(name)){
								onlines.add("(��)" + nm);
							}else{
								onlines.add(nm);
							}
						}
						listModel = new UUListModel(onlines);
						addressList.setModel(listModel);
						chatingMsg.append("\t" + msg.getTime() + 
								"\n[" + name + "]: "
						+ msg.getMessage() + "\r\n");
						// ���ı����λ���ƶ������һ��
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); 
						Sound.play(properties.getProperty("onlineMu")); // ����Ч��ʾ��������
						break;
					}
					case CHAT: {
						Sound.play(properties.getProperty("chatMu")); // ����Ч��ʾ����Ϣ
						chatingMsg.append(
								"\t" + msg.getTime() + "\n[" + msg.getUsername() + "]: " + msg.getMessage() + "\r\n");
						chatingMsg.setCaretPosition(chatingMsg.getDocument().getLength()); // ���ı����λ���ƶ������һ��
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
