package sim;


public class Agent {
    public Tag t;
    public Match m;
    public Agent(Tag t, Match m) { this.t = t; this.m = m; }

    public boolean cooperate(Agent b) {
	return m.matchWith(b.t);
    }
    @Override
    public String toString() {
    	return t.toString() +" "+m.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
    	Agent other = (Agent) obj;
    	return this.m.equals(other.m) &&this.t.equals(other.t);
    }
}