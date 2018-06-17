package singleplayer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import singleplayer.listener.DragAndDropListener;
import singleplayer.logic.ChessGame;
import singleplayer.logic.Move;
import singleplayer.logic.MoveController;
import singleplayer.logic.MoveValidator;
import singleplayer.logic.Piece;



public class ChessGui extends JPanel implements MoveController {
	
		//Vị trí bắt đầu của bàn cờ
		private static final int BOARD_START_X = 44;
		private static final int BOARD_START_Y = 48;

		//Kích thước của ô
		private static final int SQUARE_WIDTH = 63;
		private static final int SQUARE_HEIGHT = 62;

		//Kích thước hình của quân cờ
		private static final int PIECE_WIDTH = 48;
		private static final int PIECE_HEIGHT = 48;
		
		private static final int PIECES_START_X = BOARD_START_X + (int)(SQUARE_WIDTH/2.0 - PIECE_WIDTH/2.0);
		private static final int PIECES_START_Y = BOARD_START_Y + (int)(SQUARE_HEIGHT/2.0 - PIECE_HEIGHT/2.0);
		
		private static final int DRAG_TARGET_SQUARE_START_X = BOARD_START_X - (int)(PIECE_WIDTH/2.0);
		private static final int DRAG_TARGET_SQUARE_START_Y = BOARD_START_Y - (int)(PIECE_HEIGHT/2.0);
		
		private Image imgBackground;
		private ChessGame chessGame;
		
		private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();
		
		private boolean draggingGamePieces;
		
		private GuiPiece dragPiece;
		//Lưu move vừa mới thực hiện để vẽ nước vừa đi xong
		private Move lastMove;
		//current lưu move khi drag thành công
		private Move currentMove;
		private JLabel state;
		public ChessGui(ChessGame chessGame)
		{
			setLayout(null);
			URL urlBackgroundImg = getClass().getResource("/images/board.jpg");
			this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();
			this.chessGame = chessGame;
			//Từ thông tin quân cờ(màu, loại, cột, dòng)
			//Add vào list thể hiện đồ họa của quân cờ
			
			for (Piece piece : this.chessGame.getPieces()) {
				addGuiPieceToList(piece);
			}
		
			
			state = new JLabel("White start first");
			state.setFont(new Font("Tahoma", Font.BOLD, 18));
			state.setHorizontalAlignment(SwingConstants.CENTER);
			DragAndDropListener listener = 
					new DragAndDropListener(this.guiPieces,this);
			this.addMouseListener(listener);
			this.addMouseMotionListener(listener);
			this.setPreferredSize(new Dimension(imgBackground.getWidth(null), imgBackground.getHeight(null)));
			JFrame f = new JFrame();
			f.setLayout(new BorderLayout());
			f.setVisible(true);
			f.getContentPane().setBackground(new Color(245, 222, 179));
			f.add(this, BorderLayout.CENTER);
			f.add(state, BorderLayout.PAGE_START);
			
			f.setIconImage(new ImageIcon( getClass().getResource("/images/logo.png")).getImage());
			f.pack();
		}
		
		public void saveAIsetting(){
			
		}
		private String getGameStateAsText() {
			String state = "unknown";
			switch (this.chessGame.getGameState()) {
				case ChessGame.GAME_STATE_BLACK: state = "black";break;
				case ChessGame.GAME_STATE_END_WHITE_WON: state = "white won";break;
				case ChessGame.GAME_STATE_END_BLACK_WON: state = "black won";break;
				case ChessGame.GAME_STATE_WHITE: state = "white";break;
			}
			return state;
		}
		
		//Di chuyển piece đang drag tới vị trí x,y
		public void setNewPiece(GuiPiece dragPiece, int x, int y) {
			
			
			
			//Chuyển x,y -> column, row(đích)
			int targetRow = ChessGui.convertYToRow(y);
			int targetColumn = ChessGui.convertXToColumn(x);
			
			
			
			//Thông tin move cần di chuyển
			Move move = new Move(dragPiece.getPiece().getRow(),
								 dragPiece.getPiece().getColumn(),
								 targetRow, targetColumn);
			//nếu cho phép di chuyển
			if( this.chessGame.getMoveValidator().isMoveAllow(move)){
				this.currentMove = move;
			}else{
				/*
				 * Cập nhật lại vị trí cũ nếu không đc move
				 * do lúc drag mouse thì x,y vẽ ảnh thay đổi
				 * nhưng row va column không đổi và khôi pục lại đc
				 */
				dragPiece.resetToUnderlyingPiecePosition();
			}
		}
		private void addGuiPieceToList(Piece piece) {
			//Lấy hình dựa trên loại và màu quân cờ
			Image img = this.getImageForPiece(piece.getColor(), piece.getType());
			GuiPiece guiPiece = new GuiPiece(img, piece);
			this.guiPieces.add(guiPiece);
		}
		private Image getImageForPiece(int color, int type) {

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

		//Column_A -> 123
		public static int convertColumnToX(int column){
			return  PIECES_START_X + SQUARE_WIDTH * column;
			
		}
		//Row_1 -> 123
		public static int convertRowToY(int row){
			return  PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_8 - row);
		}
		//123 -> Row_1 
		public static int convertYToRow(int y){
			return Piece.ROW_8 - (y - DRAG_TARGET_SQUARE_START_Y)/SQUARE_HEIGHT;
		}

		//123 -> Column_1
		public static int convertXToColumn(int x){
			return (x - DRAG_TARGET_SQUARE_START_X)/SQUARE_WIDTH;
		}
		
		public boolean isDraggingGamePieces(){
			return draggingGamePieces;
		}
		public int getGameState() {
			return this.chessGame.getGameState();
		}
		
		public void setDragPiece(GuiPiece guiPiece) {
			this.dragPiece = guiPiece;
		}
		public GuiPiece getDragPiece(){
			return this.dragPiece;
		}
	
		private boolean isUserDraggingPiece() {
			return this.dragPiece != null;
		}
		@Override
		protected void paintComponent(Graphics g) {
			//gọi super để delete all components
			
			super.paintComponent(g);
			g.drawImage(this.imgBackground, 0, 0, null);

			
			if(!isUserDraggingPiece() && lastMove != null){
				
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
			if(isUserDraggingPiece())
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
					
					g.drawImage(guiPiece.getImage(),
							guiPiece.getX(), guiPiece.getY(), null);
	
				}
			}
			
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
		
		@Override
		public Move getMove() {
			
			this.draggingGamePieces = true; 
			
			Move moveExecution = this.currentMove;
			
			this.currentMove = null;
			
			return moveExecution;
		}
		
		/*
		 *Thực hiện hàm này sau mỗi lần movePiece(Move)
		 *để cập nhật lại x,y của piece thực hiện move 
		 */
		@Override
		public void moveSuccessfullyExecuted(Move move) {
			// adjust GUI piece
			GuiPiece guiPiece = this.getGuiPiece(move.targetRow, move.targetColumn);
			if( guiPiece == null){
				throw new IllegalStateException("no guiPiece at "+move.targetRow+"/"+move.targetColumn);
			}
			/*
			 * Cập nhật lại x,y theo piece vì trong lúc drag
			 * thì x,y thay đổi liên tục 
			 * GuiPiece dragPiece = this.chessGui.getDragPiece();
			 * dragPiece.setX(x);
			 * dragPiece.setY(y);
			 */
			guiPiece.resetToUnderlyingPiecePosition();
			
			// remember last move
			this.lastMove = move;
			
			// disable dragging until asked by ChessGame for the next move
			this.draggingGamePieces = false;
					
			// repaint the new state
			this.repaint();
			
			
		}


		public JLabel getState() {
			return state;
		}
		public ChessGame getChessGame() {
			return chessGame;
		}
		public void setChessGame(ChessGame chessGame) {
			this.chessGame = chessGame;
		}
		
	
}
