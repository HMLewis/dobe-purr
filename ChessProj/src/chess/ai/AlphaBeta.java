package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;

public class AlphaBeta extends Searcher {
	@Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
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
			if(protagonist){
				for(Move m : board.getLegalMoves()){
					Chessboard next = generate(board, m);
					alpha = Math.max(alpha, -evalAlphaBeta(next, eval, depth-1, alpha, beta, false));
					if(alpha >= beta){
						break;
					}
				}
				return alpha;
			}
			else{
				for(Move m : board.getLegalMoves()){
					Chessboard next = generate(board, m);
					beta = Math.min(beta, -evalAlphaBeta(next, eval, depth-1, alpha, beta, true));
					if(alpha >= beta){
						break;
					}
				}
				return beta;
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