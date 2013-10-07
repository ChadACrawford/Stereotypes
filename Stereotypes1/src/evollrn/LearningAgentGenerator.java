package evollrn;

import java.util.ArrayList;
import java.util.Random;

import sim.*;

abstract class LearningAgentGenerator {
	public final int tagSize;
	public static final Random rand = Constraints.rand;
	public LearningAgentGenerator(int tagSize) { this.tagSize = tagSize; }

	public abstract LearningAgent[] generate(int amount);

	public static int[] randStr(int size, int rng) {
		int[] ret = new int[size];
		for(int i = 0; i < size; i++) ret[i] = rand.nextInt(rng);
		return ret;
	}
	public static int[] randDT(int depth,int length)
	{
		//TODO same check should not appear on the same path more than once
		int[] mDT = new int[(int) Math.pow(2, depth)];
		ArrayList<Integer> set = new ArrayList<>();
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
			DTWiden(m, 2*index+1, length,remaining);
			DTWiden(m, 2*index+2, length,remaining);
		}
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
				Match m = new MatchDT(randDT(4, tagSize),tagSize);
				agents[i] = new LearningAgent(t,m);
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
				agents[i] = new LearningAgent(t,m);
			}
			return agents;
		}
	}
}