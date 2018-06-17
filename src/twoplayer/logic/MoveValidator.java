package twoplayer.logic;

public class MoveValidator {
	
	private ChessGame chessGame;
	private Piece sourcePiece;
	private Piece targetPiece;
	
	public MoveValidator(ChessGame chessGame){
		this.chessGame = chessGame;
	}
	
	public boolean isMoveAllow(Move move)
	{
		int sourceRow = move.sourceRow;
		int sourceColumn = move.sourceColumn;
		int targetRow = move.targetRow;
		int targetColumn = move.targetColumn;
		
		sourcePiece = chessGame.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
		targetPiece = chessGame.getNonCapturedPieceAtLocation(targetRow, targetColumn);
		
		if(sourcePiece == null){
			System.out.println("Cannot find sourcePiece in Movali");
			return false;
		}
		
		if(sourcePiece.getColor() != chessGame.getGameState()){
			return false;
			
		}
		if( targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8
				|| targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_H){
			System.out.println("target row or column out of scope");
			
			return false;
		}
		boolean canMove = false;
		switch (sourcePiece.getType()) {
		case Piece.TYPE_BISHOP:
			canMove = isValidBishopMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
		case Piece.TYPE_KING:
			canMove = isValidKingMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
		case Piece.TYPE_KNIGHT:
			canMove = isValidKnightMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
		case Piece.TYPE_PAWN:
			canMove = isValidPawnMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
		case Piece.TYPE_QUEEN:
			canMove = isValidQueenMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
		case Piece.TYPE_ROOK:
			canMove = isValidRookMove(sourceRow,sourceColumn,targetRow,targetColumn);break;
		default: break;
		}
		
		return canMove;
	}

	private boolean checkTarget(){
		if(targetPiece == null){
			return true;
		}
		else if(targetPiece.getColor() == sourcePiece.getColor()){
			return false;
		}
		return true;
	}
	
	private boolean isBetweenSourceAndTarget(
			int sourceRow, int sourceColumn,
			int targetRow, int targetColumn,
			int rowStep, int columnStep)
	{
		int currentRow = sourceRow+rowStep;
		int currentColumn = sourceColumn+columnStep;
		while(true){
			if(currentRow == targetRow && currentColumn == targetColumn){
				break;
			}
			if( currentRow < Piece.ROW_1 || currentRow > Piece.ROW_8
					|| currentColumn < Piece.COLUMN_A || currentColumn > Piece.COLUMN_H){
					break;
			}
			if(this.chessGame.isNonCapturedPieceAtLocation(currentRow, currentColumn)){
				return true;
			}
			
			currentRow += rowStep;
			currentColumn += columnStep;
		}
		
		return false;
	}
	private boolean isValidRookMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		if(!checkTarget()){
			return false;
		}
		boolean canMove = false;
		
		int deltaRow = targetRow - sourceRow;
		int deltaColumn = targetColumn - sourceColumn;
		
		if(deltaRow == 0 && deltaColumn > 0){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn,
					targetRow, targetColumn, 0, 1);
			
		}else if(deltaRow == 0 && deltaColumn < 0){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn,
					targetRow, targetColumn, 0, -1);
			
		}else if(deltaRow > 0 && deltaColumn == 0){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn,
					targetRow, targetColumn, 1, 0);
		}else if(deltaRow < 0 && deltaColumn == 0){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn,
					targetRow, targetColumn, -1, 0);
			
		}else{
			canMove = false;
		}
		return canMove;
	}

	private boolean isValidQueenMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		boolean canMove = false;
		if(isValidRookMove(sourceRow, sourceColumn,
				targetRow, targetColumn)
		|| isValidBishopMove(sourceRow, sourceColumn,
				targetRow, targetColumn)){
			canMove = true;
		}
		return canMove;
	}

	private boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		boolean canMove = false;
		
		if(targetPiece == null)
		{
			if(sourceColumn == targetColumn)
			{
				if(sourcePiece.getColor() == Piece.COLOR_WHITE)
				{
					if(sourceRow == Piece.ROW_2)
					{
						if(sourceRow+2 == targetRow)
						{
							if(!isBetweenSourceAndTarget(sourceRow, sourceColumn,
									targetRow, targetColumn, 1, 0))
							{
								canMove = true;
							}else
							{
								canMove = false;
							}
						}else if(sourceRow+1 == targetRow)
						{
							canMove = true;
						}
					}
					else if(sourceRow+1 == targetRow)
					{
						canMove = true;
					}
				} 
				else if(sourcePiece.getColor() == Piece.COLOR_BLACK)
				{
					if(sourceRow == Piece.ROW_7)
					{
						if(sourceRow-2 == targetRow)
						{
							if(!isBetweenSourceAndTarget(sourceRow, sourceColumn,
									targetRow, targetColumn, -1, 0))
							{
								canMove = true;
							}else
							{
								canMove = false;
							}
						}else if(sourceRow-1 == targetRow)
						{
							canMove = true;
						}
					}
					else if(sourceRow-1 == targetRow)
					{
						canMove = true;
					}
				}
			}
		}
		else{
			if(sourcePiece.getColor() != targetPiece.getColor()){
				if(sourcePiece.getColor() == Piece.COLOR_WHITE)
				{
					if(sourceRow+1 == targetRow)
					{
						if(Math.abs(targetColumn-sourceColumn) == 1)
						{
							return true;
						}
					}
				}else if(sourcePiece.getColor() == Piece.COLOR_BLACK)
				{
					if(sourceRow - 1 == targetRow)
					{
						if(Math.abs(targetColumn-sourceColumn) == 1)
						{
							return true;
						}
					}
				}
			}
		}
		return canMove;
	}

	private boolean isValidKnightMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		if(!checkTarget()){
			return false;
		}
		boolean canMove = false;
		if(((Math.abs(sourceColumn - targetColumn) +
			 Math.abs(sourceRow - targetRow) == 3)) &&
			 sourceColumn != targetColumn && sourceRow != targetRow)
		{
			canMove = true;
		}
		return canMove;
	}

	private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		if(!checkTarget()){
			return false;
		}
		boolean canMove = false;
		if(Math.abs(sourceRow - targetRow) <= 1 &&
		   Math.abs(sourceColumn - targetColumn) <= 1)
		{
			canMove  = true;
		}
		return canMove;
	}

	private boolean isValidBishopMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		if(!checkTarget()){
			return false;
		}
		boolean canMove = false;
		int deltaRow = targetRow - sourceRow;
		int deltaColumn = targetColumn - sourceColumn;
		
		if((deltaRow == deltaColumn) && (deltaColumn > 0)){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, 1, 1);
			
		}else if((deltaRow == -deltaColumn) && (deltaColumn > 0)){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, 1);
			
		}else if((deltaRow == deltaColumn) && (deltaColumn < 0)){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, -1, -1);
			
		}else if((deltaRow == -deltaColumn) && (deltaColumn < 0)){
			
			canMove = !isBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn, 1, -1);
			
		}else{
			//System.out.println("not moving bishop");
			return false;
		
		}
		
		return canMove;
	}
}
