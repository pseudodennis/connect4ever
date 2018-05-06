package connect4ever;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import java.util.HashSet;
import java.util.Set;

/**
 * This MDP is used for training the DQN. (For playing a game itself, the Play class uses C4MDPPlay instead.)
 * The C4MDP class is essentially a wrapper for the MDP interface.
 * Contains all the methods the DQN needs to step through a series of games/epochs.
 * MDP, or Markov Decision Process, is a formal way of expressing state-action-reward relationships for reinforcement learning.
 * See chapter 3 of Sutton and Barto, "Reinforcement Learning: An Introduction," [Author's Website](http://incompleteideas.net/book/the-book-2nd.html).
 * Class design based on the deeplearning4j example [ALEMDP](https://github.com/deeplearning4j/rl4j/blob/master/rl4j-ale/src/main/java/org/deeplearning4j/rl4j/mdp/ale/ALEMDP.java)
 */
public class C4MDP implements MDP<C4MDP.GameObservation, Integer, DiscreteSpace>
{
	protected boolean v = false;												// verbose; option to control printing every move TODO move to input parameter or .setupGame() config
	protected double scaleFactor = 1; 											// the amount by which to multiply the reward
	protected C4CPU opponent;													// the other agent the DQN is playing against. Different types could be eventually set with different classes or within the same C4CPU class.
	protected C4Board c4board;													// environment that contains the gameplay states and methods
    protected int[] actions;													// array of valid actions; i.e. the column indices
    final protected DiscreteSpace discreteSpace;								// the space in which the agent is able to act within, essentially the int[] actions. Cannot be updated mid-game, unfortunately.
    final protected ObservationSpace<GameObservation> observationSpace;			// in most environments, this would be various conditions that define a particular game situation (distance from enemy, player speed, etc). For connect four, it is the board state itself.
    protected int stepCount;													// accumulator for the .steps taken; does not reset after .isDone() to allow monitoring of training progress.
    protected SolverState theBoard;												// for the Solver classes
    protected SolverMinMax computerPlayer; 										// for the Solver classes

	/**
	 * Constructor for the MDP
	 */
    public C4MDP() // the MDP referenced by the NN
    {
        c4board = new C4Board(); 												// the board environment
        opponent = new C4CPU();
        setupGame();															// specified by MDP example
        c4board.updateLegalActions();
        actions = new int[c4board.getLegalActions().length];
        actions = c4board.getLegalActions();
        discreteSpace = new DiscreteSpace(actions.length);
        int[] shape = {42}; 													// 6x7 array with 1D of depth
        observationSpace = new ArrayObservationSpace<>(shape); 					// number of input neurons?
        this.stepCount=0;

        computerPlayer = new SolverMinMax(SolverState.O);						// for the Solver
        theBoard = new SolverState();											// for the Solver
    }

	/**
	 * Not used, but included in the MDP example
	 */
    public void setupGame()
    {
        // loads the configuration for the environment
        // in the future this may be custom board size or verbose (v) argument
    }

	/**
	 * Getter for ObservationSpace
	 * In most environments, this would be various conditions that define a
	 * particular game situation (distance from enemy, player speed, etc).
	 * For connect four, it is the board state itself.
	 * @return ObservationSpace<GameObservation>
	 */
    @Override
    public ObservationSpace<GameObservation> getObservationSpace()
    {
        return observationSpace;
    }

	/**
	 * Getter for Discrete/ActionSpace. Needed for MDP interface.
	 * The space in which the agent is able to act within,
	 * essentially the int[] actions. Cannot be updated mid-game, unfortunately.
	 * @return DiscreteSpace
	 */
    @Override
    public DiscreteSpace getActionSpace()
    {
        return discreteSpace;
    }

	/**
	 * Resets the board for a new game. Needed for MDP interface.
	 * @return GameObservation Starting game conditions.
	 */
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

	/**
	 * Needed for MDP interface. Possibly used only when interacting with an online environment such as the ALE.
	 */
    @Override
    public void close() {}

    /**
     * Where the action is! The .step() method updates the environment/board, and returns the effect of that action.
     * (Part of the training process is the agent 'figuring out' the effect its actions have on the environment.)
	 * Required for MDP interface.
	 * https://github.com/deeplearning4j/gym-java-client/blob/master/src/main/java/org/deeplearning4j/gym/StepReply.java
     * @param action The "move" the agent makes
     * @return StepReply<GameObservation> the new board state, the reward after the action, and whether or not the game is over. StepReply is the container for the data returned after each step(action).
     */
    @Override
    public StepReply<GameObservation> step(Integer action)
    {
    		double r = -0.01;		// default reward value if no one wins this turn; just a small incentive to win quickly if possible
			this.stepCount++;		// running total of steps/iterations to track training progress

		//  check if the action is legal with a HashSet(!!)
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
				r= -500;														// ...set reward to train it not to do that, but not so high that it negates all the moves that came before

				if (v)															//  ...print illegal attempt
				{
					int columnAdj = action + 1;
					System.out.println("Illegal move! DQN (X) tries to add piece to column " + columnAdj);
				}
				c4board.setGameOver(true);										// ...end the game
			}

		//  update valid actions
        	actions = new int[c4board.getLegalActions().length];
        	actions = c4board.getLegalActions();

        return new StepReply(new GameObservation(c4board), r, c4board.isGameOver(), null);
    } // end of .step()

	/**
	 * Checks if the game is over. Required for MDP interface.
	 * @return boolean True if done; False if not done.
	 */
    @Override
    public boolean isDone()
    {
        return c4board.isGameOver();
    }

	/**
	 * Clones the (C4)MDP class. Required for the MDP interface.
	 * @return C4MDP
	 */
    @Override
    public C4MDP newInstance()
    {
        return new C4MDP();
    }

	/**
	 * Essentially a list of numbers that the agent uses to define the game state.
	 * In most environments, this would be various conditions that define a
	 * particular game situation (distance from enemy, player speed, direction, etc).
	 * For connect four, it is the board array itself.
	 * Encodable interface requires the GameObservation to be expressed as a double[].
	 */
    public static class GameObservation implements Encodable
    {
        double[] flatBoard;

		/**
		 * Constructor for GameObservation
		 * @param c4board The environment being observed.
		 */
        public GameObservation(C4Board c4board)
        {
            flatBoard = c4board.encode();
        } // end of GameObservation() constructor

		/**
		 * Required to return the set of game observations as a double[].
		 * TODO experiment with multiple arrays - for empty, red, and black cells
		 * @return double[] The list of game observations.
		 */
        public double[] toArray()
        {
            return flatBoard;
        }

    } // end of GameObservation class
} // end of class C4MDP