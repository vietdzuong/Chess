package singleplayer.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import singleplayer.gui.ChessGui;
import singleplayer.gui.GuiPiece;
import singleplayer.logic.ChessGame;
import singleplayer.logic.Piece;



public class DragAndDropListener implements MouseListener, MouseMotionListener {

	private List<GuiPiece> guiPieces;
	private ChessGui chessGui;
	private int deltaX;
	private int deltaY;
	
	public DragAndDropListener(List<GuiPiece> guiPieces, ChessGui chessGui) {
		this.guiPieces = guiPieces;
		this.chessGui = chessGui;
	}

	/*So sánh tọa độ x,y của chuột và toạn độ của mảnh guiPeice
	 *Nếu x,y nằm trong tọa độ của guiPiece thì return true
	 */
	private boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {

		return guiPiece.getX() <= x 
			&& guiPiece.getX()+guiPiece.getWidth() >= x
			&& guiPiece.getY() <= y
			&& guiPiece.getY()+guiPiece.getHeight() >= y;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(this.chessGui.getDragPiece() != null){
			
			int x = e.getPoint().x - this.deltaX;
			int y = e.getPoint().y - this.deltaY;
			
			GuiPiece dragPiece = this.chessGui.getDragPiece();
			//Set lại vị trí vẽ ảnh của piece
			dragPiece.setX(x);
			dragPiece.setY(y);
			
			this.chessGui.repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!chessGui.isDraggingGamePieces()){
			return;
		}
		//lấy vị trí chuột
		int x = e.getPoint().x;
		int y = e.getPoint().y;
		
		//kiểm tra trong mảng guiPieces có 
		for (GuiPiece guiPiece:guiPieces) {
			
			if (guiPiece.isCaptured()) {
				continue;
			}
			/*So sánh tọa độ x,y của chuột và toạn độ của mảnh guiPeice
			 *Nếu x,y nằm trong tọa độ của guiPiece thì return true
			 */
			if(mouseOverPiece(guiPiece,x,y)){
				
				//Kiểm tra màu của piece và màu của Game state
				if( (this.chessGui.getGameState() == ChessGame.GAME_STATE_WHITE
						&& guiPiece.getColor() == Piece.COLOR_WHITE) ||
					(this.chessGui.getGameState() == ChessGame.GAME_STATE_BLACK
							&& guiPiece.getColor() == Piece.COLOR_BLACK))
				{
					/*
					 * Tính khoảng cách giữa vị trí bắt đầu của piece 
					 * và vị trí chuột press trên piece đó
					 */
					this.deltaX = x - guiPiece.getX();
					this.deltaY = y - guiPiece.getY();
					this.chessGui.setDragPiece(guiPiece);
					this.chessGui.repaint();
					break;
				}
			
			}
		}
		
		//Add to top
		if(chessGui.getDragPiece() != null){
			this.guiPieces.remove(chessGui.getDragPiece());
			this.guiPieces.add(chessGui.getDragPiece());
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(chessGui.getDragPiece() != null){
			/*
			 * Tính toán lại vị trí để vẽ ảnh cảu piece
			 */
			int x = e.getPoint().x - this.deltaX;
			int y = e.getPoint().y - this.deltaY;
			
			chessGui.setNewPiece(this.chessGui.getDragPiece(), x, y);
			this.chessGui.repaint();
			this.chessGui.setDragPiece(null);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
