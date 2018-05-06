
/**
 * The Human class gets a column number from the hardware
 * to pass it to the main class. 
 */

public class Human 
{	
	/**
	 * @param playerBoard A two dimentional array that holds the
	 * board. We don't use this.
	 * @return The column that the player went in.
	 */
	
	public int move(int[][] playerBoard) 
	{
		int colPick;
		
		do {
			/**
			 * Get the location of the piece the player put in
			 * from the arduino
			 */
			colPick = hardware_connect.getNum();
		} while(colPick == 0);
		
		return (colPick - 1);
	} // end move method

} // end human class
