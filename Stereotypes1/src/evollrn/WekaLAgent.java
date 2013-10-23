package evollrn;

import sim.Match;
import sim.Simulator;
import sim.Tag;

public class WekaLAgent extends EvolLAgent {

	public WekaLAgent(Tag t, WekaMatch m) {
		super(t, m);
	}

	@Override
	public void learn(Simulator.gTuple[] perf) {
		WekaMatch wm = (WekaMatch)m;
		if(wm.cModel == null)
			wm.trainModel(perf);
	}

	@Override
	public void mutateMatch(Constraints c) {
    }
	
	public static class Generator extends LAGenerator {
		final WekaSetup ws;
		public Generator(WekaSetup ws) {
			this.ws = ws;
		}
		
		public LearningAgent genAgent(Tag t, Match m) {
			if(m == null) return new WekaLAgent(t, new WekaMatch(ws));
			return new WekaLAgent(t, (WekaMatch)m);
		}
	}
}
