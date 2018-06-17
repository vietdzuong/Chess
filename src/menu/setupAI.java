package menu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import singleplayer.ai.AI;
import singleplayer.gui.ChessGui;
import singleplayer.logic.ChessGame;
import singleplayer.logic.Piece;

public class setupAI extends JFrame {

    JSlider level;
    JRadioButton white_btn;
    JRadioButton black_btn;
    JButton ok;
    JButton cancel;
    private ButtonGroup group;
	

	/**
	 * Create the frame.
	 */
	public setupAI() {
	      super("Options");
	      setIconImage(new ImageIcon( getClass().getResource("/images/logo.png")).getImage());
	       JPanel mainPane = new JPanel(new BorderLayout());
	       mainPane.add(createLevelPane(),BorderLayout.NORTH);
	       mainPane.add(createColorPane(),BorderLayout.CENTER);
	       mainPane.add(createButtonPane(),BorderLayout.SOUTH);
	       mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	       setContentPane(mainPane);
	       addEvents();
	       pack();
	       setLocationRelativeTo(null);
	       setResizable(false);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	      
	}

	 private void addEvents() {
		 ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clickOk();
				
			}
		});
		 cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		
	}
	public void clickOk() {
		int lv = level.getValue();
		String team = group.getSelection().getActionCommand();		
		
		ChessGame game = new ChessGame();
		
		ChessGui chessGui = new ChessGui(game);
		
		AI ai = new AI(chessGui);
		ai.Depth = lv;
		
		if(team.equals("Black")){
			game.setPlayer(Piece.COLOR_BLACK, chessGui);
			game.setPlayer(Piece.COLOR_WHITE, ai);
		}else{
			game.setPlayer(Piece.COLOR_WHITE, chessGui);
			game.setPlayer(Piece.COLOR_BLACK, ai);
		}
		
		this.setVisible(false);
		new Thread(game).start();
		
		
	}

	public JPanel createLevelPane(){
	        level = new JSlider(JSlider.HORIZONTAL,1,3,2);
	        JPanel levelPane = new JPanel();        
	        level.setMajorTickSpacing(1);
	        level.setPaintTicks(true);
	        level.setPaintLabels(true);
	        levelPane.add(level);
	        levelPane.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createEmptyBorder(5,5,5,5),
	                BorderFactory.createTitledBorder("Level")));
	        return levelPane;
	    }
	    public JPanel createColorPane(){
	        JPanel colorPane = new JPanel(new GridLayout(1,2));
	        white_btn = new JRadioButton("White",true);
	        white_btn.setActionCommand("White");
	        black_btn = new JRadioButton("Black");
	        black_btn.setActionCommand("Black");
	        group = new ButtonGroup();
	        group.add(white_btn);
	        group.add(black_btn);
	        colorPane.add(white_btn);
	        colorPane.add(black_btn);
	        colorPane.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createEmptyBorder(5,5,5,5),
	                BorderFactory.createTitledBorder("Color")));
	        return colorPane;
	    }
	    public JPanel createButtonPane(){
	        JPanel buttonPane = new JPanel(new BorderLayout());
	        JPanel pane = new JPanel(new GridLayout(1,2,5,0));
	        pane.add(ok = new JButton("OK"));
	        pane.add(cancel = new JButton("Cancel"));
	        buttonPane.add(pane,BorderLayout.EAST);
	        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	        return buttonPane;
	    }
	   
}
