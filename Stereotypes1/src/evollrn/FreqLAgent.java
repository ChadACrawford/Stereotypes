package evollrn;

import sim.Match;
import sim.MatchString;
import sim.Simulator;
import sim.Tag;

import java.util.ArrayList;

public class FreqLAgent extends EvolLAgent {

	public FreqLAgent(Tag t, Match m) {
		super(t, m);
	}

	@Override
	public void learn(Simulator.gTuple[] performance) {
		ArrayList<Tag> ct = new ArrayList<Tag>(),
				dt = new ArrayList<Tag>();
		for(Simulator.gTuple gt: performance) 
			if(gt.c2) ct.add(gt.t2);
			else dt.add(gt.t2);

		final double MATCH_TOLERANCE = 0.90;

		int[] sumC = new int[t.ts.length],
				//					sumD = new int[t.ts.length],
				nm = new int[t.ts.length];
		for(int i = 0; i < nm.length; i++) {
			for(Tag t2: ct) sumC[i] += t2.ts[i];
			if(sumC[i] >= MATCH_TOLERANCE * ct.size())
				nm[i] = 1;
			else if(sumC[i] <= (1-MATCH_TOLERANCE) * ct.size())
				nm[i] = 0;
			else nm[i] = 2;
			//				for(Tag t3: dt) sumD[i] += t3.ts[i];
			//				if(sumC[i] - sumD[i] <= -MATCH_TOLERANCE * sumC[i])
			//					nm[i] = 0;
			//				if(sumC[i] - sumD[i] >= MATCH_TOLERANCE * sumC[i])
			//					nm[i] = 1;
			//				else nm[i] = 2;
		}
		this.m = new MatchString(nm);
	}
	
	public static class Generator extends LAGenerator {
		@Override
		public LearningAgent genAgent(Tag t, Match m) {
			return new FreqLAgent(t,m);
		}
	}
}
