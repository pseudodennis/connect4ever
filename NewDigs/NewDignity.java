package connect4ever;

public class NewDignity {

	public static void main(String[] args) throws InterruptedException
	{
		/**
         *  construct players
         */
        CPU player1 = new CPU();
        CPU player2 = new CPU();
        // Human player3 = new Human();
        // NN player4 = new NN();
        
        Board board;
        GameOver gameover = new GameOver();

	    /**
	     *  construct gameboard
	     */
	    board = new Board(6, 7);
	    System.out.println(board.toString());
	    
	    /**
	     *  play game
	     */
	    
	    while(!gameover.isGameOver())
	    {
	    	// player one moves
	    	if(board.isPlayer1Turn() && !gameover.isGameOver())
	    	{
	    		board.addPiece(player1.move(board.getBoardState()));
	    		/**
			     * player1.move() accepts the current board array as a parameter
			     * and returns an int specifying the column where it wants to move.
			     * board.addPiece() accepts that int as the column in which to place the piece.
			     * The board will validate and check for a win after every move.
			     */
	    	}
	    	// System.out.println(board.toString());
	    	Thread.sleep(00);
	    	
	    	// then player two moves
	    	if(!board.isPlayer1Turn() && !gameover.isGameOver())
	    	{
	    		board.addPiece(player2.move(board.getBoardState()));
	    	}
	    	
	    	System.out.println(board.toString());
	    	Thread.sleep(00);
	    	
	    }
	    
	    System.out.println(board.getFinalScore());
		
	} // end of main

} // end of dignity
