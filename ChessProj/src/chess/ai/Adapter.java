package chess.ai;

import java.util.EnumMap;


import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;
import chess.ai.Aggressive;
import chess.ai.Defensive;
import chess.ai.TablesAndMaterial;

public class Adapter implements BoardEval {
        final static int MAX_VALUE = 20000;
        private EnumMap<ChessPiece,Integer> values = new EnumMap<ChessPiece,Integer>(ChessPiece.class);
        private Aggressive aggressor = new Aggressive();
        private Defensive defender = new Defensive();
        private TablesAndMaterial tabmat = new TablesAndMaterial();
        
        public Adapter(){
        values.put(ChessPiece.PAWN, 100);
        values.put(ChessPiece.BISHOP, 320);
        values.put(ChessPiece.KNIGHT, 325);
        values.put(ChessPiece.ROOK, 500);
        values.put(ChessPiece.QUEEN, 900);
        values.put(ChessPiece.KING, MAX_VALUE);

        }

        @Override
        public int eval(Chessboard board) {
        	int Moverscore = 0;
        	for (BoardSquare s: board.allPieces()) {
    			ChessPiece type = board.at(s);			
    			if (values.containsKey(type)) {
    				if (board.colorAt(s).equals(board.getMoverColor())) {
    					Moverscore += values.get(type);
    				} 
    				else{
    					Moverscore -= values.get(type);
    				}
    			}
    		}
        	int OppoScore = 0;
        	for (BoardSquare s: board.allPieces()) {
    			ChessPiece type = board.at(s);			
    			if (values.containsKey(type)) {
    				if (board.colorAt(s).equals(board.getOpponentColor())) {
    					OppoScore += values.get(type);
    				} 
    				else{
    					OppoScore -= values.get(type);
    				}
    			}
    		}
                if(Moverscore <= OppoScore){
                    return defender.eval(board);
                }
                else{
                    return aggressor.eval(board);
                }
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
