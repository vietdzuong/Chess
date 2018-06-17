package twoplayer.logic;

import java.util.ArrayList;
import java.util.List;

import menu.winner;
import twoplayer.gui.ChessGui;
import twoplayer.gui.ConsoleGui;



public class ChessGame{
	private List<Piece> pieces = new ArrayList<Piece>();
	private int gameState = GAME_STATE_WHITE;
	public static final int GAME_STATE_WHITE = 0;
	public static final int GAME_STATE_BLACK = 1;
	public static final int GAME_STATE_BLACK_WON = 2;
	public static final int GAME_STATE_WHITE_WON = 3;
	
	public static final int TEAM_WHITE = 0;
	public static final int TEAM_BLACK = 1;
	
	public static final int GAME_STATE_END = 2;
	private ConsoleGui consoleGui;
	
	private MoveValidator moveValidator;

	private int teamColor;
	
	private ChessGui gui;
	public ChessGame(ChessGui gui,int color){
		this.gui = gui;
		this.teamColor = color;
		addWhite();
		addBlack();
		consoleGui = new ConsoleGui(this);
		this.moveValidator = new MoveValidator(this);
		consoleGui.printCurrentGameState();
	}

	private void addBlack() {
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_ROOK, Piece.ROW_8, Piece.COLUMN_A);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_KNIGHT, Piece.ROW_8, Piece.COLUMN_B);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_BISHOP, Piece.ROW_8, Piece.COLUMN_C);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_QUEEN, Piece.ROW_8, Piece.COLUMN_D);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_KING, Piece.ROW_8, Piece.COLUMN_E);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_BISHOP, Piece.ROW_8, Piece.COLUMN_F);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_KNIGHT, Piece.ROW_8, Piece.COLUMN_G);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_ROOK, Piece.ROW_8, Piece.COLUMN_H);
		
		// pawns
		int currentColumn = Piece.COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_7, currentColumn);
			currentColumn++;
		}
	}

	private void addWhite() {
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_ROOK, Piece.ROW_1, Piece.COLUMN_A);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_KNIGHT, Piece.ROW_1, Piece.COLUMN_B);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_BISHOP, Piece.ROW_1, Piece.COLUMN_C);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_QUEEN, Piece.ROW_1, Piece.COLUMN_D);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_KING, Piece.ROW_1, Piece.COLUMN_E);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_BISHOP, Piece.ROW_1, Piece.COLUMN_F);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_KNIGHT, Piece.ROW_1, Piece.COLUMN_G);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_ROOK, Piece.ROW_1, Piece.COLUMN_H);
		
		// pawns
		int currentColumn = Piece.COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_2, currentColumn);
			currentColumn++;
		}
		
		
	}

	
	private void createAndAddPiece(int color, int type, int row, int column) {
		Piece piece = new Piece(color, type, row, column);
		this.pieces.add(piece);
	}

	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	public List<Piece> getPieces() {
		return pieces;
	}
	//lay manh nguon
	public Piece getNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if( piece.getRow() == row
					&& piece.getColumn() == column
					&& piece.isCaptured() == false ){
				return piece;
			}
		}
		return null;
	}
	public MoveValidator getMoveValidator(){
		return this.moveValidator;
	}
	private boolean isNonCapturedPieceAtLocation(int color, int row, int column) {
		for (Piece piece : this.pieces) {
			if( piece.getRow() == row
					&& piece.getColumn() == column
					&& piece.isCaptured() == false
					&& piece.getColor() == color){
				return true;
			}
		}
		return false;
	}
	public boolean isNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column
					&& piece.isCaptured() == false) {
				return true;
			}
		}
		return false;
	}
	public int getTeamColor() {
		return teamColor;
	}

	public void setTeamColor(int teamColor) {
		this.teamColor = teamColor;
	}

	public boolean movePiece(Move move) {
		
		int sourceRow = move.sourceRow;
		int sourceColumn = move.sourceColumn;
		int targetRow = move.targetRow;
		int targetColumn = move.targetColumn;
		if(!moveValidator.isMoveAllow(new  Move(sourceRow, sourceColumn, targetRow, targetColumn))){
			System.out.println("move invalid");
			return false;
		}
		
		Piece piece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
		
		//check if the move is capturing an opponent piece
		int opponentColor = (piece.getColor()==Piece.COLOR_BLACK?
				Piece.COLOR_WHITE:Piece.COLOR_BLACK);
		// kiem tra xem vi tri tiep theo co cung mau k
		// neu khong cung thi bi an
		if( isNonCapturedPieceAtLocation(opponentColor, targetRow, targetColumn)){
			Piece opponentPiece = getNonCapturedPieceAtLocation( targetRow, targetColumn);
			opponentPiece.isCaptured(true);
		}
		
		piece.setRow(targetRow);
		piece.setColumn(targetColumn);
		
		consoleGui.printCurrentGameState();
		/*if(isGameEndConditionReached()){
			this.gameState = GAME_STATE_END;
			
		}
		else {
			this.changeGameState();
		}*/
		this.changeGameState();
		return true;
	}

		
		

	// kim tra king co bi an chua
	private boolean isGameEndConditionReached() {
		for (Piece piece : this.pieces) {
			if (piece.getType() == Piece.TYPE_KING && piece.isCaptured()) {
				return true;
			}
		}
		return false;
	}
	public void changeGameState() {

		if (this.isGameEndConditionReached()) {

			if (this.gameState == ChessGame.GAME_STATE_BLACK) {
				this.gameState = ChessGame.GAME_STATE_BLACK_WON;
			} else if(this.gameState == ChessGame.GAME_STATE_WHITE){
				this.gameState = ChessGame.GAME_STATE_WHITE_WON;
			}else{
				
			}
		}

		switch (this.gameState) {
			case GAME_STATE_BLACK:
				this.gameState = GAME_STATE_WHITE;
				gui.state.setText("White's turn");
				break;
			case GAME_STATE_WHITE:
				this.gameState = GAME_STATE_BLACK;
				gui.state.setText("Black's turn");
				break;
			case GAME_STATE_WHITE_WON:
				System.out.println("WHITE WON");
				winner winner1 = new winner();
				winner1.setWinner("White win !!!");
				
				break;
			case GAME_STATE_BLACK_WON:
				System.out.println("BLACK WON");
				winner winner2 = new winner();
				winner2.setWinner("Black win !!!");
				
				break;
			default:
				throw new IllegalStateException("Lỗi state" + this.gameState);
		}
	}
}
