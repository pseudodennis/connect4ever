// TODO update javadoc for block of 10 setters and getters

package connect4ever;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the game board environment.
 * Has methods pertaining to score-keeping, making moves, and checking for wins and ties.
 */

public class C4Board
{
    private int[][] boardState;													// the connect four game board as a int[][]
	private int[] legalActions;													// the list of legal actions, for move validation
	private int movesLeft;														// starts with 42, decrements after each successful .addPiece()
    private int finalScore;														// calculated from movesLeft after a win or tie
    private boolean gameOver = false;
    private boolean tiegame = false;
    private int dqnValid;														// number of valid moves the DQN has made this game, for tuning/debugging
    private int dqnInvalid;														// number of invalid moves...

    private int nRows = 6;  													// the number of rows in the board
    private int nCols = 7;  													// the number of cols in the board

    /**
     * Constructor initializes a two dimensional array and sets relevant data for starting values
     */
    public C4Board()
    {
        this.boardState = new int[nRows][nCols];
        this.movesLeft = 42;													// TODO calculate from array size
        this.gameOver = false;
        this.legalActions = new int[] {0, 1, 2, 3, 4, 5, 6};
        this.dqnInvalid=0;
        this.dqnValid=0;
    }

	/**
	 * Setter for gameOver
	 * @param gameOver A boolean that indicates if the game is over.
	 */
	public void setGameOver(boolean gameOver)
	{
		this.gameOver = gameOver;
	}

	/**
	 * Getter for gameOver
	 * @return boolean True if the game is over.
	 */
	public boolean isGameOver()
	{
		return gameOver;
	}

	/**
	 * Getter for dqnValid accumulator
	 * Useful for tracking ratio of in/valid steps taken.
	 * @return int of valid moves made by the dqn so far.
	 */	public int getDqnValid()
	{
		return dqnValid;
	}

	/**
	 * Setter for the dqnValid accumulator.
	 * Useful for tracking ratio of in/valid steps taken.
	 * @param dqnValid int of valid moves made by the dqn so far.
	 */
	public void setDqnValid(int dqnValid)
	{
		this.dqnValid = dqnValid;
	}

	/**
	 * Getter for the dqnInvalid accumulator.
	 * Useful for stopping the training epoch after n invalid moves, etc.
	 * @return int The invalid moves made by the dqn so far.
	 */
	public int getDqnInvalid()
	{
		return dqnInvalid;
	}

	/**
	 * Setter for the dqnInvalid accumulator.
	 * Useful for stopping the training epoch after n invalid moves, etc.
	 * @param dqnInvalid The int of invalid moves made by the dqn so far.
	 */
	public void setDqnInvalid(int dqnInvalid)
	{
		this.dqnInvalid = dqnInvalid;
	}

	/**
	 * Getter for legalActions
	 * @return int[] The list of column indices that are not full.
	 */
	public int[] getLegalActions()
	{
		return this.legalActions;
	}

	/**
	 * Getter for boardState
	 * @return int[][] The current board arrangement
	 */
	public int[][] getBoardState()
	{
		return this.boardState;
	}

	/**
	 * Getter for finalScore.
	 * @return int The final score, calculated from moves made
	 */
	public int getFinalScore()
	{
		return finalScore;
	}

	/**
	 * Getter for tieGame
	 * @return boolean true if game has ended in tie.
	 */
	public boolean isTiegame()
	{
		return tiegame;
	}

	/**
	 * Resets the board. Needed (ultimately) for the MDP interface.
	 */
    public void reset()
    {
        this.boardState = new int[nRows][nCols];
        this.movesLeft = 42;
        this.gameOver = false;
        this.tiegame = false;
        this.finalScore = 0;
        this.legalActions = new int[] {0, 1, 2, 3, 4, 5, 6};
        this.dqnInvalid=0;
        this.dqnValid=0;
    } // end of .reset()



	/**
	 * Checks each column to see if the top row is empty;
	 * if so, adds that column number to the List of legal actions
	 */
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
        this.legalActions = new int[availCols.length];
        this.legalActions = availCols;
    } // end of updateLegalActions

    /**
     * The win method determines if the game has been won.
	 * Cycles through each array element, adds that value to a HashSet (unless 0),
	 * then checks the next three array elements to see if they can also be added to the HashSet.
	 * If all of them cannot, it returns a win.
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

    } // end of .win()

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

    } // end of .tie()

	/**
	 * Adds a piece to the given column in the game board,
	 * then checks for win, tie, updates legal actions,
	 * and assigns a final score if win
	 * @param column The column into which the player decides to move
	 * @param playerColor The piece color of the player, 1 or 2
	 * TODO try different player numbers (1, -1, etc) to see if the learning rate is affected
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

        // update moves left, legal actions, and check for win, tie
        this.movesLeft--;
        this.win();
        this.tie();
        this.updateLegalActions();

        // check for win and change score, or else wait for other player to move
        if (this.isGameOver())
        {
            this.finalScore = this.movesLeft;
        }
    } // end of .addPiece()


	/**
	 * Represents the current board state in a pretty table.
	 * Depicts player 1 as "O", player 2 as "O".
	 * The first column is "1", so column-index values will need to be offset when printing elsewhere
	 * @return String representation of the board
	 */
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

        String columnHeaders = " 1  2  3  4  5  6  7 \n";
        boardString = boardString.replace("0", " ");
        boardString = boardString.replace("1", "X");
        boardString = boardString.replace("2", "O");
        boardString = columnHeaders + boardString;

        return boardString;
    } // end of .toString()


	/**
	 * Since the MDP interface can only accept a 1D array, this method flattens the 2D board to a 1D double[]
	 * TODO figure out how to read the board as an array with 3 layers of depth (empty, red, black), like CNNs do with RGB arrays
	 * @return double[] the flattened board state
	 */
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
    } // end of .encode()

} // end of C4Board
