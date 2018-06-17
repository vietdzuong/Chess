package menu;

import java.awt.CardLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class menuFrame extends JFrame {

	private JPanel contentPane;

	
	/**
	 * Create the frame.
	 */
	public menuFrame() {
		
		setIconImage(new ImageIcon( getClass().getResource("/images/logo.png")).getImage());
		setLocation(330, 100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new CardLayout(0, 0));
		getContentPane().add(new menu(this), "cardMenu");
		getContentPane().add(new menuTwoPlayer(this), "cardTwoPlayer");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

}
