package evollrn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import sim.*;

class EvolPopulation {
	LearningAgent[] lagents;

	static Random rand = Constraints.rand;
	Constraints c;
	Simulator sim;
	LearningAgentGenerator lgen;
	
	public void showAllStaticAgents()
	{
		for(StaticAgent sa : sim.agents)
		{
			System.out.println(sa);
		}
	}
	public EvolPopulation(Constraints con) {
		c = con;
		Results.initialize();
		//Sag 1 Statics have Random matching and random tag
		//Sag 2 Statics have generalized from same matching string but different Tags
		//Sag 3 
		sim = new Simulator(new StaticAgentGenerator.SAGSim1(c.TAG_SIZE),c.STATIC_POOL_SIZE);
		lgen = new LearningAgentGenerator.LAG1(c.TAG_SIZE);
		lagents = lgen.generate(c.POOL_SIZE);
//		lagents[0].m = new MatchString(new int[] {2});
	}

	private Results r;
	public void evolve() {
		printFitnessTable();
		for(int i = 0; i < c.LIFETIME; i++) {
			r = new Results(i,c);
			
			lagents = selectTournament2();
			
			if(i % 1 == 0) {
				for(LearningAgent a: lagents) {
					r.addMember(a);
					r.addPayoff(fitness(a));
				}
				
				System.out.println("\nGeneration " + (i));
				
				int tagCount = r.Tgroups.size();
				int matCount = r.Mgroups.size();
				System.out.println("Unique Tags: " + tagCount);
				System.out.println("Unique Matches: " + matCount);
				
				printFitnessTable();
				System.out.println("\nImitations: " + r.imitateCount);

				r.finish();
				
				if(tagCount == 1 && matCount == 1) break;
			}
			else
				r.finish();
		}
		Results.closeAll();
	}

	public double fitness(Agent a) {
		double weight = 0;
		Simulator.gTuple[] res = sim.interactWith(a);
		for(Simulator.gTuple r: res) {
			if     (r.c1 && r.c2) weight += 1;
			else if(!r.c1 && r.c2) weight += .5;
			else if(r.c1 && !r.c2) weight += -.02;
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

	public LearningAgent[] selectTournament() 
	{
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
			else
				newL[i] = new LearningAgent(new Tag(b.t),(Match) b.m.clone());
		}
		return newL;
	}

	public LearningAgent[] selectTournament2() 
	{
		LearningAgent[] newL = new LearningAgent[c.POOL_SIZE];
		for(int i = 0; i < c.POOL_SIZE; i++) {
			LearningAgent a = lagents[i];
			int rIndex;
			while(lagents[(rIndex = rand.nextInt(c.POOL_SIZE))] == a);
			LearningAgent b = lagents[rIndex];
			if(a.equals(b)) 
			{
				newL[i] = a;
				continue;
			}
			double aF = fitness(a), bF = fitness(b);
			if(bF > aF) { 
				LearningAgent temp = a;
				a = b;
				b = temp;
			}
			if(rand.nextDouble() < c.SELECT_TOURNAMENT_P)
			{	
				newL[i] = new LearningAgent(new Tag(a.t),(Match) a.m.clone());
				if(lagents[i] == b)
				{
					r.imitateCount++;
					if(rand.nextDouble() <= c.TAG_MUTATE_RATE)
					{
						newL[i].mutateMatch();
						newL[i].mutateTag();
					}
				}


			}
			else
			{	
				newL[i] = new LearningAgent(new Tag(b.t),(Match) b.m.clone());
				if(lagents[i] == a)
				{
					r.imitateCount++;
					if(rand.nextDouble() <= c.TAG_MUTATE_RATE)
					{
						newL[i].mutateMatch();
						newL[i].mutateTag();
					}
				}

			}
		}
		return newL;
	}
	
	public LearningAgent[] mutate() {

		for(LearningAgent a: lagents) {
			if(rand.nextDouble() <= c.TAG_MUTATE_RATE) {
				a.mutateTag();

			}

			if(rand.nextDouble() <= c.MATCH_MUTATE_RATE) {
				a.mutateMatch();
			}
		}
		return lagents;
	}

	public int[] fitnessTable() {
		int cc = 0, dcc = 0, cdc = 0, dcdc = 0;
		for(LearningAgent a: lagents) {
			int[] pays = new int[4];
			Simulator.gTuple[] res = sim.interactWith(a);
			for(Simulator.gTuple r: res) {
				if (r.c1 && r.c2) pays[0] += 1;
				else if(!r.c1 && r.c2) pays[1] += 1;
				else if(r.c1 && !r.c2) pays[2] += 1;
				else pays[3] += 1;
			}
			cc += pays[0]; dcc += pays[1]; cdc += pays[2]; dcdc += pays[3];
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
//		System.out.println();
		//esim.showAllLearningAgents();
		//esim.showAllStaticAgents();
//		System.out.println("Imitations :"+Results.imitateCount);
//		System.out.println("Similarpeople :"+Results.similarCount);
	}

}
