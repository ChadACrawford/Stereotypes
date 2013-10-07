package sim;

import java.util.Random;



public class LearningAgent extends Agent {
	public static final Random rand = Parameters.rand;

	public LearningAgent(Tag t, Match m) {
		super(t, m);
	}
	public void mutateTag()
	{
		int ind = rand.nextInt(this.t.ts.length);
		this.t.ts[ind] = (this.t.ts[ind] == 0 ? 1:0);
	}
	public void mutateMatch() {
		this.m.mutate();
	}
	@Override
	public String toString() {
		return "T : " +this.t.toString() +" M : "+ this.m.toString(); 
	}
}