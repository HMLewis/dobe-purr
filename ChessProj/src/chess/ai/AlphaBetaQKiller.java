package chess.ai;

import java.util.ArrayList;

import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;
import chess.core.Move;
import chess.core.PieceColor;

public class AlphaBetaQKiller extends Searcher {
	int[][][][] history = new int[2][6][64][64];
	ArrayList<Move> killerWhiteMoves = new ArrayList<Move>();
	ArrayList<Move> killerBlackMoves = new ArrayList<Move>();
	@Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
		//killerWhiteMoves.add(new Move( PieceColor.WHITE, ChessPiece.PAWN, BoardSquare.E2, BoardSquare.E3 ));
		//killerBlackMoves.add(new Move( PieceColor.BLACK, ChessPiece.PAWN, BoardSquare.E7, BoardSquare.E6 ));
		//history[0][0][BoardSquare.E2.getNum()][BoardSquare.E3.getNum()] += 1;
		//history[1][0][BoardSquare.E7.getNum()][BoardSquare.E6.getNum()] += 1;
		setup(board, eval, depth);
		MoveScore result = evalMoves(board, eval, depth);
		tearDown();
		return result;
	}
	
	MoveScore evalMoves(Chessboard board, BoardEval eval, int depth) {
		MoveScore best = null;
		int alpha = -eval.maxValue();
		int beta = eval.maxValue();
		for (Move m: board.getLegalMoves()) {
			Chessboard next = generate(board, m);
			MoveScore result = new MoveScore(-evalAlphaBeta(next, eval, depth - 1, alpha, beta, false), m);//safe to assume you are antagonist
			if (best == null || result.getScore() > best.getScore()) {
				best = result;
				alpha = result.getScore();
			}
		}
		return best;
	}	
	
	int evalAlphaBeta(Chessboard board, BoardEval eval, int depth, int alpha, int beta, boolean protagonist) {
		if (!board.hasKing(board.getMoverColor()) || board.isCheckmate()) {
			return -eval.maxValue();
		} else if (board.isStalemate()) {
			return 0;
		} else if (depth == 0) {	
			return quiescent(alpha, beta, board, eval, 6);
		}
		 else {	
			ArrayList<Move> ret = new ArrayList<Move>();
			if(board.getMoverColor().equals(PieceColor.WHITE)){
				ret.addAll(killerWhiteMoves);
			}
			else{
				ret.addAll(killerBlackMoves);
			}
			if(protagonist){
				for(Move m : ret){
					if(board.getLegalMoves().contains(m)){
						Chessboard next = generate(board, m);
						alpha = Math.max(alpha, -evalAlphaBeta(next, eval, depth-1, alpha, beta, false));
						if(alpha >= beta){
							return alpha;
						}
					}
				}
				for(Move m : board.getLegalMoves()){
					Chessboard next = generate(board, m);
					alpha = Math.max(alpha, -evalAlphaBeta(next, eval, depth-1, alpha, beta, false));
					if(alpha >= beta){
					      if (!m.captures()){					    	
					    	 updateKillerMoves(board, m);					   
					    }
						break;
					}
				}
				return alpha;
			}
			else{
				for(Move m : ret){
					if(board.getLegalMoves().contains(m)){
						Chessboard next = generate(board, m);
						alpha = Math.max(alpha, -evalAlphaBeta(next, eval, depth-1, alpha, beta, false));
						if(alpha >= beta){
							return beta;
						}
					}
				}
				for(Move m : board.getLegalMoves()){
					Chessboard next = generate(board, m);
					beta = Math.min(beta, -evalAlphaBeta(next, eval, depth-1, alpha, beta, true));
					if(alpha >= beta){				  
					     if (!m.captures()){	
					    	updateKillerMoves(board, m);
					     }
						break;
					}
				}
				return beta;
			}
		 }
	}

	public void updateKillerMoves(Chessboard b, Move m){
		if(b.getMoverColor().equals(PieceColor.WHITE)){
			history[0][m.getPiece().getPieceNum(m.getPiece())][m.getStart().fileNum()][m.getStop().fileNum()]++;
			for(int i = 0; i < killerWhiteMoves.size(); i++){				
				if(history[0][m.getPiece().getPieceNum(m.getPiece())][m.getStart().fileNum()][m.getStop().fileNum()] > history[0][m.getPiece().getPieceNum(m.getPiece())][killerWhiteMoves.get(i).getStart().fileNum()][killerWhiteMoves.get(i).getStop().fileNum()]){
					if(killerWhiteMoves.size() == 3){
						killerWhiteMoves.remove(i);
					}
					killerWhiteMoves.add(m);
					i = killerWhiteMoves.size();
				}
			}
		}
		else{
			history[1][m.getPiece().getPieceNum(m.getPiece())][m.getStart().fileNum()][m.getStop().fileNum()]++; 
	    	for(int i = 0; i < killerBlackMoves.size(); i++){
	    		  if(history[1][m.getPiece().getPieceNum(m.getPiece())][m.getStart().fileNum()][m.getStop().fileNum()] > history[1][m.getPiece().getPieceNum(m.getPiece())][killerBlackMoves.get(i).getStart().fileNum()][killerBlackMoves.get(i).getStop().fileNum()]){
	    			  if(killerBlackMoves.size() == 3){
	    				  killerBlackMoves.remove(i);
	    			  }
	    			  killerBlackMoves.add(m);
	    			  i = killerBlackMoves.size();
	    		  }
	    	  }
		}
	}
	
	public int quiescent( int alpha, int beta, Chessboard board, BoardEval eval, int depth ) {
	    if(!board.hasKing(board.getMoverColor()) || board.isCheckmate()){
	    	return -eval.maxValue();
	    }
	    int stand_pat = evaluate(board, eval);
	    if(depth == 0){
	    	return stand_pat;
	    }
	    if( stand_pat >= beta )
	        return beta;
	    if( alpha < stand_pat )
	        alpha = stand_pat;
	    for(Move m: board.getLegalMoves())  {
	    	if(m.captures()){
    			Chessboard next = generate(board,m);
	    		int score = -quiescent(-beta, -alpha, next, eval, depth--);
		        if( score >= beta )
		            return beta;
	        	if( score > alpha )
	        	alpha = score;
	    	}
	    }
	    return alpha;
	}
}
