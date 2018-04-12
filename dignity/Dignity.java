
public class Dignity
{
  public static void main(String[] args)
  {

    boolean gamewin = false;
    boolean tiegame = false;
    
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


    /**
     *  play game
     */
    while (!gamewin && !tiegame)
    {  
      board = board.setState(player1.move(board));
      gamewin = board.win();
      tiegame = board.tie();
      
      board = board.setState(player2.move(board));  
      gamewin = board.win();
      tiegame = board.tie();
    }


  } // end of main()
} // end of dignity
