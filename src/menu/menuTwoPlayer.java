package menu;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import twoplayer.gui.ChessGui;
import twoplayer.logic.ChessGame;

public class menuTwoPlayer extends JPanel {
	private Image background;
	private JButton btn1, btn2, btn3;
	private JLabel title;
	private JFrame frame;
	public menuTwoPlayer(JFrame frame) {
	
		this.frame = frame;
		
		setLayout(null);
		URL urlBackgroundImg = getClass().getResource("/images/menubg.png");
		
		this.background = new ImageIcon(urlBackgroundImg).getImage();
		
		this.setPreferredSize(new Dimension(background.getWidth(null)-10,
				background.getHeight(null)-10));
		addControls();
		addEvents();
	
	}
	private void addControls() {
		
		btn1 = new JButton("Create room");
		btn1.setBounds(90, 280, 200, 40);
		this.add(btn1);
		
		btn2 = new JButton("Join room");
		btn2.setBounds(90, 330, 200, 40);
		this.add(btn2);
		
		btn3 = new JButton("Back to menu");
		btn3.setBounds(90, 380, 200, 40);
		this.add(btn3);
		
		title = new JLabel("Two player");
		title.setFont(new Font("Tahoma", Font.ITALIC, 28));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(90, 10, 200, 40);
		this.add(title);
		
	}
	private void addEvents() {
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createRoom();
			}
		});
		btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				joinRoom();
			}
		});
		btn3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
				cl.show(frame.getContentPane(), "cardMenu");
			}
		});
	
	}
	
	
	protected void joinRoom() {
		final String serverHost = "localhost";
		 
	    Socket socketOfClient = null;
	    ObjectInputStream is;
	    ObjectOutputStream os;

	       try {
	           // Gửi yêu cầu kết nối tới Server đang lắng nghe
	           // trên máy 'localhost' cổng 9999.
	           socketOfClient = new Socket(serverHost, 9999);
	           // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
	           os = new ObjectOutputStream(socketOfClient.getOutputStream());
	           is = new ObjectInputStream(socketOfClient.getInputStream());
	         
	           
	           EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							
							new ChessGui(os, is,ChessGame.TEAM_BLACK);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	 
	       } catch (UnknownHostException e) {
	           System.err.println("Don't know about host " + serverHost);
	           return;
	       } catch (IOException e) {
	           System.err.println("Couldn't get I/O for the connection to " + serverHost);
	           return;
	       }
		
	}
	protected void createRoom() {
		title.setText("Connecting...");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					createServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void createServer() {
		   ServerSocket listener = null;
	       
	       ObjectInputStream is;
	       ObjectOutputStream os;
	       Socket socketOfServer = null;

	       try {
	           listener = new ServerSocket(9999);
	       } catch (IOException e) {
	           System.out.println(e);
	           System.exit(1);
	       }
	       System.out.println("Server is waiting to accept user...");
	 
	       try {
				socketOfServer = listener.accept();
				os = new ObjectOutputStream(socketOfServer.getOutputStream());
		        is = new ObjectInputStream(socketOfServer.getInputStream());
		        System.out.println("Accept a client!");
			   
		        new ChessGui(os, is,ChessGame.TEAM_WHITE);
			   
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.background, 0, 0, null);
	}
}
