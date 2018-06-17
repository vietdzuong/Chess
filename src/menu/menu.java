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
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class menu extends JPanel{
	
	private Image background;
	private JButton btn1, btn2;
	private JFrame frame;
	
	private CardLayout cl;
	public menu(JFrame frame) {
	
		cl = (CardLayout) frame.getContentPane().getLayout();
		this.frame = frame;
		setLayout(null);
		URL urlBackgroundImg = getClass().getResource("/images/menubg.png");
		
		this.background = new ImageIcon(urlBackgroundImg).getImage();
		
		this.setPreferredSize(new Dimension(background.getWidth(null)-10,
				background.getHeight(null)-10));
		addControls();
		addEvents();
		
		/*JFrame f = new JFrame();
		f.setLocation(330, 100);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this, BorderLayout.CENTER);
	
		f.setResizable(false);
		f.pack();*/
	}
	private void addControls() {
		
		btn1 = new JButton("Single Player");
		btn1.setBounds(90, 280, 200, 40);
		this.add(btn1);
		
		btn2 = new JButton("Two Player");
		btn2.setBounds(90, 330, 200, 40);
		this.add(btn2);
		
		JLabel title = new JLabel("Chess Menu");
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
				chooseSingle();
			}
		});
		btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseTwo();
			}
		});
	
	}
	protected void chooseSingle() {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					  for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
                          if(info.getName().equals("Nimbus")){
                              UIManager.setLookAndFeel(info.getClassName());
                              break;
                          }
                      }
					setupAI frame = new setupAI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	protected void chooseTwo() {
		
		cl.show(frame.getContentPane(), "cardTwoPlayer");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.background, 0, 0, null);
	}
	


}
