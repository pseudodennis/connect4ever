import java.util.Random;
import java.util.Arrays;
import java.util.*;

// Credits to https://codereview.stackexchange.com/questions/150541/connect-four-game-with-minimax-ai/150543?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

public class Algo  // this is the minmax algorithm
{
    public int value;
    int[][] Boards; // this is the board
    private ArrayList<Integer> bestMoves;
    Board prev = null;
    int depth;
    static int maxDepth = 4;  // this is the max depth im going down

    public Algo(int[][] Boards, int depth)
    {
        this.Boards = Boards;
        this.bestMoves = new ArrayList<Integer>();
        this.depth = depth;
        this.value = getValue();

        if(depth < maxDepth && this.value < 100 && this.value > -100 )
        {
            ArrayList<Integer> possibilities = new ArrayList<Integer>();
            for(int i = 0; i < 7; i++)
                if(Boards[i][0] == 0)
                    possibilities.add(i);

            for(int i = 0; i < possibilities.size(); i++)
            {
                //insertTo(Boards[possibilities.get(i)][0]);
                Algo child = new Algo(Boards, depth+1);
                //prev.setPiece(Piece.None);

                if(i == 0)
                {
                    bestMoves.add(possibilities.get(i));
                    value = child.value;
                }
                else if(depth % 2 == 0)
                {
                    if(value < child.value)
                    {
                        bestMoves.clear();
                        bestMoves.add(possibilities.get(i));
                        this.value = child.value;
                    }
                    else if(value == child.value)
                        bestMoves.add(possibilities.get(i));
                }
                else if(depth % 2 == 1)
                {
                    if(value > child.value)
                    {
                        bestMoves.clear();
                        bestMoves.add(possibilities.get(i));
                        this.value = child.value;
                    }
                    else if(value == child.value)
                        bestMoves.add(possibilities.get(i));
                }
            }
        }
        else
        {
            this.value = getValue();
        }
    }


    //void insertTo(int[][] Board)  // insert into board
    //{
        //if(Board.piece != 0)
            //return;

        //int i = Board.i;
        //int j = Board.j;

        //while(j < Boards[0].length-1 && Boards[i][j+1].piece == Piece.None)
            //j++;

        //if(depth % 2 == 0)
            //Boards[i][j] = 1;
        //else
            //Boards[i][j] = 2;
        //prev = Boards[i][j];
    //}

    public int getX()  // get the player move 
    {
        int random = (int)(Math.random() * 100) % bestMoves.size();
        return bestMoves.get(random);
    }

    public int getValue() // get the value of each move
    {
        int value = 0;
        for(int j = 0; j < 6; j++)
        {
            for(int i = 0; i < 7; i++)
            {
                if(Boards[i][j] != 0)
                {
                	// Based on red move
                    if(Boards[i][j] == 1)
                    {
                        value += possibleConnections(i, j) * (maxDepth - this.depth);
                    }
                    else
                    {
                        value -= possibleConnections(i, j) * (maxDepth - this.depth);
                    }
                }
            }
        }
        return value;
    }

    public int possibleConnections(int i, int j)
    {
        int value = 0;
        value += lineOfFour(i, j, -1, -1);
        value += lineOfFour(i, j, -1, 0);
        value += lineOfFour(i, j, -1, 1);
        value += lineOfFour(i, j, 0, -1);
        value += lineOfFour(i, j, 0, 1);
        value += lineOfFour(i, j, 1, -1);
        value += lineOfFour(i, j, 1, 0);
        value += lineOfFour(i, j, 1, 1);

        return value;
    }

    public int lineOfFour(int x, int y, int i, int j)
    {
        int value = 1;
        int color = Boards[x][y];

        for(int k = 1; k < 4; k++)
        {
            if(x+i*k < 0 || y+j*k < 0 || x+i*k >= Boards.length || y+j*k >= Boards[0].length)
                return 0;
            if(Boards[x+i*k][y+j*k] == color)
                value++;
            else if (Boards[x+i*k][y+j*k] != 0)
                return 0;
            else
            {
                for(int l = y+j*k; l >= 0; l--)
                    if(Boards[x+i*k][l] == 0)
                        value--;
            }
        }

        if(value == 4) return 100;
        if(value < 0) return 0;
        return value;
    }
    
    
    public int move(int[][] playerBoard) {

		Random rand = new Random();
		int colPick = rand.nextInt(7);

		do
		{
			colPick = rand.nextInt(7);

		} while (playerBoard[0][colPick] != 0);


		System.out.println(colPick);
		
		//int thecolPick = (Boards[possibilities.get(i)][0]);
		
		return colPick;
	}
    
}
