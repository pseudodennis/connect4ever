package connect4ever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hjpoe
 * @author pseudodennis
 * @author ianj98
 */

public class C4Board
{
    private int[][] boardState;
    private int movesLeft;
    private int finalScore; // positive for player1 victory, negative for player2 victory
    private boolean gameOver = false;
    private boolean tiegame = false;
    private boolean player1turn;

    int nRows = 6;  // the number of rows in the board
    int nCols = 7;  // the number of cols in the board

    private int player1color = 1;
    private int player2color = 2;

    private int[] legalActions;

    /**
     * This Constructor initializes a two dimentional array to the size of the
     * board, sets the starting score to 42, and the game over state to false.
     */

    public C4Board()
    {
        this.boardState = new int[nRows][nCols];

        this.movesLeft = 42;
        this.gameOver = false;
        this.player1turn = true;
        this.legalActions = new int[] {0, 1, 2, 3, 4, 5, 6};
    }

    public void reset()
    {
        this.boardState = new int[nRows][nCols];





        this.movesLeft = 42;
        this.gameOver = false;
        this.tiegame = false;
        this.player1turn = true;
        this.finalScore = 0;
        this.legalActions = new int[] {0, 1, 2, 3, 4, 5, 6};
    }

    public int[] getLegalActions()
    {
        return this.legalActions;
    }

    // checks each column to see if the top row is empty; if so, adds that column number to the List of legal actions
    public void updateLegalActions()
    {
        // TODO find a better way to do this; Treeset?
        // count the number of avail cols and create an array that size
        int mt=0;
        for (int c=0; c<nCols; c++)
        {
            if (boardState[0][c] == 0)
            mt++;
        }

        // add the columns to the array
        int[] availCols = new int[mt];
        int i=0;
        for (int c=0; c<nCols; c++)
        {
            if (boardState[0][c] == 0)
            {
                availCols[i] = c;
                i++;
            }
        }
    } // end of updateLegalActions

    public boolean isGameOver()
    {
        return gameOver;
    }

    /*	*//**
 * One gameOver to rule them all
 * @return
 *//*
	public void checkGameOver()
	{
		if (this.win())
			this.gameOver = true;
		if (this.tie())
			this.gameOver = true;
	}*/

    /**
     * The win method determines if the game has been won.
     * @return A boolean that is true if the game has been won.
     */


    public void win()
    {
        int iRow = 0;   // row index number for incrementing
        int iCol = 0;   // col index number for incrementing
        int cl = 4;     // length of chain for indexing
        boolean win = false;

// check for horizontal wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move down the rows
        for (iRow = 0; iRow < nRows && !win; iRow++)
        {
            // move from left to right across row
            for (iCol = 0; iCol <= nCols - cl && !win; iCol++)
            {
                // grab the starting cell value and put in hashset
                int cellVal = boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;
                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i <= 3 ; i++)
                    {
                        if (hs.add(boardState[iRow][iCol + i])==true)
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
                int cellVal = boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;

                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i <= 3; i++)
                    {
                        if (hs.add(boardState[iRow + i][iCol]))
                            win = false;
                    }
                }
            } // end of moving through columns
        }


// check for downhill diag wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move across the cols within range
        for (iCol = 0; iCol <= nCols-cl && !win; iCol++)
        {
            // move from top to bottom through row in col in range
            for (iRow = 0; iRow <= nRows - cl && !win; iRow++)
            {
                // grab the starting cell value and put in hashset
                int cellVal = boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;

                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i <= 3; i++)
                    {
                        if (hs.add(boardState[iRow + i][iCol + i]))
                            win = false;
                    }
                }
            } // end of moving through columns
        }

// check for uphill diag wins

        // move through each cell, unless the cells are within 4 spaces of nCols

        // move down the rows, starting within range
        for (iRow = cl-1; iRow < nRows && !win; iRow++)
        {
            // move from top to bottom through row in col in range
            for (iCol = 0; iCol <= nCols - cl && !win; iCol++)
            {
                // grab the starting cell value and put in hashset
                int cellVal = boardState[iRow][iCol];
                if (cellVal != 0)
                {
                    Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
                    win = true;

                    // iterate through the next three cells, if add==true, win=false
                    for (int i = 1; i < cl; i++)
                    {
                        if (hs.add(boardState[iRow - i][iCol + i]))
                            win = false;
                    }
                }
            } // end of moving through columns
        }

        if (win)
            this.gameOver = true;

    } // end of win

    /**
     * The tie method determines if the game was a tie.
     * @return A boolean that is true if the game is a tie.
     */

    public void tie()
    {
        if (this.movesLeft==0)
        {
            this.gameOver = true;
            this.tiegame = true;
        }

    } // end of tie

    public boolean isTiegame()
    {
        return tiegame;
    }

    /**
     * In the main method, wrap each player turn in
     * while(board.isPlayer1Turn()==true/false && board.isGameOver==false)
     * loops
     * @param column
     */
    public void addPiece(int column, int playerColor)
    {
        boolean moveYet = false;

        // within the column, start at the 'bottom' and go 'up'
        for (int i = 5; i>=0 || !moveYet; i--)
        {
            // if the cell is empty, fill it the current player color
            if (this.boardState[i][column] == 0 && !moveYet)
            {
                boardState[i][column] = playerColor;
                moveYet = true;
                this.updateLegalActions();
            }

        }

        this.movesLeft--;
        this.win();
        this.tie();
        this.updateLegalActions();


        // check for win and change score, or else wait for other player to move
        if (this.isGameOver())
        {
            this.finalScore = this.movesLeft;
        }
    } // end of addPiece()


    public int[][] getBoardState()
    {
        return this.boardState;
    }


    public int getFinalScore()
    {
        return finalScore;
    }



    public String toString()
    {
        int iCol = 0;
        int iRow = 0;
        int nCols = 7;
        int nRows = 6;
        String boardString = "";

        for (iRow=0; iRow<nRows; iRow++)
        {
            for (iCol = 0; iCol < nCols; iCol++)
            {
                boardString = boardString + "[" + boardState[iRow][iCol] + "]";
            }
            boardString = boardString + "\n";
        }

        return boardString.replace("0", " ");
    }




    public double[] encode()
    {
        double[] flatBoard = new double[42];

        int k=0;
        for (int i = 0; i <= 5; i++)
        {
            for (int j = 0; j <= 6; j++)
            {
                flatBoard[k] = (double)this.boardState[i][j];
                k++;
            }
        } // end processing arrays

        return flatBoard;
    }

} // end of Board
