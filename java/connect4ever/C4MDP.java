package connect4ever;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.space.ObservationSpace;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class C4MDP implements MDP<C4MDP.GameObservation, Integer, DiscreteSpace>
{
    protected       C4Board c4board;
    protected       int[] actions;
    final protected DiscreteSpace discreteSpace;
    final protected ObservationSpace<GameObservation> observationSpace;
    private         int[][] matrixBoard;
    protected       double scaleFactor = 1; // the amount by which to multiply the reward
    C4CPU opponent;
    int stepCount;

    // for the Solver
    int columnPosition;
    SolverState theBoard;
    SolverMinMax computerPlayer;

    // constructor for the MDP
    public C4MDP() // the MDP referenced by the NN
    {
        c4board = new C4Board(); // the board environment
        opponent = new C4CPU();
        setupGame();
        c4board.updateLegalActions();
        actions = new int[c4board.getLegalActions().length];
        actions = c4board.getLegalActions();
        discreteSpace = new DiscreteSpace(actions.length);
        int[] shape = {42}; //6x7 array with 1D of depth
        observationSpace = new ArrayObservationSpace<>(shape); // number of input neurons
        this.stepCount=0;

        computerPlayer = new SolverMinMax(SolverState.O);
        theBoard = new SolverState();
    }

    public void setupGame()
    {
        // loads the configuration for the environment
        // in the future this may be custom board size
    }

    @Override
    public ObservationSpace<GameObservation> getObservationSpace()
    {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace()
    {
        return discreteSpace;
    }

    @Override
    public GameObservation reset()
    {
       c4board.updateLegalActions();
       actions = new int[c4board.getLegalActions().length];
       c4board.reset();
       return new GameObservation(c4board);

    }

    @Override
    public void close()
    {

    }

    /**
     * The .step() method updates the environment/board
     * and returns the new board state along with the reward.
     * @param action The "move" the agent makes
     * @return
     */
    @Override
    // https://github.com/deeplearning4j/gym-java-client/blob/master/src/main/java/org/deeplearning4j/gym/StepReply.java
    public StepReply<GameObservation> step(Integer action)
    {
        double r = -0.01;
        this.stepCount++;
        // check is the action is legal, if not skip both players and set reward to a very large negative number

        int[] legalActions = c4board.getLegalActions();
        Set<Integer> legalSet = new HashSet<Integer>();
            for (int i=0; i<legalActions.length; i++)
            {
                legalSet.add(legalActions[i]);
            }

        if (!legalSet.add(action))
        {
            c4board.setDqnValid(c4board.getDqnValid()+1);
            if (!c4board.isGameOver())
            {
                c4board.addPiece(action, 1);
                actions = new int[c4board.getLegalActions().length];
                actions = c4board.getLegalActions();

                if (c4board.isGameOver() == true)
                {
                    r = c4board.getFinalScore() * scaleFactor;
                }
            }


            if (c4board.isGameOver() == false)
            {
                SolverGamePlay computerMove = computerPlayer.getNextMove(theBoard);
                theBoard.makeMove(computerMove.col, SolverState.O);
                c4board.addPiece(computerMove.col, 2);

                if (c4board.isGameOver() == true)
                {
                    r = c4board.getFinalScore() * scaleFactor * -1;
                }
            }

            else if (c4board.isGameOver() == true)
            {
                int totalMoves = c4board.getDqnInvalid()+c4board.getDqnValid();
                System.out.print(this.stepCount + "\ttotal steps\t\t" + totalMoves + "\ttotal moves\t\t\t\t\t\t\t\t\t");
                isDone();
            }
            //System.out.println(c4board.toString());
        }
        else // if move is illegal
        {
            //System.out.println("Illegal move! Avail cols: " + Arrays.toString(legalActions) + "\tYou picked col " + action);
            c4board.setDqnInvalid(c4board.getDqnInvalid()+1);
            //c4board.setnIllegal(c4board.getnIllegal()+1);
            r= -1000;
            //c4board.setGameOver(true);
        }

        actions = new int[c4board.getLegalActions().length];
        actions = c4board.getLegalActions();
        //System.out.println();

        return new StepReply(new GameObservation(c4board), r, c4board.isGameOver(), null);
    } // end of .step()

    @Override
    public boolean isDone()
    {
        return c4board.isGameOver();
    }




    @Override
    public C4MDP newInstance()
    {
        return new C4MDP();
    }

    public static class GameObservation implements Encodable
    {
        double[] flatBoard;

        public GameObservation(C4Board c4board)
        {
            flatBoard = c4board.encode();
            // System.out.println(c4board.toString());


        } // end of GameObservation() constructor

        public double[] toArray()
        {
            return flatBoard;
        }

    } // end of GameObservation class
} // end of class C4MDP
