package singleplayer.logic;

import java.util.ArrayList;
import java.util.List;

import menu.winner;




public class ChessGame implements Runnable{
	
	public int gameState = GAME_STATE_WHITE;
	public static final int GAME_STATE_WHITE = 0;
	public static final int GAME_STATE_BLACK = 1;
	public static final int GAME_STATE_END_BLACK_WON = 2;
	public static final int GAME_STATE_END_WHITE_WON = 3;
	
	private MoveController blackPlayer;
	private MoveController whitePlayer;
	private MoveController activePlayer;
	
	private MoveValidator moveValidator;
	//List quân cờ trên bàng
	public List<Piece> pieces = new ArrayList<Piece>();
	//List quân cờ đã bị ăn
	private List<Piece> capturedPieces = new ArrayList<Piece>();
	
	public ChessGame()
	{
		this.moveValidator = new MoveValidator(this);
		addBlack();
		addWhite();
	}
	private void addBlack() {
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_ROOK, Piece.ROW_8, Piece.COLUMN_A);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_KNIGHT, Piece.ROW_8, Piece.COLUMN_B);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_BISHOP, Piece.ROW_8, Piece.COLUMN_C);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_QUEEN, Piece.ROW_8, Piece.COLUMN_D);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_KING, Piece.ROW_8, Piece.COLUMN_E);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_BISHOP, Piece.ROW_8, Piece.COLUMN_F);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_KNIGHT, Piece.ROW_8, Piece.COLUMN_G);
		createPiece(Piece.COLOR_BLACK, Piece.TYPE_ROOK, Piece.ROW_8, Piece.COLUMN_H);
		
		// pawns
		int currentColumn = Piece.COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_7, currentColumn);
			currentColumn++;
		}
	}

	private void addWhite() {
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_ROOK, Piece.ROW_1, Piece.COLUMN_A);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_KNIGHT, Piece.ROW_1, Piece.COLUMN_B);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_BISHOP, Piece.ROW_1, Piece.COLUMN_C);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_QUEEN, Piece.ROW_1, Piece.COLUMN_D);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_KING, Piece.ROW_1, Piece.COLUMN_E);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_BISHOP, Piece.ROW_1, Piece.COLUMN_F);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_KNIGHT, Piece.ROW_1, Piece.COLUMN_G);
		createPiece(Piece.COLOR_WHITE, Piece.TYPE_ROOK, Piece.ROW_1, Piece.COLUMN_H);
		
		// pawns
		int currentColumn = Piece.COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_2, currentColumn);
			currentColumn++;
		}
		
		
	}
	private void createPiece(int color, int type, int row, int column) {
		Piece piece = new Piece(color, type, row, column);
	
		this.pieces.add(piece);
	}
	public int getGameState() {
		return this.gameState;
	}

	public Piece getNonCapturedPiece(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column) {
				return piece;
			}
		}
		return null;
	}
	boolean isNonCapturedPiece(int color, int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column
					&& piece.getColor() == color) {
				return true;
			}
		}
		return false;
	}
	boolean isNonCapturedPiece(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column) {
				return true;
			}
		}
		return false;
	}
	public List<Piece> getPieces() {
		return this.pieces;
	}
	public MoveValidator getMoveValidator(){
		return this.moveValidator;
	}
	
	
	
	public void changeGameState() {

		// check if game end condition has been reached
		//
		if (this.isGameEndConditionReached()) {

			if (this.gameState == ChessGame.GAME_STATE_BLACK) {
				this.gameState = ChessGame.GAME_STATE_END_BLACK_WON;
			} else if(this.gameState == ChessGame.GAME_STATE_WHITE){
				this.gameState = ChessGame.GAME_STATE_END_WHITE_WON;
			}else{
				
			}
			return;
		}

		switch (this.gameState) {
			case GAME_STATE_BLACK:
				this.gameState = GAME_STATE_WHITE;
				break;
			case GAME_STATE_WHITE:
				this.gameState = GAME_STATE_BLACK;
				break;
			case GAME_STATE_END_WHITE_WON:
				winner winner1 = new winner();
				winner1.setWinner("White win !!!");
				break;
			case GAME_STATE_END_BLACK_WON:
				winner winner2 = new winner();
				winner2.setWinner("Black win !!!");
				break;
			default:
				throw new IllegalStateException("unknown game state:" + this.gameState);
		}
	}
	
		
	private boolean isGameEndConditionReached() {
		for (Piece piece : this.capturedPieces) {
			if (piece.getType() == Piece.TYPE_KING ) {
				return true;
			} else {
				// continue iterating
			}
		}

		return false;
	}

	public void setPlayer(int pieceColor, MoveController playerHandler){
		switch (pieceColor) {
			case Piece.COLOR_BLACK:
				this.blackPlayer = playerHandler;
				break;
				
			case Piece.COLOR_WHITE: 
				this.whitePlayer = playerHandler; 
				break;
			default: throw new IllegalArgumentException("Invalid pieceColor: "+pieceColor);
		}
	}
	public void undoMove(Move move){
		Piece piece = getNonCapturedPiece(move.targetRow, move.targetColumn);
		
		piece.setRow(move.sourceRow);
		piece.setColumn(move.sourceColumn);
		
		if(move.capturedPiece != null){
			move.capturedPiece.setRow(move.targetRow);
			move.capturedPiece.setColumn(move.targetColumn);
			move.capturedPiece.isCaptured(false);
			this.capturedPieces.remove(move.capturedPiece);
			this.pieces.add(move.capturedPiece);
		}
		
		if(piece.getColor() == Piece.COLOR_BLACK){
			this.gameState = ChessGame.GAME_STATE_BLACK;
		}else{
			this.gameState = ChessGame.GAME_STATE_WHITE;
		}
	}
	public boolean movePiece(Move move) {
		
		/*
		 * Nếu ở vị trí đích có piece thì
		 * thêm piece đó vào captured của move
		 */
		move.capturedPiece = this.getNonCapturedPiece(move.targetRow, move.targetColumn);
		
		//Lấy piece nguồn
		Piece piece = getNonCapturedPiece(move.sourceRow, move.sourceColumn);

		/*
		 * Kiểm tra màu của piece hiện tại đang move
		 */
		int opponentColor = (piece.getColor()
				== Piece.COLOR_BLACK ? Piece.COLOR_WHITE
				: Piece.COLOR_BLACK);
		/*
		 * Nếu vị trí đích có quân cờ khác màu
		 * thì remove quân cờ đó ra khoải piece trên bàn
		 * add vào list bị ăn
		 */
		if (isNonCapturedPiece(opponentColor,
				move.targetRow, move.targetColumn)) {
			// handle captured piece
			Piece opponentPiece = getNonCapturedPiece(move.targetRow, move.targetColumn);
			
			this.pieces.remove(opponentPiece);
			this.capturedPieces.add(opponentPiece);
			opponentPiece.isCaptured(true);
		}
		

		/*
		 * Cập nhật lại piece được move cho mỗi lần repaint
		 * x,y của guiPiece đang giữ piece này sau mỗi lần 
		 * thực hiện movePiece thì nó sẽ gọi
		 * resetToUnderlyingPiecePosition() để cập nhật lại x,y
		 */
		piece.setRow(move.targetRow);
		piece.setColumn(move.targetColumn);

		return true;
	}
	
	private void waitForMove() {
		Move move = null;
		// đợi nước đi
		do{
			//thực hiện nước đi
			move = this.activePlayer.getMove();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if( move != null && this.moveValidator.isMoveAllow(move)){
				break;
				
			}else if( move != null && !this.moveValidator.isMoveAllow(move)){
				System.out.println("không thể move: "+move);
				
				//ChessConsole.printCurrentGameState(this);
				move = null;
				System.exit(0);
			}
		}while(move == null);
		
		//thực hiện move thành công
		
		boolean success = this.movePiece(move);
		
		if(success){
			//update bàn cờ của cả hai bên
			this.blackPlayer.moveSuccessfullyExecuted(move);
			this.whitePlayer.moveSuccessfullyExecuted(move);
		}else{
			throw new IllegalStateException("lỗi excute move sucess");
		}
	}
	private void swapActivePlayer() {
		
		if( this.activePlayer == this.whitePlayer ){
			this.activePlayer = this.blackPlayer;
			
		}else{
			this.activePlayer = this.whitePlayer;
		}
		
		this.changeGameState();
	}

	public void startGame(){
		
		System.out.println("ChessGame: đợi các player");
		while (this.blackPlayer == null || this.whitePlayer == null){

			try {Thread.sleep(1000);
			
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();}
		}
		
		//thiết lập người đi đầu 
		this.activePlayer = this.whitePlayer;
	
		//game chưa kết thúc
		while(!isGameEndConditionReached()){
			//đợi lượt đi
			waitForMove();
			//đổi lượt đi
			swapActivePlayer();
		}
		
		//ChessConsole.printCurrentGameState(this);
		if(this.gameState == ChessGame.GAME_STATE_END_BLACK_WON){
			System.out.println("Black won!");
			winner winner1 = new winner();
			winner1.setWinner("Black win !!!");
			
		}else if(this.gameState == ChessGame.GAME_STATE_END_WHITE_WON){
			System.out.println("White won!");
			winner winner1 = new winner();
			winner1.setWinner("White win !!!");
			
		}else{
			throw new IllegalStateException("Illegal end state: "+this.gameState);
		}
	}

	@Override
	public void run() {
		this.startGame();
		
	}
}
