import java.util.ArrayList;

public class Teach
{
	public static void main(String[] args)
	{
		boolean cpuVictory = false;
		boolean playerVictory = false;
		boolean draw = false;
		int numberOfGames = 100;
		ArrayList<Integer> gameScores = new ArrayList<Integer>();

		for (int i=0; i<numberOfGames; i++)
		{
			int score = 42;
			// construct starting environment (ie. an empty board)

			while (!cpuVictory && !playerVictory && !draw)
			{
				// CPU move (*** NN goes here ***)
				// update environment with CPU move
				// update score (score--)
				// test for victory condition

				// player move (via algorithm)
				// update environment with user move
				// update score (score--)
				// test for victory condition
			}

			// calculate final games score based on winner
				if (cpuVictory)
					score = score; // redundant, including for symmetry
				else if (playerVictory)
					score = score*-1;
				else if (draw)
					score = 0; // which it should be 0 anyway

			// add score to array
				gameScores.add(score);
			// update NN policy based on score trend (linear regression, gradient decent, etc)
		}


	} // end of main()
} // end of Teach
