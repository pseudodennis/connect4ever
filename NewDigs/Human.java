
public class Human {
	
	public int move(int[][] playerBoard) {
		int colPick;
		
		do {
			colPick = hardware_connect.getNum();
		} while(colPick == 0);
		
		return (colPick - 1);
	}

}
