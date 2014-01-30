package evollrn;

import sim.Match;
import sim.MatchDT;
import sim.MatchString;
import sim.Tag;

import java.util.ArrayList;
import java.util.Random;

abstract class PopulationGen {
	public final int tagSize;
	public final LAGenerator gen;
	public static final Random rand = Constraints.rand;
	public PopulationGen(int tagSize, LAGenerator gen) { this.tagSize = tagSize; this.gen = gen; }

	public abstract LearningAgent[] generate(int amount);

	public static int[] randStr(int size, int rng) {
		int[] ret = new int[size];
		for(int i = 0; i < size; i++) ret[i] = rand.nextInt(rng);
		return ret;
	}
	
	public static int[] randDT(int depth,int length)
	{
		int[] mDT = new int[(int) Math.pow(2, depth)];
		ArrayList<Integer> set = new ArrayList<Integer>();
		for(int i=0;i<length;i++)
		{
			set.add(i);
		}
		DTWiden(mDT, 0, length,set);
		return mDT;
	}
	
	public static void DTWiden(int[] m, int index,int length,ArrayList<Integer> remaining)
	{
		if(index*2+2>=m.length || rand.nextBoolean())
		{
			if(rand.nextBoolean())
				m[index]=-1;
			else
				m[index]=-2;
		}
		else
		{
			int i = rand.nextInt(remaining.size());
			m[index]=remaining.get(i);
			remaining.remove(i);
			DTWiden(m, 2*index+1, length, remaining);
			DTWiden(m, 2*index+2, length, remaining);
		}
	}

	public static class LAG1 extends PopulationGen {
		
		public LAG1(int tagSize, LAGenerator gen) {
			super(tagSize, gen);
		}

		// Random tag, random match.
		public LearningAgent[] generate(int amount) {
			LearningAgent[] agents = new LearningAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				//Match m = new MatchString(randStr(tagSize,3));
				Match m = new MatchDT(MatchDT.GENERATE_RANDOM,tagSize);
                //System.out.println(m);
				agents[i] = gen.genAgent(t,m);
			}
			return agents;
		}
	}
	
	public static class LAGMatch1 extends PopulationGen {
		public LAGMatch1(int tagSize, LAGenerator gen) {
			super(tagSize, gen);
		}

		// Random tag, match all 2.
		public LearningAgent[] generate(int amount) {
			LearningAgent[] agents = new LearningAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				Match m = new MatchString(randStr(tagSize,3));
				agents[i] = gen.genAgent(t,m);
			}
			return agents;
		}
	}
	
	public static class LAG2 extends PopulationGen {
		public LAG2(int tagSize, LAGenerator gen) {
			super(tagSize, gen);
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
	
	public static class LAGNullMatch extends PopulationGen {
		public LAGNullMatch(int tagSize, LAGenerator gen) {
			super(tagSize, gen);
		}
		
		public LearningAgent[] generate(int amount) {
			LearningAgent[] agents = new LearningAgent[amount];
			for(int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				agents[i] = gen.genAgent(t,null);
			}
			return agents;
		}
	}
}