package connect4ever;

import org.deeplearning4j.rl4j.policy.DQNPolicy;
import java.io.IOException;

/**
 * This is the main class to play a game, using a previously trained agent.
 * To configure the opponent type, change the "opponentType" instance variable in the C4MDPPlay class.
 */
public class C4_Run
{
    public static void main(String[] args) throws IOException
    {
        //define the mdp
        C4MDPplay mdp2 = new C4MDPplay();

        //load the previous agent from C4_Train
        DQNPolicy<C4MDPplay.GameObservation> pol2 = DQNPolicy.load("c4-dql-014-overnight.model");

        // play a game
        mdp2.reset();
        pol2.play(mdp2);
    }
}
