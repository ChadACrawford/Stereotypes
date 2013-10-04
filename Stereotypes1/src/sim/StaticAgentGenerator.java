
package sim;

import java.util.Arrays;
import java.util.Random;

public abstract class StaticAgentGenerator {
	public final int tagSize;
	public static final Random rand = new Random();
	public StaticAgentGenerator(int tagSize) { this.tagSize = tagSize; }

	public abstract StaticAgent[] generate(int amount);

	public static int[] randStr(int size, int rng) {
		int[] ret = new int[size];
		for(int i = 0; i < size; i++) ret[i] = rand.nextInt(rng);
		return ret;
	}

	public static class SAG1 extends StaticAgentGenerator {
		public SAG1(int tagSize) {
			super(tagSize);
		}

		// Random tag, random match.
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				Match m = new MatchString(randStr(tagSize,3));
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}
	
	public static class SAG2 extends StaticAgentGenerator {
		final double PROB_WILD = 0.2;
		public SAG2(int tagSize) {
			super(tagSize);
		}

		// Random tag, general match.
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			int[] baseMtch = randStr(tagSize,2);
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				int[] nMtch = Arrays.copyOf(baseMtch,baseMtch.length);
				for(int j = 0; j < nMtch.length; j++) if(rand.nextDouble() < PROB_WILD) nMtch[j] = 2;
				Match m = new MatchString(nMtch);
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}
	
	public static class SAG3 extends StaticAgentGenerator {
		final double PROB_WILD = 0.2;
		public SAG3(int tagSize) {
			super(tagSize);
		}

		// Random tag, general match.
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			int[] baseTag = randStr(tagSize,2);
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(Arrays.copyOf(baseTag,tagSize));
				Match m = new MatchString(randStr(tagSize,3));
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}
}