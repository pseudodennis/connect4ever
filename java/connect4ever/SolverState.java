package connect4ever;

import java.util.LinkedList;
/**
 * @author Raúl González <raul.gonzalezcz@udlap.mx> ID: 151211
 * @version 1.0
 * @since 05/05/17
 * Here is the definition of the board for Connect4
 */

public class SolverState {

    static final int X = 1;     //User (used in Legacy.Main and switch case)
    static final int O = -1;    //Computer (used in Legacy.Main and switch case)
    int EMPTY = 0;              //Blank space
    //We need to know the player that made the last move
    SolverGamePlay lastMove;
    int lastLetterPlayed;
    int winner;
    int [][] gameBoard;
    String winningMethod;       //Winner by [row, column, diagonal]
    //------------

    //Constructor of a state (board)
    public SolverState() {
        lastMove = new SolverGamePlay();
        lastLetterPlayed = O; //The user starts first
        winner = 0;
        gameBoard = new int[6][7];
        for(int i=0; i<6; i++) {
            for(int j=0; j<7; j++) {
                gameBoard[i][j] = EMPTY;
            }
        }
    }//end Constructor

    public void setWinner(int winner) {
        this.winner = winner;
    }//end setWinner

    public void setWinnerMethod(String winningMethod) {
        this.winningMethod = winningMethod;
    }//end setWinnerMethod

    //Make a movement based on a column and a player
    public void makeMove(int col, int letter) {
        lastMove = lastMove.moveDone(getRowPosition(col), col);
        gameBoard[getRowPosition(col)][col] = letter;
        lastLetterPlayed = letter;
    }//end makeMove

    //Checks whether a move is valid; whether a square is empty. Used only when we need to expand a movement
    public boolean isValidMove(int col) {
        int row = getRowPosition(col);
        if ((row == -1) || (col == -1) || (row > 5) || (col > 6)) {
            return false;
        }
        if(gameBoard[row][col] != EMPTY) {
            return false;
        }
        return true;
    }//end isValidMove

    //Is used when we need to make a movement (Is possible to move the piece?)
    public boolean canMove(int row, int col) {
        //We evaluate mainly the limits of the board
        if ((row <= -1) || (col <= -1) || (row > 5) || (col > 6)) {
            return false;
        }
        return true;
    }//end CanMove

    //Is a column full?
    public boolean checkFullColumn(int col) {
        if (gameBoard[0][col] == EMPTY)
            return false;
        else{
            System.out.println("The column "+(col+1)+" is full. Select another column.");
            return true;
        }
    }//end checkFullColumn

    //We search for a blank space in the board to put the piece ('X' or 'O')
    public int getRowPosition(int col) {
        int rowPosition = -1;
        for (int row=0; row<6; row++) {
            if (gameBoard[row][col] == EMPTY) {
                rowPosition = row;
            }
        }
        return rowPosition;
    }//end getRowPosition

    //This method help us to expand the board (it´s a board state given a move)
    public SolverState boardWithExpansion(SolverState board) {
        SolverState expansion = new SolverState();
        expansion.lastMove = board.lastMove;
        expansion.lastLetterPlayed = board.lastLetterPlayed;
        expansion.winner = board.winner;
        expansion.gameBoard = new int[6][7];
        for(int i=0; i<6; i++) {
            for(int j=0; j<7; j++) {
                expansion.gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
        return expansion;
    }//end boardWithExpansion

    //Generates the children of the state. The max number of the children is 7 because we have 7 columns
    public LinkedList<SolverState> getChildren(int letter) {
        LinkedList<SolverState> children = new LinkedList<SolverState>();
        for(int col=0; col<7; col++) {
            if(isValidMove(col)) {
                SolverState child = boardWithExpansion(this);
                child.makeMove(col, letter);
                children.add(child);
            }
        }
        return children;
    }//end getChildren

    public int utilityFunction() {
        //MAX plays 'O'
        // +90 if 'O' wins, -90 'X' wins,
        // +10 if three 'O' in a row, -5 three 'X' in a row,
        // +4 if two 'O' in a row, -1 two 'X' in a row
        int Xlines = 0;
        int Olines = 0;
        if (checkWinState()) {
            if(winner == X) {
                Xlines = Xlines + 90;
            } else {
                Olines = Olines + 90;
            }
        }
        Xlines  = Xlines + check3In(X)*10 + check2In(X)*4;
        Olines  = Olines + check3In(O)*5 + check2In(O);
        return Olines - Xlines;
    }//end utilityFunction

    //Is there a possible winner movement? (4In)
    public boolean checkWinState() {
        //Case if we have 4-row
        for (int i=5; i>=0; i--) {
            for (int j=0; j<4; j++) {
                if (gameBoard[i][j] == gameBoard[i][j+1] && gameBoard[i][j] == gameBoard[i][j+2] && gameBoard[i][j] == gameBoard[i][j+3] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Winner by row.");
                    return true;
                }
            }
        }

        //Case we have a 4-column
        for (int i=5; i>=3; i--) {
            for (int j=0; j<7; j++) {
                if (gameBoard[i][j] == gameBoard[i-1][j] && gameBoard[i][j] == gameBoard[i-2][j] && gameBoard[i][j] == gameBoard[i-3][j] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Winner by column.");
                    return true;
                }
            }
        }

        //Case we have an ascendent 4-diagonal
        for (int i=0; i<3; i++) {
            for (int j=0; j<4; j++) {
                if (gameBoard[i][j] == gameBoard[i+1][j+1] && gameBoard[i][j] == gameBoard[i+2][j+2] && gameBoard[i][j] == gameBoard[i+3][j+3] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Winner by diagonal.");
                    return true;
                }
            }
        }

        //Case we have an descendent 4-diagonal
        for (int i=0; i<6; i++) {
            for (int j=0; j<7; j++) {
                if (canMove(i-3,j+3)) {
                    if (gameBoard[i][j] == gameBoard[i-1][j+1] && gameBoard[i][j] == gameBoard[i-2][j+2] && gameBoard[i][j] == gameBoard[i-3][j+3]  && gameBoard[i][j] != EMPTY) {
                        setWinner(gameBoard[i][j]);
                        setWinnerMethod("Winner by diagonal.");
                        return true;
                    }
                }
            }
        }
        //There is no winner yet :(
        setWinner(0);
        return false;
    }//end checkWinState

    //Checks if there are 3 pieces of a same player
    public int check3In(int player) {
        int times = 0;
        //In row
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In column
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In diagonal ascendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 2, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In diagonal descendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }
        return times;
    }//end check3In

    //Checks if there are 2 pieces of a same player
    public int check2In(int player) {
        int times = 0;
        //In a row
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a column
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a diagonal ascendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 1, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a diagonal descendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }
        return times;
    }//end check2In

    public boolean checkGameOver() {
        //If there is a winner, we need to know it
        if (checkWinState()) {
            return true;
        }
        //Are there blank spaces in the board?
        for(int row=0; row<6; row++) {
            for(int col=0; col<7; col++) {
                if(gameBoard[row][col] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }//end checkGameOver

    //Print the board
    public void printBoard() {
        System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
        System.out.println();
        for (int i=0; i<6; i++) {
            for (int j=0; j<7; j++) {
                if (gameBoard[i][j] == 1) {
                    System.out.print("| " + "X" + " "); //Blue for user
                } else if (gameBoard[i][j] == -1) {
                    System.out.print("| " + "O" + " "); //Red for computer
                } else {
                    System.out.print("| " + "-" + " ");
                }
            }
            System.out.println("|"); //End of each row
        }
    }//end printBoard
}//end State
