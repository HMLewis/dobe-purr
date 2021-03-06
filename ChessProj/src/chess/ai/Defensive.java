package chess.ai;

import java.util.EnumMap;


import chess.core.BitBoard;
import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;
import chess.core.PieceColor;


public class Defensive implements BoardEval {
	final static int MAX_VALUE = 20000;
	private EnumMap<ChessPiece,Integer> values = new EnumMap<ChessPiece,Integer>(ChessPiece.class);	
		
	public Defensive() {
		values.put(ChessPiece.PAWN, 100);
		values.put(ChessPiece.BISHOP, 320);
		values.put(ChessPiece.KNIGHT, 325);
		values.put(ChessPiece.ROOK, 500);
		values.put(ChessPiece.QUEEN, 900);
		values.put(ChessPiece.KING, MAX_VALUE);	
	}

	@Override
	public int eval(Chessboard board) {		
		int total = 0;
		PieceColor mover = board.getMoverColor();
		BitBoard moverPieces = board.allPiecesFor(mover);
		BitBoard oppoPieces = board.allPiecesFor(board.getOpponentColor());
		BitBoard moverAttack = board.getMoveMap().getAttackMap();
		BitBoard moverDefend = board.getMoveMap().getDefenseMap();
		BitBoard oppoAttack = board.getOpponentMoveMap().getAttackMap();
		BitBoard oppoDefend = board.getOpponentMoveMap().getDefenseMap();
		BitBoard attackPieces = moverPieces.intersection(moverAttack);
		BitBoard oppoAttackPieces = oppoPieces.intersection(oppoAttack);
		for (BoardSquare s: board.allPieces()) {
			ChessPiece type = board.at(s);			
			if (values.containsKey(type)) {
				if (board.colorAt(s).equals(board.getMoverColor())) {
					total += values.get(type);
				} 
				else{
					total -= values.get(type);
				}
			}
		}	
		if(moverDefend.numPieces() > oppoDefend.numPieces()){
			total += 100;
		}
		if(attackPieces.numPieces() < oppoAttackPieces.numPieces()){
			total += 50;
		}		
		if(moverDefend.numPieces() < oppoDefend.numPieces()){
			total -= 100;
		}
		if(attackPieces.numPieces() > oppoAttackPieces.numPieces()){
			total -= 50;
		}
		total += 15 * (moverDefend.numPieces() - oppoDefend.numPieces());
		return total; 	
	}

	@Override
	public int maxValue() {
		return MAX_VALUE;
	}
	
	public boolean hasValue(ChessPiece piece) {
		return values.containsKey(piece);
	}
	
	public int valueOf(ChessPiece piece) {
		return values.get(piece);
	}
}
