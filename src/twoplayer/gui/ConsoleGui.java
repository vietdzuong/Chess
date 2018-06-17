package twoplayer.gui;

import twoplayer.logic.ChessGame;
import twoplayer.logic.Piece;

public class ConsoleGui {

	private ChessGame chessGame;

	public ConsoleGui(ChessGame chessGame) {

		// create a new chess game
		//
		this.chessGame = chessGame;
	}


	/**
	 * Print current game board and game state information.
	 */
	public void printCurrentGameState() {

		System.out.println("  a  b  c  d  e  f  g  h  ");
		for (int row = Piece.ROW_8; row >= Piece.ROW_1; row--) {

			System.out.println(" +--+--+--+--+--+--+--+--+");
			String strRow = (row + 1) + "|";
			for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_H; column++) {
				Piece piece = this.chessGame.getNonCapturedPieceAtLocation(row, column);
				String pieceStr = getNameOfPiece(piece);
				strRow += pieceStr + "|";
			}
			System.out.println(strRow + (row + 1));
		}
		System.out.println(" +--+--+--+--+--+--+--+--+");
		System.out.println("  a  b  c  d  e  f  g  h  ");

		String turnColor =
			(chessGame.getGameState() == ChessGame.GAME_STATE_BLACK ? "black" : "white");
		System.out.println("turn: " + turnColor);

	}

	/**
	 * Returns the name of the specified piece. The name is based on color and
	 * type.
	 * 
	 * The first letter represents the color: B=black, W=white, ?=unknown
	 * 
	 * The second letter represents the type: B=Bishop, K=King, N=Knight,
	 * P=Pawn, Q=Queen, R=Rook, ?=unknown
	 * 
	 * A two letter empty string is returned in case the specified piece is null
	 * 
	 * @param piece a chess piece
	 * @return string representation of the piece or a two letter empty string
	 *         if the specified piece is null
	 */
	private String getNameOfPiece(Piece piece) {
		if (piece == null)
			return "  ";

		String strColor = "";
		switch (piece.getColor()) {
			case Piece.COLOR_BLACK:
				strColor = "B";
				break;
			case Piece.COLOR_WHITE:
				strColor = "W";
				break;
			default:
				strColor = "?";
				break;
		}

		String strType = "";
		switch (piece.getType()) {
			case Piece.TYPE_BISHOP:
				strType = "B";
				break;
			case Piece.TYPE_KING:
				strType = "K";
				break;
			case Piece.TYPE_KNIGHT:
				strType = "N";
				break;
			case Piece.TYPE_PAWN:
				strType = "P";
				break;
			case Piece.TYPE_QUEEN:
				strType = "Q";
				break;
			case Piece.TYPE_ROOK:
				strType = "R";
				break;
			default:
				strType = "?";
				break;
		}

		return strColor + strType;
	}

}
