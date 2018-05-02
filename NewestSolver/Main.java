import java.util.Scanner;
/**
 * @author Raúl González <raul.gonzalezcz@udlap.mx> ID: 151211
 * @version 1.0
 * @since 05/05/17
 * Using MinMax Algorithm, I developed the game Connect4 applying concepts of artificial intelligence
 * In this class we establish the players and the interaction between user and computer.
 */

public class Main {
    
    static int columnPosition;
    static State theBoard;
    static MinMax computerPlayer;
    ////////////////////
    
    public static void main(String[] args) {	
        //We define the AI computer player "O" and the board
	computerPlayer = new MinMax(State.O);
	theBoard = new State();
	System.out.println("Connect 4 in Java!\n");
	theBoard.printBoard();
        //While the game has not finished
	while(!theBoard.checkGameOver()) {
            System.out.println();
            switch (theBoard.lastLetterPlayed) {
            //If O played last, then X plays now (blue color)
                case State.O:
                    System.out.print("User moves.");
                    try {
                        do {
                            System.out.print("\nSelect a column to drop your piece (1-7): ");
                            Scanner in = new Scanner(System.in);
                            columnPosition = in.nextInt();
                        } while (theBoard.checkFullColumn(columnPosition-1));
                    } catch (Exception e){
                        System.out.println("\nValid numbers are 1,2,3,4,5,6 or 7. Try again\n");
                        break;
                    }
                    //Movement of the user
                    theBoard.makeMove(columnPosition-1, State.X);
                    System.out.println();
                    break;
            //If X played last, then O plays now (red color)
                case State.X:
                    GamePlay computerMove = computerPlayer.getNextMove(theBoard);
                    theBoard.makeMove(computerMove.col, State.O);
                    System.out.println("Computer "+(computerMove.col+1)+".");
                    System.out.println();
                    break;
                default:
                    break;
            }
            theBoard.printBoard();
        }
        //The game has finished because...
        System.out.println();
        if (theBoard.winner == State.X) {
            System.out.println("User wins!");
            System.out.println(theBoard.winningMethod);
        } else if (theBoard.winner == State.O) {
            System.out.println("Computer wins!");
            System.out.println(theBoard.winningMethod);
        } else {
            System.out.println("It's a draw!");
        }
        System.out.println("Game over.");
    }
}//end class Main
