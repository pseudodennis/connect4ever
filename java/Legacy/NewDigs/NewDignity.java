//package connect4ever;
import java.util.Scanner;

public class NewDignity {

	public static void main(String[] args) throws InterruptedException
	{
		
		
		/**
         *  construct players
         */
        //Legacy.CPU player1 = new Legacy.CPU();
        CPU player2 = new CPU();
        Human player1 = new Human();
        // NN player2 = new NN();
        
        Board board; // = new Legacy.Board();
        GameOver gameover = new GameOver();
        boolean err = false; 
        
        
        Scanner reader = new Scanner(System.in);
		int playOrder;
		System.out.print("What is the play order (0 for no human, 1 for human first, 2 for human second)");
		playOrder = reader.nextInt();
		
		if(playOrder == 1) {
			err = hardware_connect.Connect();
			if(err == true)
	        {
	            System.exit(0);
	        }
			
			hardware_connect.starter(true);
		}
		else if(playOrder == 2) {
			err = hardware_connect.Connect();
			if(err == true)
	        {
	            System.exit(0);
	        }
			
			hardware_connect.starter(false);
		}
		else {
			System.out.println("This is a computer vs. computer game");
		}
		
		
        

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
	    
	    hardware_connect.DisConnect();
	    System.out.println(board.getFinalScore());
	} // end of main

} // end of dignity
