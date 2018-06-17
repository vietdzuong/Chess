package singleplayer.ai;

import java.util.ArrayList;
import java.util.List;

import singleplayer.gui.ChessGui;
import singleplayer.logic.ChessGame;
import singleplayer.logic.Move;
import singleplayer.logic.MoveController;
import singleplayer.logic.MoveValidator;
import singleplayer.logic.Piece;



public class AI implements MoveController{
	private ChessGame chessGame;
	private MoveValidator validator;
	private ChessGui gui;
	//Độ sâu của cây
	public int Depth = 1;

	public AI(ChessGui gui){
		this.gui = gui;
		this.chessGame = gui.getChessGame();
		this.validator = chessGame.getMoveValidator();
	}
	
	public void setDepth(int n){
		this.Depth = n;
	}
	
	private Move getBestMove() {
		
		ArrayList<Move> moves = getValidMoves();
		int bestScore = Integer.MIN_VALUE;
		Move bestMove = null;
		
		for(Move move : moves)
		{
			doMove(move);
			int score = -1*valMax(this.Depth);
			undoMove(move);
			if(score > bestScore){
				bestScore = score;
				bestMove = move;
			}
		}
		return bestMove;
	}
	private void undoMove(Move move) {
		
		chessGame.undoMove(move);
	}


	private int valMax(int depth) {
		if(depth <= 0 
		   ||chessGame.getGameState() == ChessGame.GAME_STATE_END_BLACK_WON
		   ||chessGame.getGameState() == ChessGame.GAME_STATE_END_WHITE_WON)
		{
			return evaluateOfState();
		}
		//Lấy tất cả bước đi có thể của 1 trạng thái cờ
		ArrayList<Move> moves = getValidMoves();
		int value = Integer.MIN_VALUE;
		
		for(Move move : moves){
			doMove(move);
			int score = -1*valMax(depth-1);
			undoMove(move);
			if(score > value){
				value = score;
			}
		}
		
		return value;
	}


	private int evaluateOfState() {
		int scoreWhite = 0;
		int scoreBlack = 0;
		
		/*
		 * Tính giá trị của từng màu 
		 * dựa trên vị trí và lọai quân cờ
		 */
		for (Piece piece : this.chessGame.getPieces()) {
			
			if(piece.getColor() == Piece.COLOR_BLACK){
				
				scoreBlack +=
					getScoreType(piece.getType());
				
				scoreBlack +=
					getScorePosition(piece.getRow(),piece.getColumn());
				
			}else if( piece.getColor() == Piece.COLOR_WHITE){
				scoreWhite +=
					getScoreType(piece.getType());
				scoreWhite +=
					getScorePosition(piece.getRow(),piece.getColumn());
			}else{
				throw new IllegalStateException(
						"Lỗi valueState: "+piece.getColor());
			}
		}
		
		// Giá trị trả về dựa trên lượt chơi
		int gameState = this.chessGame.getGameState();
		
		if( gameState == ChessGame.GAME_STATE_BLACK){
			System.out.println("===========>Score Black");
			return scoreBlack - scoreWhite;
		
		}else if(gameState == ChessGame.GAME_STATE_WHITE){
			System.out.println("===========>Score White");
			return scoreWhite - scoreBlack;
		
		}else if(gameState == ChessGame.GAME_STATE_END_WHITE_WON
				|| gameState == ChessGame.GAME_STATE_END_BLACK_WON){
			return Integer.MIN_VALUE + 1;
		
		}else{
			throw new IllegalStateException("unknown game state: "+gameState);
		}
	}
	private int getScorePosition(int row, int column) {
		int[][] positionScore =
		{ {1,1,1,1,1,1,1,1}
		 ,{2,2,2,2,2,2,2,2}
		 ,{2,2,3,3,3,3,2,2}
		 ,{2,2,3,4,4,3,2,2}
		 ,{2,2,3,4,4,3,2,2}
		 ,{2,2,3,3,3,3,2,2}
		 ,{2,2,2,2,2,2,2,2}
		 ,{1,1,1,1,1,1,1,1}
		 };
		return positionScore[row][column];
	}
	private int getScoreType(int type){
		switch (type) {
			case Piece.TYPE_BISHOP: return 30;
			case Piece.TYPE_KING: return 99999;
			case Piece.TYPE_KNIGHT: return 30;
			case Piece.TYPE_PAWN: return 10;
			case Piece.TYPE_QUEEN: return 90;
			case Piece.TYPE_ROOK: return 50;
			default: throw new IllegalArgumentException("unknown piece type: "+type);
		}
	}

	private void doMove(Move move){
		/*
		 * Thực hiện move và cập nhật trạng thái
		 */
		chessGame.movePiece(move);
		chessGame.changeGameState();
	}
	private ArrayList<Move> getValidMoves()
	{
		List<Piece> pieces = chessGame.getPieces();
		ArrayList<Move> list = new ArrayList<Move>();
		

		Move move = new Move(0,0,0,0);
		//lay luot
		int pieceColor = (chessGame.getGameState()
				== ChessGame.GAME_STATE_WHITE
				?Piece.COLOR_WHITE
				:Piece.COLOR_BLACK);
		
		for(Piece piece: pieces)
		{
			if(piece.getColor() == pieceColor)
			{
				
				move.sourceRow = piece.getRow();
				move.sourceColumn = piece.getColumn();
				
				for(int targetRow = Piece.ROW_1; 
						targetRow <= Piece.ROW_8; 
						targetRow++)
				{
					for (int targetColumn = Piece.COLUMN_A;
							targetColumn <= Piece.COLUMN_H;
							targetColumn++)
					{
						move.targetRow = targetRow;
						move.targetColumn = targetColumn;
						if(chessGame.getMoveValidator().isMoveAllow(move))
						{
							Move tmp = new Move(move.sourceRow, move.sourceColumn, move.targetRow, move.targetColumn);
							
							list.add(tmp);
						}
					}
				}
			}	
		}
		return list;
	}



	@Override
	public Move getMove() {
		
		gui.getState().setText("Computer is thinking...");
		Move move  = getBestMove();
		gui.getState().setText("It's your turn");
		return move;
	}



	@Override
	public void moveSuccessfullyExecuted(Move move) {
		// TODO Auto-generated method stub
		
	}
	




}
