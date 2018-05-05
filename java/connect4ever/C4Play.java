package connect4ever;

import org.deeplearning4j.rl4j.mdp.gym.GymEnv;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import java.io.IOException;
import java.util.logging.Logger;

public class C4Play
{
    public static void main(String[] args) throws IOException
    {
        //showcase serialization by using the trained agent on a new similar mdp (but render it this time)

        //define the mdp from gym (name, render)
        C4MDPplay mdp2 = new C4MDPplay();

        //load the previous agent
        DQNPolicy<C4MDPplay.GameObservation> pol2 = DQNPolicy.load("c4-dql-010.model");

        //evaluate the agent
        double rewards = 0;
        mdp2.reset();
        pol2.play(mdp2);
        //rewards += reward;
     }

}
