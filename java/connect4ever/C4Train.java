package connect4ever;

import java.io.IOException;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.learning.config.Adam;


/**
 * The main method used to run the training on the DQN (Deep Q Network)
 * Use this to setup the neural network parameters and train the DQN against the Solver algo.
 * Based on [Cartpole.java](https://github.com/deeplearning4j/oreilly-book-dl4j-examples/blob/master/rl4j-examples/src/main/java/org/deeplearning4j/examples/rl4j/Cartpole.java)
 */
public class C4Train
{
    // Configure the neural network
	// For in-depth explanation, see (esp. Slide 32 for starting-point values) [Human-level control through deep reinforcement learning](http://www.teach.cs.toronto.edu/~csc2542h/fall/material/csc2542f16_dqn.pdf)
/*
     Performance notes: (x64 Intel i5-6300U CPU @ 2.40GHz)
			Time to take 100,000 steps:
			- Solver level 0:		~11 mins?		(winning after 10,000 steps)
			- Solver level 1:		10 mins			(winning after 50,000 steps)
			- Solver level 2:		~10 mins		250,000?
			- Solver level 3:		10 mins			1m
			1 million steps = 100 mins
			3 million steps = 1000 mins = 5 hours
*/

	public static QLearning.QLConfiguration QL =
        new QLearning.QLConfiguration(
            123,    //Random seed
            50,    //Max step By epoch
            100000, //Max step --
            100000, //Max size of experience replay
            32,     //size of batches
            500,    //target update (hard)
            50,     //num step noop warmup
            1,   //reward scaling
            0.99,   //gamma
            1.0,    //td-error clipping
            0.1f,   //min epsilon
            25000,   //num step for eps greedy anneal
            true    //double DQN
        );
		// Performance notes:



    // set the parameters for the DQN
    public static DQNFactoryStdDense.Configuration NET =
        DQNFactoryStdDense.Configuration.builder()
            .l2(0.001).updater(new Adam(0.005)).numHiddenNodes(42).numLayer(3).build();


    public static void main(String[] args) throws IOException
    {
        //record the training data in rl4j-data in a new folder
        DataManager manager = new DataManager(true);

        //setup the MDP
        C4MDP mdp = new C4MDP();

        //setup the training
        QLearningDiscreteDense<C4MDP.GameObservation> dql = new QLearningDiscreteDense(mdp, NET, QL, manager);

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
        pol.save("c4-dql-015.model");
        // TODO write class that generates filename with current datetime and allows C4_Run to automatically load the most recent one

        //close the env
        mdp.close();
    }
}
