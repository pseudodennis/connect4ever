import java.util.*;

public class CPU {

	public CPU() {


	}

	public int move(int[][] playerBoard) {

		Random rand = new Random();

		int colPick = rand.nextInt(7);

		while (playerBoard[0][colPick] != 0)
		{
			colPick = rand.nextInt(7);
		}

		return colPick;
	}

}
