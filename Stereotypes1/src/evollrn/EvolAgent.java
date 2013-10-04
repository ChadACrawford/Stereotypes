package evollrn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import sim.*;

class EvolPopulation {
	LearningAgent[] lagents;
	
	static Random rand = new Random();
	Constraints c;
	Simulator sim;
	LearningAgentGenerator lgen;
	
	public EvolPopulation(Constraints con) {
		c = con;
		sim = new Simulator(new StaticAgentGenerator.SAG3(c.TAG_SIZE),c.STATIC_POOL_SIZE);
		lgen = new LearningAgentGenerator.LAG1(c.TAG_SIZE);
		lagents = lgen.generate(c.POOL_SIZE);
	}
	
	public void evolve() {
		for(int i = 0; i < c.LIFETIME; i++) {
			if(i % 100 == 0) {
				System.out.println("\nGeneration " + (i+1));
				printFitnessTable();
			}
			//int[] lpays = fitnessTable();
			lagents = selectTournament();
			lagents = mutate();
			//int[] npays = fitnessTable();
			//if(Arrays.equals(lpays, npays)) lagents = lgen.generate(c.POOL_SIZE);
		}
	}
	
	public double fitness(Agent a) {
		double weight = 0;
		Simulator.gTuple[] res = sim.interactWith(a);
		for(Simulator.gTuple r: res) {
			if     (r.c1 && r.c2) weight += 1;
			else if(!r.c1 && r.c2) weight += .5;
			else if(r.c1 && !r.c2) weight += .5;
			else weight += 0;
		}
		return weight;
	}
	
	public LearningAgent[] selectFProportionate() {
		//generate fitness utility
		WeightedList<LearningAgent> wl = new WeightedList<LearningAgent>();
		for(LearningAgent a: lagents) {
			double weight = fitness(a);
			wl.add(new LearningAgent(new Tag(a.t),MatchString.copy(a.m)), weight);
		}
		
		LearningAgent[] newL = new LearningAgent[c.POOL_SIZE];
		for(int i = 0; i < newL.length; i++) newL[i] = wl.probSelect();
		
		return newL;
	}
	
	public LearningAgent[] selectTournament() {
		LearningAgent[] newL = new LearningAgent[c.POOL_SIZE];
		for(int i = 0; i < c.POOL_SIZE; i++) {
			LearningAgent a = lagents[rand.nextInt(c.POOL_SIZE)],
					b = lagents[rand.nextInt(c.POOL_SIZE)];
			double aF = fitness(a), bF = fitness(b);
			if(bF > aF) { 
				LearningAgent temp = a;
				a = b;
				b = temp;
			}
			
			if(rand.nextDouble() < c.SELECT_TOURNAMENT_P)
				newL[i] = new LearningAgent(new Tag(a.t),MatchString.copy(a.m));
			else
				newL[i] = new LearningAgent(new Tag(b.t),MatchString.copy(b.m));
		}
		return newL;
	}
	
	public LearningAgent[] mutate() {
		for(LearningAgent a: lagents) {
			if(rand.nextDouble() <= c.TAG_MUTATE_RATE) {
				int ind = rand.nextInt(a.t.ts.length);
				a.t.ts[ind] = (a.t.ts[ind] == 0 ? 1:0);
			}
			
			if(rand.nextDouble() <= c.MATCH_MUTATE_RATE) {
				MatchString m = ((MatchString)a.m); //never ever write this again
				int ind = rand.nextInt(m.ms.length);
				m.ms[ind] = rand.nextInt(3);
			}
		}
		return lagents;
	}
	
	public int[] fitnessTable() {
		int cc = 0, dcc = 0, cdc = 0, dcdc = 0;
		for(LearningAgent a: lagents) {
			Simulator.gTuple[] res = sim.interactWith(a);
			for(Simulator.gTuple r: res) {
				if (r.c1 && r.c2) cc += 1;
				else if(!r.c1 && r.c2) dcc += 1;
				else if(r.c1 && !r.c2) cdc += 1;
				else dcdc += 1;
			}
		}
		return new int[] {cc ,dcc, cdc, dcdc};
	}
	
	public void printFitnessTable() {
		int[] pays = fitnessTable();
		System.out.format(
				"          P1           %n" +
				"P2  |  C     |  DC     %n" +
				"----|--------|---------%n" +
				" C  |%7d | %7d%n" +
				"----|--------|---------%n" +
				" DC |%7d | %7d",pays[0],pays[1],pays[2],pays[3]);
	}
	
	public static void main(String[] args) {
		EvolPopulation esim = new EvolPopulation(new Constraints());
		esim.evolve();
	}
}