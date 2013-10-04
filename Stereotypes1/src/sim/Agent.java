package sim;

public class Agent {
    public Tag t;
    public Match m;
    public Agent(Tag t, Match m) { this.t = t; this.m = m; }

    public boolean cooperate(Agent b) {
	return m.matchWith(b.t);
    }
}