package singleplayer.gui;

import java.awt.Image;

import singleplayer.logic.Piece;



/*
 * Class thể hiện giao diện của 1 mảnh trên bàng cờ
 * Chứa ảnh và vị trí tọa độ X,Y để vẻ ảnh
 * và thông tin quân cờ của ảnh(loại,màu, dòng, cột)
 */
public class GuiPiece{
	
	
	private Image img;
	//Tọa độ để vẻ ảnh (không phải cột và dòng)
	private int x;
	private int y;
	//Thông tin quân cờ(chứa cột dòng)
	private Piece piece;
	
	public GuiPiece(Image img, Piece piece) {
		this.img = img;
		this.piece = piece;

		//Chuyển cột, dòng -> X,Y (ROW_1 ->1230)
		this.x = ChessGui.convertColumnToX(piece.getColumn());
		this.y = ChessGui.convertRowToY(piece.getRow());
	}

	public Image getImage() {
		return img;
	}

	public int getX() {
		return x;
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
		
		//Cập nhật lại x,y theo piece mà nó đang giữ
		this.x = ChessGui.convertColumnToX(piece.getColumn());
		this.y = ChessGui.convertRowToY(piece.getRow());
		
	}
	@Override
	public String toString() {
		return this.piece+" "+x+", "+y;
	}
	
}
