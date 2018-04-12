public class Dignity
{
    public static void main(String[] args) throws InterruptedException
    {

        /**
         *  construct players
         */
        CPU player1 = new CPU();
        CPU player2 = new CPU();
        // Human player3 = new Human();
        // NN player4 = new NN();


        /**
         *  construct gameboard
         */
        Board board = new Board(6, 7);
        System.out.println(board.toString());

        /**
         *  play game
         */

        while (!board.isGameOver())
        {
            // player one moves
            while (board.isPlayer1Turn())
            {
                board.addPiece(player1.move(board.getBoardState()));
                /**
                 * player1.move() accepts the current board array as a parameter
                 * and returns an int specifying the column where it wants to move.
                 * board.addPiece() accepts that int as the column in which to place the piece.
                 * The board will validate and check for a win after every move.
                 */


            }
	        System.out.println(board.toString());
	        Thread.sleep(500);

            // then player two moves
            while (!board.isPlayer1Turn())
            {
                board.addPiece(player2.move(board.getBoardState()));
            }
	        System.out.println(board.toString());

	        Thread.sleep(500);

        }

	    System.out.println(board.toString());

	    System.out.println(board.getFinalScore());


    } // end of main()
} // end of dignity
