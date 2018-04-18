package connect4ever;

/* **********************************************************
 * IntelliJ didn't like it when I used the methods like this:
 * GameOver.checkGameOver(), so I added these. Now that I'm 
 * checking it out in Eclipse, I see that it thinks it's fine
 * if I use it like that. I'm leaving these here, just in case.
 * import static connect4ever.GameOver.checkGameOver;
 * import static connect4ever.GameOver.isGameOver;
*/

public class Board
{
    protected static int[][] boardState;
    protected static int movesLeft;
    private static int finalScore; // positive for player1 victory, negative for player2 victory
    private static boolean player1turn;

    /**
     * This Constructor initializes a two dimentional array to the size of the
     * board, sets the starting score to 42, sets the game over state to false,
     * and makes it player1's turn first.
     * @param rows The number of rows in the 2D array.
     * @param cols The number of columns in the 2D array.
     */

    public Board(int rows, int cols)
    {
        boardState = new int[rows][cols];
        movesLeft = 42;
        player1turn = true;
    }

    /**
     * In the main method, wrap each player turn in
     * while(board.isPlayer1Turn()==true/false && board.isGameOver==false)
     * loops
     * @param column The column to add a piece to.
     */
    public static void addPiece(int column)
    {
        int player1color = 1;
        int player2color = 2;

        boolean moveYet = false;

        // within the column, start at the 'bottom' and go 'up'
        for (int i = 5; i>=0 || !moveYet; i--)
        {
            // if the cell is empty, fill it the current player color
            if (boardState[i][column] == 0 && !moveYet)
            {
                if (player1turn)
                {
                    boardState[i][column] = player1color;
                    moveYet = true;
                }
                else
                {
                    boardState[i][column] = player2color;
                    moveYet = true;
                }

            }

        }

        movesLeft--;
        GameOver.checkGameOver(); // I changed this in Eclipse so it works w/o the import


        // check for win and change score, or else wait for other player to move
        if (GameOver.isGameOver() && player1turn)
        {
            finalScore = movesLeft;
        }
        else if (GameOver.isGameOver() && !player1turn)
        {
            finalScore = movesLeft*-1;
        }
        else
        {
            player1turn = !player1turn;
        }


    } // end of addPiece()

    /**
     * @return The contents of the boardState variable.
     */

    public static int[][] getBoardState()
    {
        return boardState;
    } // end of getBoardState

    /**
     * @return Whether it is player1's turn.
     */

    public boolean isPlayer1Turn()
    {
        return player1turn;
    } // end of isPlayer1Turn

    /**
     * @return The value of the finalScore variable.
     */

    public static int getFinalScore()
    {
        return finalScore;
    } // end of getFinalScore

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
    } // end of toString
} // end of Board
