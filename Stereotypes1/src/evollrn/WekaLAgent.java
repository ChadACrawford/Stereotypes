package evollrn;

import sim.Match;
import sim.Simulator;
import sim.Tag;

public class WekaLAgent extends EvolLAgent {
    WekaSetup ws;

	public WekaLAgent(Tag t, WekaMatch m, WekaSetup ws) {
		super(t, m);
        this.ws = ws;
	}

    public int tagIndex() {
        int index = 0;
        for(int i = 0; i < t.ts.length; i++) index += t.ts[i] * Math.pow(2,i);
        return index;
    }

	@Override
	public void learn(Simulator.gTuple[] perf) {
        int tgI = tagIndex();
        if(!ws.wms[tgI].trained) {
			ws.wms[tgI].trainModel(perf);
            ws.wms[tgI].trained = true;
        }
        this.m = ws.wms[tgI];
	}

	@Override
	public void mutateMatch(Constraints c) {
    }
	
	public static class Generator extends LAGenerator {
		public WekaSetup ws1;
		public Generator(WekaSetup ws) {
			this.ws1 = ws;
		}

		public LearningAgent genAgent(Tag t, Match m) {
			if(m == null) return new WekaLAgent(t, new WekaMatch(ws1), ws1);
			return new WekaLAgent(t, (WekaMatch)m, ws1);
		}
	}
}
