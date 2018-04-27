package connect4ever;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.space.ObservationSpace;


public class C4MDP implements MDP<C4MDP.GameObservation, Integer, DiscreteSpace>
{
    protected       C4Board c4board;
    protected       int[] actions;
    final protected DiscreteSpace discreteSpace;
    final protected ObservationSpace<GameObservation> observationSpace;
    private int[][] matrixBoard;
    protected double scaleFactor = 1.0; // the amount by which to multiply the reward
    C4CPU opponent;

    // constructor for the MDP
    public C4MDP() // the MDP referenced by the NN
    {
        c4board = new C4Board(); // the board environment
        opponent = new C4CPU();
        setupGame();

        actions = new int[c4board.getLegalActions().length];
        actions = c4board.getLegalActions();

        discreteSpace = new DiscreteSpace(actions.length);
        int[] shape = {42}; //6x7 array with 1D of depth
        observationSpace = new ArrayObservationSpace<>(shape); // number of input neurons
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
        double r=0;
        System.out.println(action);
        if (c4board.isGameOver() == false)
        {
            c4board.addPiece(action, 1);
            if (c4board.isGameOver()==true)
            {
                r = c4board.getFinalScore() * scaleFactor;
                //System.out.println(r);
                //System.out.println(c4board.toString());
            }
        }
        if (c4board.isGameOver() == false)
        {
            c4board.addPiece(opponent.move(c4board), 2);

            if (c4board.isGameOver()==true)
            {
                r = c4board.getFinalScore() * scaleFactor * -1;
                //System.out.println(r);
                //System.out.println(c4board.toString());

            }
        }
        else if (c4board.isGameOver() == true)
        {
            isDone();
        }

        System.out.println(c4board.toString());
        return new StepReply(new GameObservation(c4board), r, c4board.isGameOver(), null);
    }

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
            System.out.println(c4board.toString());


        } // end of GameObservation() constructor

        public double[] toArray()
        {
            return flatBoard;
        }

    } // end of GameObservation class
} // end of class C4MDP
