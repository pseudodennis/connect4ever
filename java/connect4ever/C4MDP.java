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
    boolean v = false;		// verbose option to control printing out every move

    // for the Solver classes
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
		//  reset the Solver board
       		computerPlayer = new SolverMinMax(SolverState.O);
       		theBoard = new SolverState();

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
    		double r = -0.1;		// default reward value if no one wins this turn; just a small incentive to win quickly if possible
			this.stepCount++;		// running total of steps/iterations to track training progress

		//  check is the action is legal with a HashSet(!)
        	int[] legalActions = c4board.getLegalActions();
        	Set<Integer> legalSet = new HashSet<Integer>();
			for (int i=0; i<legalActions.length; i++)
			{
				legalSet.add(legalActions[i]);
			}
			// System.out.println(legalSet.toString()); 						// print legal moves for debugging

			if (!legalSet.add(action))											// if the move is legal... (proceed through a full 2-player turn, otherwise, .step again)
			{
				/*
					DQN moves first
				*/
				c4board.setDqnValid(c4board.getDqnValid()+1);					// increment the number of valid moves the DQN has made this game
				if (!c4board.isGameOver())										// if the game is not over...
				{
					c4board.addPiece(action, 1);						// ...tell c4board to have player 1 move there
					theBoard.makeMove(action, SolverState.X);					// ...and tell solverBoard about it
					if (c4board.isGameOver())									// ...then if the game is over...
					{
						r = c4board.getFinalScore() * scaleFactor;				// ...assign final score
					}
					if (v)														//  print the resulting board state and adjust column numberings for humans
					{
						int columnAdj = action + 1;
						System.out.println("DQN (X) adds piece to column: " + columnAdj);
						System.out.println(c4board.toString());
					}
				}

				/*
					Solver moves next
				*/
				if (!c4board.isGameOver())										// if the game is still not over
				{
					//  ...get the Solver move, then update the Solver board, then make the move in the c4board
						SolverGamePlay computerMove = computerPlayer.getNextMove(theBoard);
						theBoard.makeMove(computerMove.col, SolverState.O);
						c4board.addPiece(computerMove.col, 2);

					if (v)														// ...print the resulting board state and adjust column numberings for humans
					{
						System.out.println("Solver (O) added piece to column: " + (computerMove.col + 1));
						System.out.println(c4board.toString());
					}

					if (c4board.isGameOver() == true)							// ...then if the game is over...
					{
						r = c4board.getFinalScore() * scaleFactor * -1;			// ...assign final score
					}
				}

				if (c4board.isGameOver())										// if game is over...
				{
					if (v)														//  ... print moves
					{
						int totalMoves = c4board.getDqnInvalid() + c4board.getDqnValid();
						System.out.print(this.stepCount + "\ttotal steps\t\t" + totalMoves + "\ttotal moves\t\t\t\t\t\t\t\t\t");
					}
					isDone();
				}
			}
			else 																// if move is illegal (column full)...
			{
				c4board.setDqnInvalid(c4board.getDqnInvalid()+1);				// ...increment DQN invalid move count
				r= -500;														// ...set reward to train it not to do that

				if (v)															//  ...print illegal attempt
				{
					int columnAdj = action + 1;
					System.out.println("Illegal move! DQN (X) tries to add piece to column " + columnAdj);
				}
				c4board.setGameOver(true);										// ...end the game
			}

        actions = new int[c4board.getLegalActions().length];
        actions = c4board.getLegalActions();

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
