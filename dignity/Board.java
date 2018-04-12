import java.util.Arrays;

/**
 * @author hjpoe
 * @author pseudodennis
 * @author ianj98
 */

public class Board
{
    private int[][] boardState;
    private int score;
    private boolean col1full;
    private boolean col2full;
    private boolean col3full;
    private boolean col4full;
    private boolean col5full;
    private boolean col6full;
    private boolean col7full;

    /**
     * This Constructor initializes a two dimentional array to the size of the
     * board, sets the starting score to 42, and the game over state to false.
     * @param rows The number of rows in the 2D array.
     * @param cols The number of columns in the 2D array.
     */

    public Board(int rows, int cols)
    {
      this.boardState = new int[rows][cols];
      this.score = 42;
      this.gameOver = false;
    }

    /**
     * The win method determines if the game has been won.
     * @return A boolean that is true if the game has been won.
     */

    public boolean win()
    {
      boolean win = false;
      // Check algorithms go here.
      return win;
    } // end of win

    /**
     * The tie method determines if the game was a tie.
     * @return A boolean that is true if the game is a tie.
     */

    public boolean tie()
    {
      boolean gameOver = false;
      if (score<1) {
        gameOver = true;
        return gameOver;
      }
    } // end of tie
} // end of Board
