import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hjpoe
 * @author pseudodennis
 * @author ianj98
 */

public class Board
{
	private int[][] boardState;
	private int movesLeft;
	private int finalScore; // positive for player1 victory, negative for player2 victory
	private boolean gameOver = false;
	private boolean player1turn;

	private int player1color = 1;
	private int player2color = 2;

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
		this.movesLeft = 42;
		this.gameOver = false;
		this.player1turn = true;
	}


	public boolean isGameOver()
	{
		return gameOver;
	}

	/**
	 * One gameOver to rule them all
	 * @return
	 */
	public void checkGameOver()
	{
		if (this.win())
			this.gameOver = true;
		this.tie();
	}

	/**
	 * The win method determines if the game has been won.
	 * @return A boolean that is true if the game has been won.
	 */


	public boolean win()
	{
		int nRows = 6;  // the number of rows in the board
		int nCols = 7;  // the number of cols in the board
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

		return win;

	} // end of win

	/**
	 * The tie method determines if the game was a tie.
	 * @return A boolean that is true if the game is a tie.
	 */

	public boolean tie()
	{
		boolean tie = false;
		if (this.movesLeft==0)
		{
			tie = true;
		}
		return tie;

	} // end of tie


	/**
	 * In the main method, wrap each player turn in
	 * while(board.isPlayer1Turn()==true/false && board.isGameOver==false)
	 * loops
	 * @param column
	 */
	public void addPiece(int column)
	{
		boolean moveYet = false;

		// within the column, start at the 'bottom' and go 'up'
		for (int i = 5; i>=0 || !moveYet; i--)
		{
			// if the cell is empty, fill it the current player color
			if (this.boardState[i][column] == 0 && !moveYet)
			{
				if (player1turn)
				{
					boardState[i][column] = player1color;
				}
				else
				{
					boardState[i][column] = player2color;
				}


				moveYet = true;
			} // end outer if statement

		} // end for loop

		this.movesLeft--;
		this.checkGameOver();


		// check for win and change score, or else wait for other player to move
		if (this.isGameOver() && player1turn)
		{
			this.finalScore = this.movesLeft;
		}
		else if (this.isGameOver() && !player1turn)
		{
			this.finalScore = this.movesLeft*-1;
		}
		else
		{
			player1turn = !player1turn;
		}


	} // end of addPiece()


	public int[][] getBoardState()
	{
		return boardState;
	}

	public boolean isPlayer1Turn()
	{
		return player1turn;
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

		return boardString;
	}



} // end of Board
