package menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

public class winner extends JPanel {
	private JFrame f;
	private JLabel state;
	private JButton close;
	private Image background;
	public winner(){
		
		URL urlBackgroundImg = getClass().getResource("/images/winner.png");
		this.background = new ImageIcon(urlBackgroundImg).getImage();
	
		this.setPreferredSize(new Dimension(background.getWidth(null)-10,
				background.getHeight(null)-10));
		setLayout(null);
		addControls();
		addEvents();
		f = new JFrame();
		f.setLocation(450, 100);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this, BorderLayout.CENTER);
		f.setIconImage(new ImageIcon( getClass().getResource("/images/logo.png")).getImage());
		f.setResizable(false);
		f.pack();
	}
	private void addEvents() {
		
		
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				f.dispose();
				
			}
		});
		
	}
	private void addControls() {
		close = new JButton("Close");
		close.setBounds(180, 250, 70, 30);
		this.add(close);
		
		state = new JLabel("White win !!!");
		state.setFont(new Font("Tahoma", Font.ITALIC, 28));
		state.setForeground(Color.WHITE);
		state.setHorizontalAlignment(SwingConstants.CENTER);
		
		state.setBounds(10, 20, 200, 150);
		this.add(state);
		
	}
	public void setWinner(String text){
		state.setText(text);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.background, 0, 0, null);
	}
}
