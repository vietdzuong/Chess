package singleplayer.logic;

import java.io.Serializable;

public class Move implements Serializable{
	public int sourceRow;
	public int sourceColumn;
	public int targetRow;
	public int targetColumn;
	public Piece capturedPiece;
	
	public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		this.sourceRow = sourceRow;
		this.sourceColumn = sourceColumn;
		this.targetRow = targetRow;
		this.targetColumn = targetColumn;
	}

	@Override
	public String toString() {
		return sourceRow+","+sourceColumn+"=>"+targetRow+","+targetColumn;
	}
	
}
