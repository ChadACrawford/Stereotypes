
package sim;

import java.util.Random;

abstract class StaticAgentGenerator {
    public final int tagSize;
    public static final Random rand = new Random();
    public StaticAgentGenerator(int tagSize) { this.tagSize = tagSize; }

    public abstract StaticAgent[] generate(int amount);

    public static int[] randStr(int size, int rng) {
	int[] ret = new int[size];
	for(int i = 0; i < size; i++) ret[i] = rand.nextInt(rng);
	return ret;
    }

    public class SAG1 {
	// Random tag, random match.
	public StaticAgent[] generate(int amount) {
	    StaticAgent[] agents = new StaticAgent[amount];
	    for (int i = 0; i < amount; i++) {
		Tag t = new Tag(randStr(tagSize),2);
		Match m = new Match(randStr(tagSize),3);
		agents[i] = new StaticAgent(t,m);
	    }
	    return agents;
	}
    }
}