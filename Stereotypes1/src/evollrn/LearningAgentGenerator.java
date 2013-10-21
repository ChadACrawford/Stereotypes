package evollrn;


import sim.Match;
import sim.MatchDT;
import sim.MatchString;
import sim.Tag;

import java.util.Random;

abstract class LearningAgentGenerator {
	public final int tagSize;
	public static final Random rand = new Random();
	public LearningAgentGenerator(int tagSize) { this.tagSize = tagSize; }

	public abstract LearningAgent[] generate(int amount);

	public static int[] randStr(int size, int rng) {
		int[] ret = new int[size];
		for(int i = 0; i < size; i++) ret[i] = rand.nextInt(rng);
		return ret;
	}



	public static class LAG1 extends LearningAgentGenerator {
		public LAG1(int tagSize) {
			super(tagSize);
		}

		// Random tag, random match.
		public LearningAgent[] generate(int amount) {
			LearningAgent[] agents = new LearningAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				//Match m = new MatchString(randStr(tagSize,3));
				Match m = new MatchDT(MatchDT.GENERATE_RANDOM, tagSize);
				agents[i] = new EvolLAgent(t,m);
			}
			return agents;
		}
	}
	
	public static class LAG2 extends LearningAgentGenerator {
		public LAG2(int tagSize) {
			super(tagSize);
		}

		// Random tag, match all 2.
		public LearningAgent[] generate(int amount) {
			LearningAgent[] agents = new LearningAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				int[] mStr = new int[tagSize];
				for(int j = 0; j < tagSize; j++) mStr[j] = 2;
				Match m = new MatchString(mStr);
				agents[i] = new EvolLAgent(t,m);
			}
			return agents;
		}
	}
}