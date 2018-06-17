package twoplayer.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import twoplayer.gui.ChessGui;
import twoplayer.gui.GuiPiece;
import twoplayer.logic.ChessGame;
import twoplayer.logic.Piece;




public class PiecesDragAndDropListener implements MouseListener, MouseMotionListener {

	private List<GuiPiece> guiPieces;
	private ChessGui chessGui;
	
	private GuiPiece dragPiece;
	private int deltaX;
	private int deltaY;
	

	public PiecesDragAndDropListener(List<GuiPiece> guiPieces, ChessGui chessGui) {
		this.guiPieces = guiPieces;
		this.chessGui = chessGui;
	
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent evt) {
		int x = evt.getPoint().x;
		int y = evt.getPoint().y;
		
		for (GuiPiece guiPiece:guiPieces) {
			
			if (guiPiece.isCaptured()||
				chessGui.getChessGame().getTeamColor()
				!= guiPiece.getPiece().getColor()) {
				continue;
			}

			if(mouseOverPiece(guiPiece,x,y)){
				
				if( (this.chessGui.getGameState() == ChessGame.GAME_STATE_WHITE
						&& guiPiece.getColor() == Piece.COLOR_WHITE) ||
					(this.chessGui.getGameState() == ChessGame.GAME_STATE_BLACK
							&& guiPiece.getColor() == Piece.COLOR_BLACK))
				{
					// tinh khoang cach gia position mouse va piece
					this.deltaX = x - guiPiece.getX();
					this.deltaY = y - guiPiece.getY();
					this.chessGui.setDragPiece(guiPiece);
					this.dragPiece = guiPiece;
					this.chessGui.repaint();
					break;
				}
			
			}
		}
		
		// move drag piece to the top of the list
		if(this.dragPiece != null){
			this.guiPieces.remove( this.dragPiece );
			this.guiPieces.add(this.dragPiece);
		}
	}

	private boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {

		return guiPiece.getX() <= x 
			&& guiPiece.getX()+guiPiece.getWidth() >= x
			&& guiPiece.getY() <= y
			&& guiPiece.getY()+guiPiece.getHeight() >= y;
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if( this.dragPiece != null){
			int x = evt.getPoint().x - this.deltaX;
			int y = evt.getPoint().y - this.deltaY;
			
			// set game piece to the new location if possible
			//
			chessGui.setNewPieceLocation(this.dragPiece, x, y);
			this.chessGui.repaint();
			this.chessGui.setDragPiece(null);
			this.dragPiece = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(this.dragPiece != null){
			
			int x = evt.getPoint().x - this.deltaX;
			int y = evt.getPoint().y - this.deltaY;
			
			//System.out.println(
				//	"row:"+ChessGui.convertYToRow(y)
					//+" column:"+ChessGui.convertXToColumn(x));
			
			this.dragPiece.setX(x);
			this.dragPiece.setY(y);
			
			this.chessGui.repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

}
