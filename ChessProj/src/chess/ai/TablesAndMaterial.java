package chess.ai;

import java.util.EnumMap;


import chess.core.BitBoard;
import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;
import chess.core.PieceColor;


public class TablesAndMaterial implements BoardEval {
	final static int MAX_VALUE = 20000;
	private EnumMap<ChessPiece,Integer> values = new EnumMap<ChessPiece,Integer>(ChessPiece.class);
	
	private static short[] PawnTable = new short[]
			{
			  0,  0,  0,  0,  0,  0,  0,  0,
			 50, 50, 50, 50, 50, 50, 50, 50,
			 10, 10, 20, 30, 30, 20, 10, 10,
			  5,  5, 10, 27, 27, 10,  5,  5,
			  0,  0,  0, 25, 25,  0,  0,  0,
			  5, -5,-10,  0,  0,-10, -5,  5,
			  5, 10, 10,-25,-25, 10, 10,  5,
			  0,  0,  0,  0,  0,  0,  0,  0
			};
	private static short[] BPawnTable = new short[]
			{
			  0,  0,  0,  0,  0,  0,  0,  0,
			  5, 10, 10,-25,-25, 10, 10,  5,			 
			  5, -5,-10,  0,  0,-10, -5,  5,
			  0,  0,  0, 25, 25,  0,  0,  0,
			  5,  5, 10, 27, 27, 10,  5,  5,
			  10, 10, 20, 30, 30, 20, 10, 10,
			  50, 50, 50, 50, 50, 50, 50, 50,
			  0,  0,  0,  0,  0,  0,  0,  0
			};
	
			private static short[] KnightTable = new short[]
			{
			 -50,-40,-30,-30,-30,-30,-40,-50,
			 -40,-20,  0,  0,  0,  0,-20,-40,
			 -30,  0, 10, 15, 15, 10,  0,-30,
			 -30,  5, 15, 20, 20, 15,  5,-30,
			 -30,  0, 15, 20, 20, 15,  0,-30,
			 -30,  5, 10, 15, 15, 10,  5,-30,
			 -40,-20,  0,  5,  5,  0,-20,-40,
			 -50,-40,-20,-30,-30,-20,-40,-50
			};
			
			private static short[] BKnightTable = new short[]
					{
					 -50,-40,-30,-30,-30,-30,-40,-50,
					 -40,-20,  0,  5,  5,  0,-20,-40,
					 -30,  5, 10, 15, 15, 10,  5,-30,
					 -30,  0, 15, 20, 20, 15,  0,-30,
					 -30,  5, 15, 20, 20, 15,  5,-30,					 
					 -30,  0, 10, 15, 15, 10,  0,-30,
					 -40,-20,  0,  0,  0,  0,-20,-40,
					 -50,-40,-20,-30,-30,-20,-40,-50
					};

			private static short[] BishopTable = new short[]
			{
			 -20,-10,-10,-10,-10,-10,-10,-20,
			 -10,  0,  0,  0,  0,  0,  0,-10,
			 -10,  0,  5, 10, 10,  5,  0,-10,
			 -10,  5,  5, 10, 10,  5,  5,-10,
			 -10,  0, 10, 10, 10, 10,  0,-10,
			 -10, 10, 10, 10, 10, 10, 10,-10,
			 -10,  5,  0,  0,  0,  0,  5,-10,
			 -20,-10,-40,-10,-10,-40,-10,-20
			};
			
			private static short[] BBishopTable = new short[]
					{
					 
					 -20,-10,-40,-10,-10,-40,-10,-20,
					 -10,  5,  0,  0,  0,  0,  5,-10,
					 -10, 10, 10, 10, 10, 10, 10,-10,
					 -10,  0, 10, 10, 10, 10,  0,-10,
					 -10,  5,  5, 10, 10,  5,  5,-10,					 
					 -10,  0,  5, 10, 10,  5,  0,-10,
					 -10,  0,  0,  0,  0,  0,  0,-10,
					 -20,-10,-10,-10,-10,-10,-10,-20
					};

			private static short[] KingTable = new short[]
			{
			  -30,-40,-40,-50,-50,-40,-40,-30,
			  -30,-40,-40,-50,-50,-40,-40,-30,
			  -30,-40,-40,-50,-50,-40,-40,-30,
			  -30,-40,-40,-50,-50,-40,-40,-30,
			  -20,-30,-30,-40,-40,-30,-30,-20,
			  -10,-20,-20,-20,-20,-20,-20,-10, 
			   20, 20,  0,  0,  0,  0, 20, 20,
			   20, 30, 10,  0,  0, 10, 30, 20
			};
			private static short[] BKingTable = new short[]
			{
			    20, 30, 10,  0,  0, 10, 30, 20,
			    20, 20,  0,  0,  0,  0, 20, 20,
			   -10,-20,-20,-20,-20,-20,-20,-10, 
			   -20,-30,-30,-40,-40,-30,-30,-20,
			   -30,-40,-40,-50,-50,-40,-40,-30,
			   -30,-40,-40,-50,-50,-40,-40,-30,
			   -30,-40,-40,-50,-50,-40,-40,-30,
			   -30,-40,-40,-50,-50,-40,-40,-30
			};

			private static short[] KingTableEndGame = new short[]
			{
			 -50,-40,-30,-20,-20,-30,-40,-50,
			 -30,-20,-10,  0,  0,-10,-20,-30,
			 -30,-10, 20, 30, 30, 20,-10,-30,
			 -30,-10, 30, 40, 40, 30,-10,-30,
			 -30,-10, 30, 40, 40, 30,-10,-30,
			 -30,-10, 20, 30, 30, 20,-10,-30,
			 -30,-30,  0,  0,  0,  0,-30,-30,
			 -50,-30,-30,-30,-30,-30,-30,-50
			};
			
			private static short[] BKingTableEndGame = new short[]
					{
					 
					 -50,-30,-30,-30,-30,-30,-30,-50,
					 -30,-30,  0,  0,  0,  0,-30,-30,
					 -30,-10, 20, 30, 30, 20,-10,-30,					 
					 -30,-10, 30, 40, 40, 30,-10,-30,
					 -30,-10, 30, 40, 40, 30,-10,-30,
					 -30,-10, 20, 30, 30, 20,-10,-30,
					 -30,-20,-10,  0,  0,-10,-20,-30,
					 -50,-40,-30,-20,-20,-30,-40,-50
					};
			
			private static short[] RookTable = new short[]
			{
				  0,  0,  0,  0,  0,  0,  0,  0,
				  5, 10, 10, 10, 10, 10, 10,  5,
				 -5,  0,  0,  0,  0,  0,  0, -5,
				 -5,  0,  0,  0,  0,  0,  0, -5,
				 -5,  0,  0,  0,  0,  0,  0, -5,
				 -5,  0,  0,  0,  0,  0,  0, -5,
				 -5,  0,  0,  0,  0,  0,  0, -5,
				  0,  0,  0,  5,  5,  0,  0,  0
			};
			
			private static short[] BRookTable = new short[]
					{						  
						  0,  0,  0,  5,  5,  0,  0,  0,
						 -5,  0,  0,  0,  0,  0,  0, -5,
						 -5,  0,  0,  0,  0,  0,  0, -5,
						 -5,  0,  0,  0,  0,  0,  0, -5,
						 -5,  0,  0,  0,  0,  0,  0, -5,
						 -5,  0,  0,  0,  0,  0,  0, -5,
						  5, 10, 10, 10, 10, 10, 10,  5,
						  0,  0,  0,  0,  0,  0,  0,  0
						  
					};
			
			private static short[] QueenTable = new short[] 
			{			
				-20,-10,-10, -5, -5,-10,-10,-20,
				-10,  0,  0,  0,  0,  0,  0,-10,
				-10,  0,  5,  5,  5,  5,  0,-10,
				 -5,  0,  5,  5,  5,  5,  0, -5,
				  0,  0,  5,  5,  5,  5,  0, -5,
				-10,  5,  5,  5,  5,  5,  0,-10,
				-10,  0,  5,  0,  0,  0,  0,-10,
				-20,-10,-10, -5, -5,-10,-10,-20			
			};
			
			private static short[] BQueenTable = new short[] 
					{			
						-20,-10,-10, -5, -5,-10,-10,-20,
						-10,  0,  5,  0,  0,  0,  0,-10,						
						-10,  0,  5,  5,  5,  5,  5,-10,
						 -5,  0,  5,  5,  5,  5,  0, -5,
						  0,  0,  5,  5,  5,  5,  0, -5,
						-10,  0,  5,  5,  5,  5,  0,-10,
						-10,  0,  0,  0,  0,  0,  0,-10,
						-20,-10,-10, -5, -5,-10,-10,-20			
					};
	
	public TablesAndMaterial() {
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
		boolean isEndGame = false;
		PieceColor mover = board.getMoverColor();
		BitBoard moverPieces = board.allPiecesFor(mover);
		BitBoard moverAttack = board.getMoveMap().getAttackMap();
		BitBoard moverDefend = board.getMoveMap().getDefenseMap();
		BitBoard oppoAttack = board.getOpponentMoveMap().getAttackMap();
		BitBoard oppoDefend = board.getOpponentMoveMap().getDefenseMap();
		BitBoard attackPieces = moverPieces.intersection(moverAttack);
		if(board.allPiecesFor(mover).numPieces() < 4){
			isEndGame = true;
		}
		for (BoardSquare s: board.allPieces()) {
			ChessPiece type = board.at(s);			
			if (values.containsKey(type)) {
				if (board.colorAt(s).equals(board.getMoverColor())) {
					total += values.get(type);
				} 
				else{
					total -= values.get(type);
				}
				if(board.getMoverColor().equals(PieceColor.WHITE)){
					if(type.equals(ChessPiece.PAWN)){
						total += PawnTable[s.getNum()];
					}
					if(type.equals(ChessPiece.BISHOP)){
						total += BishopTable[s.getNum()];
					}
					if(type.equals(ChessPiece.KNIGHT)){
						total += KnightTable[s.getNum()];
					}
					if(type.equals(ChessPiece.KING) && !isEndGame){
						total += KingTable[s.getNum()];
					}
					if(type.equals(ChessPiece.QUEEN)){
						total += QueenTable[s.getNum()];
					}
					if(type.equals(ChessPiece.ROOK)){
						total += RookTable[s.getNum()];
					}
					if(type.equals(ChessPiece.KING) && isEndGame){
						total += KingTableEndGame[s.getNum()];
					}	
				}
				else{
					if(type.equals(ChessPiece.PAWN)){
						total += BPawnTable[s.getNum()];
					}
					if(type.equals(ChessPiece.BISHOP)){
						total += BBishopTable[s.getNum()];
					}
					if(type.equals(ChessPiece.KNIGHT)){
						total += BKnightTable[s.getNum()];						
					}
					if(type.equals(ChessPiece.KING) && !isEndGame){
						total += BKingTable[s.getNum()];
					}
					if(type.equals(ChessPiece.QUEEN)){
						total += BQueenTable[s.getNum()];
					}
					if(type.equals(ChessPiece.ROOK)){
						total += BRookTable[s.getNum()];
					}
					if(type.equals(ChessPiece.KING) && isEndGame){
						total += BKingTableEndGame[s.getNum()];
					}
				}
			}
		}
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
