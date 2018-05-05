package connect4ever;

import java.util.Random;

public class C4CPU
{
    public C4CPU() {
        }

    public int move(C4Board c4Board) {

        int[] actionList = c4Board.getLegalActions();


        Random rand = new Random();
        int pick = rand.nextInt(actionList.length);
        int colPick = actionList[pick];

        // System.out.println(colPick);
        return colPick;
    }

}
