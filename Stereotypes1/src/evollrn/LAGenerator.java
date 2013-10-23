package evollrn;

import sim.Match;
import sim.Tag;

public abstract class LAGenerator {
	public abstract LearningAgent genAgent(Tag t, Match m);
}
