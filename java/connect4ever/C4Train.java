
package connect4ever;

import java.io.IOException;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.rl4j.learning.HistoryProcessor;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteConv;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdConv;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.learning.config.Adam;


public class C4Train
{
    // http://www.teach.cs.toronto.edu/~csc2542h/fall/material/csc2542f16_dqn.pdf
    public static QLearning.QLConfiguration CARTPOLE_QL =
        new QLearning.QLConfiguration(
            123,    //Random seed
            100,    //Max step By epoch
            100000, //Max step
            1000000, //Max size of experience replay
            32,     //size of batches
            100,    //target update (hard)
            10,     //num step noop warmup
            1,   //reward scaling
            0.9,   //gamma
            1.0,    //td-error clipping
            0.1f,   //min epsilon
            50000,   //num step for eps greedy anneal
            true    //double DQN
        );

    ScoreIterationListener scoreIterationListener = new ScoreIterationListener(10000000);

    public static DQNFactoryStdDense.Configuration CARTPOLE_NET =
        DQNFactoryStdDense.Configuration.builder()
            .l2(0.001).updater(new Adam(0.0025)).numHiddenNodes(42).numLayer(3).build();


    public static void main(String[] args) throws IOException
    {
        //record the training data in rl4j-data in a new folder
        DataManager manager = new DataManager(true);

        //setup the MDP
        C4MDP mdp = new C4MDP();

        //setup the training
        QLearningDiscreteDense<C4MDP.GameObservation> dql = new QLearningDiscreteDense(mdp, CARTPOLE_NET, CARTPOLE_QL, manager);

        //start the training
            long startTime = System.currentTimeMillis();

        dql.train();

            // report the training time
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            long minutes = (totalTime / 1000)  / 60;
            long seconds = (totalTime / 1000) % 60;
            System.out.println();
            System.out.println("***");
            System.out.printf("Training duration:\t%d:%02d", minutes, seconds);


        //get the final policy
        DQNPolicy<C4MDP.GameObservation> pol = dql.getPolicy();

        //save the model at the end
        pol.save("c4-dql-010.model");

        //close the ALE env
        mdp.close();
    }
}
