package twoplayer.gui;
import java.awt.Image;

import twoplayer.logic.Piece;



public class GuiPiece{
	
	private Image img;
	private int x;
	private int y;
	private Piece piece;
	
	public GuiPiece(Image img, Piece piece) {
		this.img = img;
		this.piece = piece;

		this.x = ChessGui.convertColumnToX(piece.getColumn());
		this.y = ChessGui.convertRowToY(piece.getRow());
	}

	public Image getImage() {
		return img;
	}

	public int getX() {
		return x;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return img.getHeight(null);
	}

	public int getHeight() {
		return img.getHeight(null);
	}
	public Piece getPiece() {
		return piece;
	}

	public boolean isCaptured() {
		return this.piece.isCaptured();
	}

	public int getColor() {
		return this.piece.getColor();
	}
	public void resetToUnderlyingPiecePosition() {
		
		this.x = ChessGui.convertColumnToX(piece.getColumn());
		this.y = ChessGui.convertRowToY(piece.getRow());
		
	}
	@Override
	public String toString() {
		return this.piece+" "+x+"/"+y;
	}
	
}
