package chess.ai;

import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.text.html.HTMLDocument.Iterator;

import chess.core.BitBoard;
import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;
import chess.core.PieceColor;


public class GlorifiedValue implements BoardEval {
	final static int MAX_VALUE = 1000;
	private EnumMap<ChessPiece,Integer> values = new EnumMap<ChessPiece,Integer>(ChessPiece.class);
	
	public GlorifiedValue() {
		values.put(ChessPiece.BISHOP, 3);
		values.put(ChessPiece.KNIGHT, 3);
		values.put(ChessPiece.PAWN, 1);
		values.put(ChessPiece.QUEEN, 9);
		values.put(ChessPiece.ROOK, 5);
		values.put(ChessPiece.KING, MAX_VALUE);
	}

	@Override
	public int eval(Chessboard board) {
		int total = 0;
		PieceColor mover = board.getMoverColor();
		BitBoard moverPieces = board.allPiecesFor(mover);
		BitBoard moverAttack = board.getMoveMap().getAttackMap();
		BitBoard moverDefend = board.getMoveMap().getDefenseMap();
		BitBoard oppoAttack = board.getOpponentMoveMap().getAttackMap();
		BitBoard oppoDefend = board.getOpponentMoveMap().getDefenseMap();
		BitBoard attackPieces = moverPieces.intersection(moverAttack);
		for (BoardSquare s: board.allPieces()) {
			ChessPiece type = board.at(s);			
			if (values.containsKey(type)) {	
				int curMoverPieces = 0; 
				int oppMoverPieces = 0; 
				java.util.Iterator<BoardSquare> it = moverPieces.iterator();
				BitBoard oppoPieces = board.allPiecesFor(board.getOpponentColor());
				java.util.Iterator<BoardSquare> it2 = oppoPieces.iterator();			
				while(it.hasNext()){
					if(it.equals(type)){
						curMoverPieces++;
					}
					it.next();
				}while(it2.hasNext()){				
					if(it2.equals(type)){
						oppMoverPieces++;
					}
					it2.next();
				}				
				if (board.colorAt(s).equals(mover)) {
					total += values.get(type) * (curMoverPieces - oppMoverPieces);
					if(board.getMoveMap().canAttack(s)){
						total += (5 * values.get(type));
					}
				} 
			}
		}
		if(moverDefend.numPieces() < oppoDefend.numPieces()){
			total -= 90;
		}
		else{
			total += 45;
		}
		
		if(attackPieces.numPieces() > oppoAttack.numPieces()){
			total -= 90;
		}
		else{
			total += 45;
		}
		total += 3 * (moverDefend.numPieces() - oppoDefend.numPieces());
		int curMoves = board.getMoveMap().getTotalPossibleMoves() + board.getParent().getOpponentMoveMap().getTotalPossibleMoves();
		int oppMoves = board.getOpponentMoveMap().getTotalPossibleMoves() + board.getParent().getMoveMap().getTotalPossibleMoves();
		int mobility = (int) (0.1 * (curMoves - oppMoves));
		return total + mobility;
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
