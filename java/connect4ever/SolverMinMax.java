package connect4ever;

import java.util.LinkedList;
import java.util.Random;
/**
 * @author Raúl González <raul.gonzalezcz@udlap.mx> ID: 151211
 * @version 1.0
 * @since 05/05/17
 * Here is the core of the MinMax algorithm
 */

public class SolverMinMax {
    //Variable that holds the maximum depth the MinMax algorithm will reach (level of the three)
    int maxDepth;
    //Variable that holds which letter the computer controls
    int computerLetter;
    ////////////////////

    public SolverMinMax(int thePlayerLetter) {
        maxDepth = 3; //This is important to get a better decision (more depth, more accurate decision, more time)
        computerLetter = thePlayerLetter;
    }//end Constructor

    //Initiates the MinMax algorithm
    public SolverGamePlay getNextMove(SolverState board) {
        //We want to take the lowest of its recursive generated values in order to choose the greatest
        return max(board.boardWithExpansion(board), 0);
    }//end getNextMove

    //The max and min methods are called interchangingly, one after another until a max depth is reached
    //The difference between Tic Tac Toe program is that here we can specify a depth (number of levels in the three)
    public SolverGamePlay min(SolverState board, int depth) { //MIN plays 'X' (user)
        Random r = new Random();
        // If MIN is called on a state that is terminal or after a maximum depth is reached, then a heuristic is calculated on the state and the move returned.
        if((board.checkGameOver()) || (depth == maxDepth)){
            SolverGamePlay baseMove = new SolverGamePlay();
            baseMove = baseMove.possibleMove(board.lastMove.row, board.lastMove.col, board.utilityFunction());
            return baseMove;
        }else{
            //The children-moves of the state are calculated (expansion)
            LinkedList<SolverState> children = new LinkedList<SolverState>(board.getChildren(SolverState.X));
            SolverGamePlay minMove = new SolverGamePlay();
            minMove = minMove.moveToCompare(Integer.MAX_VALUE);
            for (int i =0; i < children.size();i++) {
                SolverState child = children.get(i);
                //And for each child max is called, on a lower depth
                SolverGamePlay move = max(child, depth + 1);
                //The child-move with the lowest value is selected and returned by max
                if(move.getValue() <= minMove.getValue()) {
                    if ((move.getValue() == minMove.getValue())) {
                        //If the heuristic has the same value then we randomly choose one of the two moves
                        if (r.nextInt(2) == 0) { //If 0, we rewrite the maxMove. Else, the maxMove is the same
                            minMove.setRow(child.lastMove.row);
                            minMove.setCol(child.lastMove.col);
                            minMove.setValue(move.getValue());
                        }
                    }
                    else {
                        minMove.setRow(child.lastMove.row);
                        minMove.setCol(child.lastMove.col);
                        minMove.setValue(move.getValue());
                    }
                }
            }
            return minMove;
        }
    }//end min

    //The max and min methods are called interchangingly, one after another until a max depth or game over is reached
    public SolverGamePlay max(SolverState board, int depth) { //MAX plays 'O'
        Random r = new Random();
        //If MAX is called on a state that is terminal or after a maximum depth is reached, then a heuristic is calculated on the state and the move returned.
        if((board.checkGameOver()) || (depth == maxDepth)) {
            SolverGamePlay baseMove = new SolverGamePlay();
            baseMove = baseMove.possibleMove(board.lastMove.row, board.lastMove.col, board.utilityFunction());
            return baseMove;
        }else{
            LinkedList<SolverState> children = new LinkedList<SolverState>(board.getChildren(computerLetter));
            SolverGamePlay maxMove = new SolverGamePlay();
            maxMove = maxMove.moveToCompare(Integer.MIN_VALUE);
            for (int i =0; i < children.size();i++) {
                SolverState child = children.get(i);
                SolverGamePlay move = min(child, depth + 1);
                //Here is the difference with Min method: The greatest value is selected
                if(move.getValue() >= maxMove.getValue()) {
                    if ((move.getValue() == maxMove.getValue())) {
                        if (r.nextInt(2) == 0) {
                            maxMove.setRow(child.lastMove.row);
                            maxMove.setCol(child.lastMove.col);
                            maxMove.setValue(move.getValue());
                        }
                    }
                    else {
                        maxMove.setRow(child.lastMove.row);
                        maxMove.setCol(child.lastMove.col);
                        maxMove.setValue(move.getValue());
                    }
                }
            }
            return maxMove;
        }
    }//end max
}//end class MinMax
