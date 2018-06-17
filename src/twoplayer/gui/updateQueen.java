package twoplayer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import twoplayer.logic.Piece;



public class updateQueen extends JFrame {

	private JPanel contentPane;

	ChessGui chessGui;


	/**
	 * Create the frame.
	 * @param chessGui 
	 */
	public updateQueen(ChessGui chessGui, GuiPiece guiPiece) {
		setIconImage(new ImageIcon( getClass().getResource("/images/logo.png")).getImage());
		this.chessGui = chessGui;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 195);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 222, 179));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 222, 179));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		JButton btnQueen = new JButton("");
		btnQueen.setBackground(new Color(25, 25, 112));
		
		btnQueen.setIcon(getIconForPiece(guiPiece.getColor(), Piece.TYPE_QUEEN));
		panel.add(btnQueen);
		
		
		JButton btnKnight = new JButton("");
		btnKnight.setBackground(new Color(25, 25, 112));
		
		btnKnight.setIcon(getIconForPiece(guiPiece.getColor(), Piece.TYPE_KNIGHT));
		panel.add(btnKnight);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(245, 222, 179));
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Upgrade Pawn");
		lblNewLabel.setBackground(new Color(0, 0, 0));
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_1.add(lblNewLabel);
		btnKnight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				knight(guiPiece);
				chessGui.repaint();
				
			}
		});
		btnQueen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				queen(guiPiece);
				chessGui.repaint();
				
			}
		});
		setVisible(true);
		pack();
	}


	protected void queen(GuiPiece guiPiece) {
		
		guiPiece.getPiece().setType(Piece.TYPE_QUEEN);
		guiPiece.setImg(getImageForPiece(guiPiece.getColor(), Piece.TYPE_QUEEN));
		chessGui.setDragPiece(guiPiece);
		this.dispose();
		this.chessGui.sendLastMove();
		try {
			chessGui.getOs().writeObject(new String("queen"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	protected void knight(GuiPiece guiPiece) {
		guiPiece.getPiece().setType(Piece.TYPE_KNIGHT);
		guiPiece.setImg(getImageForPiece(guiPiece.getColor(), Piece.TYPE_KNIGHT));
		this.dispose();
		this.chessGui.sendLastMove();
		try {
			//this.chessGui.getOs().writeInt(Piece.TYPE_KNIGHT);
			
			this.chessGui.getOs().writeObject(new String("knight"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public ImageIcon getIconForPiece(int color, int type) {

		String filename = "";

		filename += (color == Piece.COLOR_WHITE ? "w" : "b");
		switch (type) {
			case Piece.TYPE_BISHOP:
				filename += "b";
				break;
			case Piece.TYPE_KING:
				filename += "k";
				break;
			case Piece.TYPE_KNIGHT:
				filename += "n";
				break;
			case Piece.TYPE_PAWN:
				filename += "p";
				break;
			case Piece.TYPE_QUEEN:
				filename += "q";
				break;
			case Piece.TYPE_ROOK:
				filename += "r";
				break;
		}
		filename += ".png";

		URL urlPieceImg = getClass().getResource("/images/" + filename);
		return new ImageIcon(urlPieceImg);
	}
	public Image getImageForPiece(int color, int type) {

		String filename = "";

		filename += (color == Piece.COLOR_WHITE ? "w" : "b");
		switch (type) {
			case Piece.TYPE_BISHOP:
				filename += "b";
				break;
			case Piece.TYPE_KING:
				filename += "k";
				break;
			case Piece.TYPE_KNIGHT:
				filename += "n";
				break;
			case Piece.TYPE_PAWN:
				filename += "p";
				break;
			case Piece.TYPE_QUEEN:
				filename += "q";
				break;
			case Piece.TYPE_ROOK:
				filename += "r";
				break;
		}
		filename += ".png";

		URL urlPieceImg = getClass().getResource("/images/" + filename);
		return new ImageIcon(urlPieceImg).getImage();
	}
}
