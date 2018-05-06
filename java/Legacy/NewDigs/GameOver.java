package connect4ever;

/**
 * The GameOver class contains utilities for game overs. It can check
 * to see if the game is over and see if the game was a tie. 
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameOver
{
    private static boolean gameOver;
    private static boolean win;
    private static boolean tieGame;
    
    /**
     * The constructor sets all fields to false.
     */

    public GameOver() {
        gameOver = false;
        win = false;
        tieGame = false;
    }

    /**
     * The isGameOver method gets the status of the game.
     * @return The value of the gameOver variable.
     */

    public static boolean isGameOver()
    {
        checkGameOver();
        return gameOver;
    }
    
    /**
     * The checkGameOver method checks if the game is over using the win
     * method. It also checks to see if the game was a tie.
     */

    public static void checkGameOver()
    {
        win();
        tie();
    }

    /**
     * The win method determines if the game has been won.
     */

    private static void win()
    {
        int nRows = 6;  // the number of rows in the board
        int nCols = 7;  // the number of cols in the board
        int iRow = 0;   // row index number for incrementing
        int iCol = 0;   // col index number for incrementing
        int cl = 4;     // length of chain for indexing

        win = false;

// check for horizontal wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move down the rows
        for (iRow = 0; iRow < nRows && !win; iRow++)
        {
            // move from left to right across row
            for (iCol = 0; iCol <= nCols - cl && !win; iCol++)
            {
                // grab the starting cell value and put in hashset
            	// NOTE: IntelliJ does not like the Legacy.Board.boardState thing.
                int cellVal = Board.boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;
                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i <= 3 ; i++)
                    {
                        if (hs.add(Board.boardState[iRow][iCol + i]) == true)
                            win = false;
                    }
                }
            } // end of moving through columns
        }


// check for vertical wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move across all the cols
        for (iCol = 0; iCol < nCols && !win; iCol++)
        {
            // move from top to bottom through each row in col
            for (iRow = 0; iRow <= nRows - cl && !win; iRow++)
            {
                // grab the starting cell value and put in hashset
                int cellVal = Board.boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;

                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i <= 3; i++)
                    {
                        if (hs.add(Board.boardState[iRow + i][iCol]))
                            win = false;
                    }
                }
            } // end of moving through columns
        }


// check for downhill diagonal wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move across the cols within range
        for (iCol = 0; iCol <= nCols-cl && !win; iCol++)
        {
            // move from top to bottom through row in col in range
            for (iRow = 0; iRow <= nRows - cl && !win; iRow++)
            {
                // grab the starting cell value and put in hashset
                int cellVal = Board.boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;

                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i <= 3; i++)
                    {
                        if (hs.add(Board.boardState[iRow + i][iCol + i]))
                            win = false;
                    }
                }
            } // end of moving through columns
        }

// check for uphill diagonal wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move down the rows, starting within range
        for (iRow = cl-1; iRow < nRows && !win; iRow++)
        {
            // move from top to bottom through row in col in range
            for (iCol = 0; iCol <= nCols - cl && !win; iCol++)
            {
                // grab the starting cell value and put in hashset
                int cellVal = Board.boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;

                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i < cl; i++)
                    {
                        if (hs.add(Board.boardState[iRow - i][iCol + i]))
                            win = false;
                    }
                }
            } // end of moving through columns
        }

        if (win)
            gameOver = true;

    } // end of win

    /**
     * The tie method determines if the game was a tie.
     */

    private static void tie() {
        if (Board.movesLeft==0)
        {
            gameOver = true;
            tieGame = true;
        }
    } // end of tie

    /**
     * @return Whether the game is a tie.
     */

    public boolean isTieGame()
    {
        tie();
        return tieGame;
    } // end of isTieGame

} // end of GameOver (the beginning of the game?)
