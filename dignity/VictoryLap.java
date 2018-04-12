import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VictoryLap
{

	public static void main(String[] args)
	{

		int nRows = 6;  // the number of rows in the board
		int nCols = 7;  // the number of cols in the board
		int iRow = 0;   // row index number for incrementing
		int iCol = 0;   // col index number for incrementing
		int cl = 4;     // length of chain for indexing
		boolean win = false;


// build an array

		int[][] board = new int[nRows][nCols];


// fill it with some numbers

		board[1][1]=1;
		board[2][2]=1;
		board[3][3]=1;
		board[4][5]=1;


// print it to check

		for (iRow=0; iRow<nRows; iRow++)
		{
			for (iCol = 0; iCol < nCols; iCol++)
			{
				System.out.print("[" + board[iRow][iCol] + "]");
			}
			System.out.println();
		}



// check for horizontal wins

		// move through each cell, unless the cells are within 4 spaces of nCols

		// move down the rows
		for (iRow = 0; iRow < nRows && !win; iRow++)
		{
			// move from left to right across row
			for (iCol = 0; iCol <= nCols - cl && !win; iCol++)
			{
				// grab the starting cell value and put in hashset
				int cellVal = board[iRow][iCol];
				if (cellVal != 0)
				{
					Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
					win = true;
					// iterate through the next three cells, if add==true, win=false
					for (int i = 1; i <= 3 ; i++)
					{
						if (hs.add(board[iRow][iCol + i])==true)
						win = false;
					}
				}
			} // end of moving through columns
		}


// check for vertical wins

		// move through each cell, unless the cells are within 4 spaces of nCols

		// move across all the cols
		for (iCol = 0; iCol < nCols && !win; iCol++)
		{
			// move from top to bottom through each row in col
			for (iRow = 0; iRow <= nRows - cl && !win; iRow++)
			{
				// grab the starting cell value and put in hashset
				int cellVal = board[iRow][iCol];
				if (cellVal != 0)
				{
					Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
					win = true;

					// iterate through the next three cells, if add==true, win=false
					for (int i = 1; i <= 3; i++)
					{
						if (hs.add(board[iRow + i][iCol]))
						win = false;
					}
				}
			} // end of moving through columns
		}


// check for downhill diag wins

		// move through each cell, unless the cells are within 4 spaces of nCols

		// move across the cols within range
		for (iCol = 0; iCol <= nCols-cl && !win; iCol++)
		{
			// move from top to bottom through row in col in range
			for (iRow = 0; iRow <= nRows - cl && !win; iRow++)
			{
				// grab the starting cell value and put in hashset
				int cellVal = board[iRow][iCol];
				if (cellVal != 0)
				{
					Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
					win = true;

					// iterate through the next three cells, if add==true, win=false
					for (int i = 1; i <= 3; i++)
					{
						if (hs.add(board[iRow + i][iCol + i]))
						win = false;
					}
				}
			} // end of moving through columns
		}

// check for uphill diag wins

		// move through each cell, unless the cells are within 4 spaces of nCols

		// move down the rows, starting within range
		for (iRow = cl-1; iRow < nRows && !win; iRow++)
		{
			// move from top to bottom through row in col in range
			for (iCol = 0; iCol <= nCols - cl && !win; iCol++)
			{
				// grab the starting cell value and put in hashset
				int cellVal = board[iRow][iCol];
				if (cellVal != 0)
				{
					Set<Integer> hs = new HashSet<>(Arrays.asList(cellVal));
					win = true;

					// iterate through the next three cells, if add==true, win=false
					for (int i = 1; i < cl; i++)
					{
						if (hs.add(board[iRow - i][iCol + i]))
						win = false;
					}
				}
			} // end of moving through columns
		}

		System.out.print(win);

	} // end main()
} // end of VictoryLap
