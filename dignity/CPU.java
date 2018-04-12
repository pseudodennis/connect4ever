import java.util.*;

public class CPU {
	
	public CPU(Board obj) {
		
		Board obj1 = new Board(5, 6);
		
	}
	
	public int move(int[][] playerBoard) {
		Board obj1 = new Board(5, 6);
		
		obj1.getBoardState();
		
		Random rand = new Random();
		
		int colPick = rand.nextInt(6);
		
		return colPick;
	}

}
