package evollrn;

import sim.Match;
import sim.Simulator.gTuple;
import sim.Tag;

public class EvolLAgent extends LearningAgent {
	public EvolLAgent(Tag t, Match m) {
		super(t, m);
	}
	
	@Override
	public void learn(gTuple[] performance) {}
	
	public void mutateTag(Constraints c)
	{
		for(int i = 0; i < t.ts.length; i++) {
			if(rand.nextDouble() < c.TAG_MUTATE_RATE) {
				t.ts[i] = (t.ts[i] == 0 ? 1:0);
			}
		}
	}
	
	public void mutateMatch(Constraints c) {
		this.m.mutate(c.MATCH_MUTATE_RATE);
//		int[] newMs = new int[m.ms.length];
//		for(int i = 0; i < newMs.length; i++)
//			newMs[i] = 2;
//		this.m = new MatchString(newMs);
	}
	
	public static class Generator extends LAGenerator {
		@Override
		public LearningAgent genAgent(Tag t, Match m) {
			return new EvolLAgent(t,m);
		}
	}
}