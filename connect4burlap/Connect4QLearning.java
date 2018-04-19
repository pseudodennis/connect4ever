
public class Connect4QLearning {
	public static void main(String[] args) {
		Connect4World connect4World = new Connect4World();
	    SADomain domain = connect4World.generateDomain();
	    HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
	    LearningAgent agent = new QLearning(domain, 0.90, hashingFactory, 0.0, 1.0);
	    Connect4Env env = new Connect4Env();
	}

}
