package connect4ever;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This MDP is used for playing the DQN against a given opponent. To set opponent type, see the instance variable below.
 * The C4MDPplay class contains all the methods the DQN needs to step through a series of games/epochs.
 * MDP, or Markov Decision Process, is a formal way of expressing state-action-reward relationships for reinforcement learning.
 * See chapter 3 of Sutton and Barto, "Reinforcement Learning: An Introduction," [Author's Website](http://incompleteideas.net/book/the-book-2nd.html).
 * Class design based on the deeplearning4j example [ALEMDP](https://github.com/deeplearning4j/rl4j/blob/master/rl4j-ale/src/main/java/org/deeplearning4j/rl4j/mdp/ale/ALEMDP.java)
 */

public class C4MDPplay implements MDP<C4MDPplay.GameObservation, Integer, DiscreteSpace>
{
    protected C4Board c4board;													// environment that contains the gameplay states and methods
    protected int[] actions;													// array of valid actions; i.e. the column indices
    final protected DiscreteSpace discreteSpace;								// the space in which the agent is able to act within, essentially the int[] actions. Cannot be updated mid-game, unfortunately.
    final protected ObservationSpace<GameObservation> observationSpace;			// in most environments, this would be various conditions that define a particular game situation (distance from enemy, player speed, etc). For connect four, it is the board state itself.
    protected C4CPU opponent;													// the other agent the DQN is playing against. Different types could be eventually set with different classes or within the same C4CPU class.
    protected int stepCount;													// accumulator for the .steps taken; does not reset after .isDone() to allow monitoring of training progress.

	protected SolverState theBoard;												//  for the Solver classes
	protected SolverMinMax computerPlayer;										//  for the Solver classes

	int opponentType = 2;		//	set the opponent type for the DQN to play against:
	//	0:	Random opponent (default value)
	//	1:	Human opponent via keyboard
	//	2:	Human opponent via Arduino
	//	3:	Solver opponent


    // constructor for the MDP
    public C4MDPplay() // the MDP referenced by the DQN
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
    public void close() {}

    /**
     * This is where the action is...
     * The .step() method updates the environment/board
     * and returns the new board state along with the reward.
     * https://github.com/deeplearning4j/gym-java-client/blob/master/src/main/java/org/deeplearning4j/gym/StepReply.java
     * @param action The "move" the DQN agent makes, used to update the environment state and calculate reward
     * @return
     */
    @Override
    public StepReply<GameObservation> step(Integer action)
    {
        double r = 0;       // reward value, not really used in live game
        this.stepCount++;   // tracks how many steps for training progress

/*
		DQN moves first
*/

		//  check if the action is legal with a HashSet(!)
	        int[] legalActions = c4board.getLegalActions();
	        Set<Integer> legalSet = new HashSet<Integer>();
			for (int i=0; i<legalActions.length; i++)
			{
				legalSet.add(legalActions[i]);
			}
	        // System.out.println(legalSet.toString()); // print legal moves for debugging

        if (!legalSet.add(action)) // if the move is legal...
        {
            c4board.addPiece(action, 1);			// ...tell c4board to have player 1 move there
			if (opponentType == 3)
				theBoard.makeMove(action, SolverState.X);	// ...and tell solverBoard about it

			//  print the resulting board state and adjust column numberings for humans
				int columnAdj = action + 1;
				System.out.println("DQN (X) adds piece to column: " + columnAdj);
				System.out.println(c4board.toString());
        }
        else // otherwise, if the move is not legal...
        {
        	// TODO instead of a random move, use the Solver instead
            int altMove = opponent.move(c4board);			// ...play a random move
            c4board.addPiece(altMove, 1);

            if (opponentType == 3)
				theBoard.makeMove(altMove, SolverState.X);	// ...and tell solverBoard about it

            //  print the resulting board state and adjust column numberings for humans
				int columnAdj = altMove + 1;
				hardware_connect.sendNum(columnAdj);
				System.out.println("DQN (X) adds piece to column: " + columnAdj);
				System.out.println(c4board.toString());
        }

	//  TODO print the board state outside the if/else structure
	//  update c4board legal actions
		actions = new int[c4board.getLegalActions().length];
		actions = c4board.getLegalActions();

/*
		Opponent moves next
*/

        if (c4board.isGameOver() == false) // if the game is not over...
        {
            // TODO validate user input
			// TODO add decision structure to use a variable to switch between Solver, Human-Keyboard, Human-Arduino, and Random input cases
			// TODO move the opponent-type cases to their own classes

			int userInput;
			Scanner keyboard = new Scanner(System.in);

			// do different things for different opponent types, set in instance variable above
			switch (opponentType)
			{
				case 1:		// Human-Keyboard opponent
					System.out.print("What is your move? " + Arrays.toString(actions) + "\t");
					userInput = keyboard.nextInt()-1;
					c4board.addPiece(userInput, 2);
					System.out.println("You (O) added piece to column: " + (userInput+1));
					break;
				case 2:		// Human-Arduino opponent
					// TODO add arduino interface here! Using keyboard for now...
					
					boolean err = false;
					err = hardware_connect.Connect();
					if(err == true)
				    {
				        System.exit(0);
				    }
					hardware_connect.starter(true);
					
					System.out.print("What is your move? " + Arrays.toString(actions) + "\t");
					//userInput = keyboard.nextInt()-1;
					userInput = hardware_connect.getNum();
					c4board.addPiece(userInput, 2);
					System.out.println("You (O) added piece to column: " + (userInput+1));
					break;
				case 3:		// Solver opponent
					SolverGamePlay computerMove = computerPlayer.getNextMove(theBoard);
					theBoard.makeMove(computerMove.col, SolverState.O);
					c4board.addPiece(computerMove.col, 2); // make move based on Solver return (just above this switch block)
					System.out.println("Solver (O) added piece to column: " + (computerMove.col+1));
					break;
				default:	// Random opponent
					int randMove = opponent.move(c4board);			// ...play a random move
					c4board.addPiece(randMove, 2);
					System.out.println("Random (O) added piece to column: " + (randMove+1));
					break;
			}	// end switch (opponentType)
        }

		//  print board and update c4board legal actions
			System.out.println(c4board.toString());
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
    public C4MDPplay newInstance()
    {
        return new C4MDPplay();
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
