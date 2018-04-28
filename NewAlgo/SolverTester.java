import java.util.Scanner;

public class SolverTester {

	public static void main(String[] args) throws InterruptedException, CloneNotSupportedException
	{
	
        System.out.print("Enter the algorithm depth: ");
    	Scanner in = new Scanner(System.in);
    	int depth = in.nextInt();
    	
    		
    	minimaxAgent mma = new minimaxAgent(depth);
    	State s=new State(6,7);
    	while(true){
    		int action = mma.getAction(s);
    		s = s.generateSuccessor('O', action);
    		s.printBoard();
    		//check if O won?
    		if(s.isGoal('O'))
    		break;
    		System.out.print("Enter your row: ");
    		// Can we plug the neural network into 'enemy_move' below???
    		// Instead of in.NextInt() maybe we can just use the neural network's output?
    		int enemy_move = in.nextInt();
    		s = s.generateSuccessor('X', enemy_move);
    		s.printBoard();
    		//check if X won? break
    		if(s.isGoal('X'))
    		break;
    		//pause
    		
    	}
        
	} // end of main

	

}
