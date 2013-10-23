package evollrn;

import sim.*;

import java.util.Random;

public abstract class LearningAgent extends Agent {
	public static Random rand = Parameters.rand;
	public LearningAgent(Tag t, Match m) {
		super(t, m);
	}
	
	public abstract void learn(Simulator.gTuple[] performance);
	public abstract void mutateTag(Constraints c);
	public abstract void mutateMatch(Constraints c);
	
	@Override
	public String toString() {
		return "T : " +this.t.toString() +" M : "+ this.m.toString(); 
	}
}
