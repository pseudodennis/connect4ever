package connect4ever;

/**
 * @author Raúl González <raul.gonzalezcz@udlap.mx> ID: 151211
 * @version 1.0
 * @since 05/05/17
 * This class allows us to make a move and get a sepecific position
 */

public class SolverGamePlay
{
    int row;
    int col;
    private int value;      //Value of utility function
    ////////////////////

    public SolverGamePlay() {
        row = -1;
        col = -1;
        value = 0;
    }//end Constructor

    //Move done
    public SolverGamePlay moveDone(int row, int col) {
        SolverGamePlay moveDone = new SolverGamePlay();
        moveDone.row = row;
        moveDone.col = col;
        moveDone.value = -1;
        return moveDone;
    }// end moveDone

    //Move for expansion (with utility function)
    public SolverGamePlay possibleMove(int row, int col, int value) {
        SolverGamePlay posisibleMove = new SolverGamePlay();
        posisibleMove.row = row;
        posisibleMove.col = col;
        posisibleMove.value = value;
        return posisibleMove;
    }//end possibleMove

    //Move used to compare in MinMax algorithm
    public SolverGamePlay moveToCompare(int value) {
        SolverGamePlay moveToCompare = new SolverGamePlay();
        moveToCompare.row = -1;
        moveToCompare.col = -1;
        moveToCompare.value = value;
        return moveToCompare;
    }//end moveCOmpare

    public int getValue() {
        return value;
    }//end getValue

    public void setRow(int aRow) {
        row = aRow;
    }//end setRow

    public void setCol(int aCol) {
        col = aCol;
    }//end setCol


    public void setValue(int aValue) {
        value = aValue;
    }//end setValue
}//end class GamePlay
