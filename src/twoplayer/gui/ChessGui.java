package twoplayer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import twoplayer.listener.PiecesDragAndDropListener;
import twoplayer.logic.ChessGame;
import twoplayer.logic.Move;
import twoplayer.logic.MoveValidator;
import twoplayer.logic.Piece;



public class ChessGui extends JPanel{
	
	// bat dong cua board
	/*private static final int BOARD_START_X = 48;
	private static final int BOARD_START_Y = 52;

	//Kích thước của ô
	private static final int SQUARE_WIDTH = 63;
	private static final int SQUARE_HEIGHT = 62;*/
	//Vị trí bắt đầu của bàn cờ
	private static final int BOARD_X = 44;
	private static final int BOARD_Y = 48;

	//Kích thước của ô
	private static final int SQUARE_WIDTH = 63;
	private static final int SQUARE_HEIGHT = 62;

	//Kích thước hình của quân cờ
	private static final int PIECE_WIDTH = 48;
	private static final int PIECE_HEIGHT = 48;
			
	private static final int PIECES_X = BOARD_X + (int)(SQUARE_WIDTH/2.0 - PIECE_WIDTH/2.0);
	private static final int PIECES_Y = BOARD_Y + (int)(SQUARE_HEIGHT/2.0 - PIECE_HEIGHT/2.0);
			
	private static final int DRAG_SQUARE_X = BOARD_X - (int)(PIECE_WIDTH/2.0);
	private static final int DRAG_SQUARE_Y = BOARD_Y - (int)(PIECE_HEIGHT/2.0);
	
	private Move lastMove = null;
	private GuiPiece dragPiece;
	private Image imgBackground;
	private ChessGame chessGame;
	private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();
	public JLabel state, team;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	public ChessGui(ObjectOutputStream os,  ObjectInputStream is, int color)
	{
		
		this.os = os;
		this.is = is;
		
		this.setLayout(null);
		URL urlBackgroundImg = getClass().getResource("/images/board.jpg");
		this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();
		
		
		this.setPreferredSize(new Dimension(imgBackground.getWidth(null), imgBackground.getHeight(null)));
		this.chessGame = new ChessGame(this, color);
		for(Piece piece: this.chessGame.getPieces()){
			createAndAddGuiPiece(piece);
		}
		
		PiecesDragAndDropListener listener = 
				new PiecesDragAndDropListener(this.guiPieces, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		state = new JLabel("White start first");
	
		JFrame f = new JFrame();
		team = new JLabel("");
		team.setFont(new Font("Tahoma", Font.BOLD, 20));
		team.setHorizontalAlignment(SwingConstants.CENTER);
		
		state.setFont(new Font("Tahoma", Font.BOLD, 14));
		state.setHorizontalAlignment(SwingConstants.CENTER);
		state.setForeground(new Color(0, 51, 102));
		if(chessGame.getTeamColor() == ChessGame.TEAM_BLACK){
			team.setText("Team Black");
			team.setForeground(Color.BLACK);
			
			f.setLocation(650, 50);
		}
		else{
			team.setText("Team White");
			team.setForeground(Color.WHITE);
			
			f.setLocation(10, 50);
		}
		f.setLayout(new BorderLayout());
		f.getContentPane().setBackground(new Color(245, 222, 179));
		f.setVisible(true);
		f.setIconImage(new ImageIcon( getClass().getResource("/images/logo.png")).getImage());
		f.add(this, BorderLayout.CENTER);
		f.add(state, BorderLayout.PAGE_END);
		f.add(team, BorderLayout.PAGE_START);
		f.pack();
		getFirstMove();
	}
	
	public ObjectOutputStream getOs() {
		return os;
	}

	public void setOs(ObjectOutputStream os) {
		this.os = os;
	}

	public ObjectInputStream getIs() {
		return is;
	}

	public void setIs(ObjectInputStream is) {
		this.is = is;
	}

	private void getFirstMove() {
		if(chessGame.getTeamColor() == ChessGame.TEAM_BLACK){
			receiveLastMove();
			state.setText("White start first");
		}
		
	}

	private void createAndAddGuiPiece(Piece piece) {
		Image img = this.getImageForPiece(piece.getColor(), piece.getType());
		GuiPiece guiPiece = new GuiPiece(img, piece);
		this.guiPieces.add(guiPiece);
		
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
	//X, Y là theo theo vị trí pixel
	//Column_A -> X(vi tri bat dau cau image de ve)
	public static int convertColumnToX(int column){
		return  PIECES_X + SQUARE_WIDTH * column;
		
	}
	//Row_1 -> Y
	public static int convertRowToY(int row){
		return  PIECES_Y + SQUARE_HEIGHT * (Piece.ROW_8 - row);
	}
	//Y -> Row 
	public static int convertYToRow(int y){
		return Piece.ROW_8 - (y - DRAG_SQUARE_Y)/SQUARE_HEIGHT;
	}

	public static int convertXToColumn(int x){
		return (x - DRAG_SQUARE_X)/SQUARE_WIDTH;
	}

	public int getGameState() {
		return this.chessGame.getGameState();
	}
	private GuiPiece getGuiPiece(int row, int column) {
		for (GuiPiece guiPiece : this.guiPieces) {
			if( guiPiece.getPiece().getRow() == row
					&& guiPiece.getPiece().getColumn() == column
					&& guiPiece.isCaptured() == false){
				return guiPiece;
			}
		}
		return null;
	}
	public ChessGame getChessGame() {
		return chessGame;
	}

	public void setChessGame(ChessGame chessGame) {
		this.chessGame = chessGame;
	}

	//Update vi tri cua manh dc di chuyen

	
	
	public void setDragPiece(GuiPiece guiPiece) {
		this.dragPiece = guiPiece;
	}
	@Override
	protected void paintComponent(Graphics g) {
		//gọi super để delete all components
		
		super.paintComponent(g);
		g.drawImage(this.imgBackground, 0, 0, null);

		
		if(!isDraggingPiece() && lastMove != null){
			
			int sourceX = convertColumnToX(lastMove.sourceColumn);
			int sourceY = convertRowToY(lastMove.sourceRow);
			int targetX = convertColumnToX(lastMove.targetColumn);
			int targetY = convertRowToY(lastMove.targetRow);
			
			g.setColor(Color.MAGENTA);
			
			g.drawRoundRect( sourceX-2, sourceY-2,
					SQUARE_WIDTH-12, SQUARE_HEIGHT-12, 10, 10);
			
			g.setColor(Color.YELLOW);
			g.drawRoundRect( targetX-2, targetY-2, 
					SQUARE_WIDTH-12, SQUARE_HEIGHT-12, 10, 10);
			
		}
		if(isDraggingPiece())
		{
			
			MoveValidator validator = chessGame.getMoveValidator();
			for(int column = Piece.COLUMN_A; column <= Piece.COLUMN_H; column++)
			{
				for(int row = Piece.ROW_1; row <= Piece.ROW_8; row++)
				{
					int sourceRow = dragPiece.getPiece().getRow();
					int sourceColumn = dragPiece.getPiece().getColumn();
					
					if(validator.isMoveAllow(new Move(sourceRow, sourceColumn, row, column)))
					{
						int canMoveX = convertColumnToX(column);
						int canMoveY = convertRowToY(row);
						g.setColor(Color.GREEN);
						g.drawRoundRect( canMoveX-2, canMoveY-2, SQUARE_WIDTH-12, SQUARE_HEIGHT-12, 10, 10);
					}
				}
			}
			
		}
		for (GuiPiece guiPiece : this.guiPieces) {
			if( !guiPiece.isCaptured()){
				g.drawImage(guiPiece.getImage(), guiPiece.getX(), guiPiece.getY(), null);
			}
		}
		/*g.setColor(Color.GREEN);
		for(int row = Piece.ROW_1; row <= Piece.ROW_8; row++)
		{
			for(int column = Piece.COLUMN_A; column <= Piece.COLUMN_H; column++)
			{
				int x = convertColumnToX(column);
				int y = convertRowToY(row);
				g.drawString(row+ ","+column,x, y+10);
			}
		}*/
	}
	private boolean isDraggingPiece() {
		if(dragPiece == null){
			return false;
		}
		return true;
	}
	
	
	public void setNewPieceLocation(GuiPiece dragPiece, int x, int y) {
		
		int targetRow = ChessGui.convertYToRow(y);
		int targetColumn = ChessGui.convertXToColumn(x);
		
		//kiem traneu sai thi return ve cu
		if( targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8 || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_H){
			
			dragPiece.resetToUnderlyingPiecePosition();
		
		}else{
		
			System.out.println("moving piece to "+targetRow+"/"+targetColumn);
			Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn()
					, targetRow, targetColumn);
			boolean rs = this.chessGame.movePiece(move);
			if(rs == true){
				lastMove = move;
				
				
				if(dragPiece.getPiece().getType() 
						== Piece.TYPE_PAWN &&
						(targetRow == Piece.ROW_1||
						 targetRow == Piece.ROW_8)){
					
					new updateQueen(this, dragPiece);
					sendLastMove();
				}
				else{
					sendLastMove();
				}
			}
			dragPiece.resetToUnderlyingPiecePosition();
			receiveLastMove();
		}
	}
	
	public void sendLastMove(){

		try {
			
			os.writeObject(lastMove);
			os.flush();
			lastMove = null;
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		if(lastMove != null){
			
			System.out.println("Send: "+lastMove);
			
		}
	
		
	}
	private void receiveLastMove() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						
						if(lastMove == null){
							lastMove = (Move) is.readObject();
							
							if(lastMove != null){
								upgradePawn();
								doLastMove();
								break;
							}
						}
					} catch (ClassNotFoundException e1) {
						if(lastMove == null){
							continue;
						}
					
					} catch (IOException e1) {
						if(lastMove == null){
							continue;
						}
					}
					
					
				}
			}
		}).start();
		
	}
	protected void upgradePawn() {
		for(GuiPiece gui : guiPieces){
			if(gui.getPiece().getRow() == lastMove.sourceRow &&
			   gui.getPiece().getColumn() == lastMove.sourceColumn &&
			   gui.getPiece().getType() == Piece.TYPE_PAWN) 
				if(lastMove.targetRow == Piece.ROW_1 ||
				   lastMove.targetRow == Piece.ROW_8)
				{
					if(Math.abs(lastMove.targetRow - lastMove.sourceRow) == 1)
					{
						System.out.println("Upgrade");
						
							
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									
										String type = "";
										while(true){
											try {
												type = (String) is.readObject();
												System.out.println("type:"+type);
												if(type != null){
													break;
												}
											} catch (ClassNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
												if(type == null){
													continue;
												}
											} catch (IOException e) {
												if(type == null){
													continue;
												}
							
											}
										}
										//gui.getPiece().setType(Piece.TYPE_QUEEN);
										//gui.setImg(getImageForPiece(gui.getColor(), Piece.TYPE_QUEEN));
										if(type.equals("queen")){
											gui.getPiece().setType(Piece.TYPE_QUEEN);
											gui.setImg(getImageForPiece(gui.getColor(), Piece.TYPE_QUEEN));
										}else if(type.equals("knight")){
											gui.getPiece().setType(Piece.TYPE_KNIGHT);
											gui.setImg(getImageForPiece(gui.getColor(), Piece.TYPE_KNIGHT));
										}
										gui.resetToUnderlyingPiecePosition();
										repaint();
									
									
								}
							}).start();
								
								
					}
				}
		}
	}
	

	private void doLastMove(){
		if(lastMove == null){
			return;
		}
		System.out.println("Receive: "+lastMove);
		GuiPiece piece = this.getGuiPiece(lastMove.sourceRow, lastMove.sourceColumn);
		
		if(piece == null)
		{
			return;
		}
		chessGame.movePiece(lastMove);
		
		piece.resetToUnderlyingPiecePosition();
		repaint();
		//lastMove = null;
		
	}

}
