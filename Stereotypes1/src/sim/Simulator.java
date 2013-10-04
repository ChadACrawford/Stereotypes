/**
 * Simulator
 *
 *  Generates a population of static agents and allows learning agents
 *  to interact with each of the static agents.
 **/

package sim;

class Simulator {
    StaticAgentGenerator sag;
    StaticAgent[] agents;

    public Simulator(StaticAgentGenerator sag, int poolSize) {
	this.sag = sag;
	agents = sag.generate(poolSize);
    }

    class gTuple { boolean c1,c2; Tag t2;
	public gTuple(boolean a, boolean b, Tag c)
	 {c1 = a; c2 = b; t2 = c;}}
    public gTuple[] interactWith(Agent a) {
	gTuple[] ret = new gTuple[agents.length];
	for(int i = 0; i < agents.length; i++) {
	    ret[i] = new gTuple(a.cooperate(agents[i]),
				agents[i].cooperate(a),
				agents[i].t);
	}
	return ret;
    }

}
