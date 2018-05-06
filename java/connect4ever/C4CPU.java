package connect4ever;

import java.util.Random;

/**
 * Class for a random "CPU" opponent.
 * Will eventually contain all the methods for each type of opponent
 */
public class C4CPU
{
	/**
	 * Constructor for C4CPU. No instance variables to set.
	 */
    public C4CPU() {}

	/**
	 * Makes a random move
	 * @param c4Board the c4board object from which to extract the current game state
	 * @return int The column in which the CPU moves
	 */
    public int move(C4Board c4Board) {

    	// create array from list of legal actions
        int[] actionList = c4Board.getLegalActions();

        // pick one of the legal actions at random
        Random rand = new Random();
        int pick = rand.nextInt(actionList.length);
        int colPick = actionList[pick];

        // System.out.println(colPick);
        return colPick;
    }

}
