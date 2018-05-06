import java.util.*;

/**
 * The CPU class is a computer player that picks random columns to move in.
 */

public class CPU {

	public CPU() {


	}
	
	/**
	 * The move method picks a random and valid column on the board.
	 * @param playerBoard The current state of the board
	 * @return a random column
	 */

	public int move(int[][] playerBoard) {

		Random rand = new Random();
		int colPick = rand.nextInt(7);

		do
		{
			colPick = rand.nextInt(7);

		} while (playerBoard[0][colPick] != 0);


		hardware_connect.sendNum(colPick + 1);
		System.out.println(colPick);
		return colPick;
	} // end of move

} // end of CPU
