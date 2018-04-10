package chess.core;

public enum ChessPiece {
	PAWN {
		public char symbol() {return 'P';}
		public boolean castsShadow() {return false;}
	}, QUEEN {
		public char symbol() {return 'Q';}
		public boolean castsShadow() {return true;}
	}, ROOK {
		public char symbol() {return 'R';}
		public boolean castsShadow() {return true;}
	}, BISHOP {
		public char symbol() {return 'B';}
		public boolean castsShadow() {return true;}
	}, KNIGHT {
		public char symbol() {return 'N';}
		public boolean castsShadow() {return false;}
	}, KING {
		public char symbol() {return 'K';}
		public boolean castsShadow() {return false;}
	}, EMPTY {
		public char symbol() {return '-';}
		public boolean castsShadow() {return false;}
	};
	abstract public boolean castsShadow();
	abstract public char symbol();
	
	public int getPieceNum(ChessPiece piece){
		if(piece == ChessPiece.PAWN){
			return 0;
		}if(piece == ChessPiece.QUEEN){
			return 1;
		}if(piece == ChessPiece.ROOK){
			return 2;
		}if(piece == ChessPiece.BISHOP){
			return 3;
		}if(piece == ChessPiece.KNIGHT){
			return 4;
		}if(piece == ChessPiece.KING){
			return 5;
		}
		return 0;
	}
	
	public final static ChessPiece[] slidingPieces = new ChessPiece[]{QUEEN, ROOK, BISHOP};
}