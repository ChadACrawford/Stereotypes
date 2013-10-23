package evollrn;

import sim.*;

import java.util.Random;

class EvolPopulation {
	LearningAgent[] lagents;

	static Random rand = Constraints.rand;
	Constraints c;
	Simulator sim;
	PopulationGen lgen;
	
	public final boolean DEBUG_VERBOSE = true;
	
	public void debug(String s) {
		if(DEBUG_VERBOSE) System.out.print(s);
	}
	
	public void showAllStaticAgents()
	{
		for(StaticAgent sa : sim.agents)
		{
			System.out.println(sa);
		}
	}
	
	public void showAllLearningAgents()
	{
		for(LearningAgent sa : lagents)
		{
			System.out.println(sa.getClass());
		}
	}
	
	public EvolPopulation(Constraints con) {
		c = con;
		Results.initialize();
		//Sag 1 Statics have Random matching and random tag
		//Sag 2 Statics have generalized from same matching string but different Tags
		//Sag 3 
		sim = new Simulator(c.SAG,c.STATIC_POOL_SIZE);
		lgen = c.LAG;
		lagents = lgen.generate(c.POOL_SIZE);

        c.EVOLVE_S = new Selection.Tournament(this, c);
        c.EVOLVE_C = new Crossover.Uniform();
        c.EVOLVE_M = new Mutation.Bit(c);
		//create an optimal agent
		//lagents[0].t = new Tag(new int[]{0,0,0,0,0,0,0,0});
		//lagents[0].m = new MatchString(new int[] {2,2,2,2,2,2,2,2});
//		System.out.println(lagents[0]);
//		for(Agent a: sim.agents) System.out.println(a);
	}

	public Tag genCompliment() {
		int[] ts = new int[c.TAG_SIZE];
		for(Agent a: sim.agents) {
			for(int i = 0; i < ts.length; i++)
				if(a.m.ms[i] != 2) ts[i] = a.m.ms[i];
		}
		return new Tag(ts);
	}
	
	private Results r;
	public void evolve() {
//		printFitnessTable();
		for(int i = 0; i < c.LIFETIME; i++) {
//			r = new Results(i,c);
//			//System.out.println(lagents[0]);
//
//			lagents = selectTournament2();
//
////			for(LearningAgent a: lagents) {
////				r.addMember(a);
////				r.addPayoff(fitness(a));
////			}
//			//int[] ps = fitnessTable();
//			//double[] ps1 = new double[]{ps[0],ps[1],ps[2],ps[3]};
//			//r.payoffs[i] = ps1;
//			//int tagCount = r.Tgroups.size();
//			int matCount = r.Mgroups.size();
			if((i+1) % 100 == 0) {
				//int[] ps = fitnessTable();
				//System.out.format("%4d %10d %10d %10d %10d%n", i, ps[0], ps[1], ps[2], ps[3]);
            }
//				System.out.format(
//						"Gen %3d: %3d tags,%3d matches; %3d imitations%n",
//						i,tagCount,matCount,r.imitateCount
//						);
//
//				printFitnessTable();
//				System.out.print("T groups: {");
//				for(Results.Grooup g: r.Tgroups) System.out.print(g.o + "(" + g.size + "),");
//				System.out.println("}");
//
//				System.out.print("M groups: {");
//				for(Results.Group g: r.Mgroups) System.out.print(g.o + "(" + g.size + "),");
//				System.out.print("}\n");
//
//				r.finish();
//
//			}
//			else
//				r.finish();
//			if(tagCount <= 1 && matCount <= 1) break;
            //System.out.println(lagents.length);
            LearningAgent[] sPop = c.EVOLVE_S.select(lagents);
            LearningAgent[] nPop = new EvolLAgent[c.POOL_SIZE];
            for(int j = 0; j < c.POOL_SIZE; j++) {
                int r1 = rand.nextInt(nPop.length),r2=rand.nextInt(nPop.length);
                LearningAgent e1 = sPop[r1], e2 = sPop[r2];
                nPop[j] = new EvolLAgent(c.EVOLVE_C.cross(e1.t,e2.t), c.EVOLVE_C.cross(e1.m,e2.m));
                nPop[j].mutateTag(c); nPop[j].mutateMatch(c);
                nPop[j].learn(sim.interactWith(nPop[j]));
            }
            lagents = nPop;
		}
		Results.closeAll();
//		System.out.println(lagents[0].m);
	}

	public double fitness(Agent a) {
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

//	public EvolLAgent[] selectFProportionate() {
//		//generate fitness utility
//		WeightedList<EvolLAgent> wl = new WeightedList<EvolLAgent>();
//		for(LearningAgent a: lagents) {
//			double weight = fitness(a);
//			wl.add(new EvolLAgent(new Tag(a.t),MatchString.copy(a.m)), weight);
//		}
//
//		EvolLAgent[] newL = new EvolLAgent[c.POOL_SIZE];
//		for(int i = 0; i < newL.length; i++) newL[i] = wl.probSelect();
//
//		return newL;
//	}

//	public EvolLAgent[] selectTournament() 
//	{
//		EvolLAgent[] newL = new EvolLAgent[c.POOL_SIZE];
//		for(int i = 0; i < c.POOL_SIZE; i++) {
//			LearningAgent a = lagents[rand.nextInt(c.POOL_SIZE)],
//					b = lagents[rand.nextInt(c.POOL_SIZE)];
//			double aF = fitness(a), bF = fitness(b);
//			if(bF > aF) { 
//				LearningAgent temp = a;
//				a = b;
//				b = temp;
//			}
//			else
//				newL[i] = new EvolLAgent(new Tag(b.t),(Match) b.m.clone());
//		}
//		return newL;
//	}

	public LearningAgent[] selectTournament2() {
		LearningAgent[] newL = new LearningAgent[c.POOL_SIZE];
		for(int i = 0; i < c.POOL_SIZE; i++) {
			LearningAgent a = lagents[i];
			int rIndex;
			while(lagents[(rIndex = rand.nextInt(c.POOL_SIZE))] == a);
			LearningAgent b = lagents[rIndex];
			double aF = fitness(a), bF = fitness(b);
			if(bF > aF) {
				LearningAgent temp = a;
				a = b;
				b = temp;
			}
			
			if(rand.nextDouble() < c.SELECT_TOURNAMENT_P)
			{
				newL[i] = c.lGen.genAgent(new Tag(a.t),(Match)a.m.clone());
				if(lagents[i] != a) {
					r.imitateCount++;
					//newL[i].mutateMatch(c);
				}
				else newL[i].m = a.m;
				newL[i].mutateTag(c);
			}
			else
			{
				newL[i] = c.lGen.genAgent(new Tag(b.t),(Match)b.m.clone());
				if(lagents[i] != b) {
					r.imitateCount++;
					//newL[i].mutateMatch(c);
				}
				else newL[i].m = b.m;
				newL[i].mutateTag(c);
			}
			//newL[i].optimizeMatch(sim.interactWith(newL[i]));
			newL[i].learn(sim.interactWith(newL[i]));
		}
		return newL;
	}
	
	public LearningAgent[] mutate() {

		for(LearningAgent a: lagents) {
			if(rand.nextDouble() <= c.TAG_MUTATE_RATE) {
				a.mutateTag(c);
			}

			if(rand.nextDouble() <= c.MATCH_MUTATE_RATE) {
				a.mutateMatch(c);
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
						"          C      DC%n" +
//						"----|---------|---------|---------%n" +
						"  C %7d %7d %7d %n" +
//						"----|---------|---------|---------%n" +
						" DC %7d %7d %7d%n" +
//						"----|---------|---------|---------%n" +
						"SUM %7d %7d%n",
						pays[0],		pays[1],		pays[0]+pays[1],
						pays[2],		pays[3],		pays[2]+pays[3],
						pays[0]+pays[2],pays[1]+pays[3]);
	}

	public static void main(String[] args) {
		Constraints c = new Constraints();
		CalcGameMatrix.arrangeGameMatrixForHD(c);
		EvolPopulation esim = new EvolPopulation(c);
		esim.evolve();
//		System.out.println();
		//esim.showAllLearningAgents();
		//esim.showAllStaticAgents();
//		System.out.println("Imitations :"+Results.imitateCount);
//		System.out.println("Similarpeople :"+Results.similarCount);
	}

}
