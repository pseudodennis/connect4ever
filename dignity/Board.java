import java.util.Arrays;

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


	/**
	 * One gameOver to rule them all
	 * @return
	 */
	public boolean isGameOver()
	{
		this.gameOver = false;
		if (this.win() || this.tie())
			this.gameOver = true;
		return this.gameOver;
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
		if (this.movesLeft<1)
		{
			gameOver = true;
		}
		return gameOver;
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
			if (boardState[i][column] == 0)
			{
				if (player1turn)
				{
					boardState[i][column] = player1color;
				}
				else
				{
					boardState[i][column] = player2color;
				}

				this.movesLeft--;
				moveYet = true;
			}
		}

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


} // end of Board
