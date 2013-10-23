package evollrn;

import sim.Simulator;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: chad
 * Date: 10/23/13
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Fitness {

    final Simulator sim;
    final Constraints c;
    private HashMap<Integer, Double> fitVals = new HashMap<Integer, Double>();
    public Fitness(Simulator sim, Constraints c) {
        this.sim = sim;
        this.c = c;
    }

    public double getFitness(EvolLAgent a) {
        double weight = 0;
        Simulator.gTuple[] res = sim.interactWith(a);
        for(Simulator.gTuple r: res) {
            if     (r.c1 && r.c2) weight += c.PAYOFF_CC;
            else if(!r.c1 && r.c2) weight += c.PAYOFF_DC;
            else if(r.c1 && !r.c2) weight += c.PAYOFF_CD;
            else weight += c.PAYOFF_DD;
        }
        return weight;
    }
}
