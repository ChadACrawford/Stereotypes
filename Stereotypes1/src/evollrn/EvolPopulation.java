package evollrn;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
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
		//Sag 1 Statics have Random matching and random tag
		//Sag 2 Statics have generalized from same matching string but different Tags
		//Sag 3 
		sim = new Simulator(c.SAG,c.STATIC_POOL_SIZE);
		lgen = c.LAG;
		lagents = lgen.generate(c.POOL_SIZE);

        c.EVOLVE_S = new Selection.Tournament(this, c);
        c.EVOLVE_C = new Crossover.Asexual();
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
			r = new Results(i,c);
//			//System.out.println(lagents[0]);
//
//			lagents = selectTournament2();
//
////			for(LearningAgent a: lagents) {
////				r.addMember(a);
////				r.addPayoff(fitness(a));
////			}
			int[] ps = fitnessTable();
			r.plays = ps;
            r.payoffs = new double[lagents.length];
            for(int j = 0; j < lagents.length; j++) {
                r.payoffs[j] = fitness(lagents[j]);
            }
//			//int tagCount = r.Tgroups.size();
//			int matCount = r.Mgroups.size();
			if(DEBUG_VERBOSE && (i+1) % 1 == 0) {
                System.out.println("Round " + i + ":");
                printFitnessTable();
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
            r.finish();

            for(int t1 = 0; t1 < 100; t1++) {
                LearningAgent[] sPop = c.EVOLVE_S.select(lagents);
                LearningAgent[] nPop = new EvolLAgent[c.POOL_SIZE];
                for(int j = 0; j < c.POOL_SIZE; j++) {
                    int r1 = rand.nextInt(nPop.length),r2=rand.nextInt(nPop.length);
                    LearningAgent e1 = sPop[r1], e2 = sPop[r2];
                    if(fitness(e1) < fitness(e2)) {
                        LearningAgent temp = e1;
                        e1 = e2; e2 = temp;
                    }
                    nPop[j] = c.lGen.genAgent(c.EVOLVE_C.cross(e1.t,e2.t), c.EVOLVE_C.cross(e1.m,e2.m));
                    nPop[j].mutateTag(c); //nPop[j].mutateMatch(c);
                    nPop[j].learn(sim.interactWith(nPop[j]));
                }
                lagents = nPop;
            }

            for(int t2 = 0; t2 < 100; t2++) {
                LearningAgent[] sPop = c.EVOLVE_S.select(lagents);
                LearningAgent[] nPop = new EvolLAgent[c.POOL_SIZE];
                for(int j = 0; j < c.POOL_SIZE; j++) {
                    int r1 = rand.nextInt(nPop.length),r2=rand.nextInt(nPop.length);
                    LearningAgent e1 = sPop[r1], e2 = sPop[r2];
                    if(fitness(e1) < fitness(e2)) {
                        LearningAgent temp = e1;
                        e1 = e2; e2 = temp;
                    }
                    nPop[j] = c.lGen.genAgent(c.EVOLVE_C.cross(e1.t,e2.t), c.EVOLVE_C.cross(e1.m,e2.m));
                    //nPop[j].mutateTag(c);
                    nPop[j].mutateMatch(c);
                    nPop[j].learn(sim.interactWith(nPop[j]));
                }
                lagents = nPop;
            }
        }
		//Results.closeAll();
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

	public int[] fitnessTable() {
//        Evolving = column player, Static = row player
//        Format: EvolvingStatic
		int cc = 0, dcc = 0, cdc = 0, dcdc = 0; int n = 0;
		for(LearningAgent a: lagents) {
            n += 1;
			int[] pays = new int[4];
			Simulator.gTuple[] res = sim.interactWith(a);
			for(Simulator.gTuple r: res) {
				if (r.c1 && r.c2) pays[0] += 1;
				else if(!r.c1 && r.c2) pays[1] += 1;
				else if(r.c1 && !r.c2) pays[2] += 1;
				else pays[3] += 1;
			}
			cc += pays[0]; dcc += pays[1]; cdc += pays[2]; dcdc += pays[3];
            //if(DEBUG_VERBOSE) System.out.format("%16s %7d %7d%n",a.m,pays[0],pays[3]);
		}
		return new int[] {cc ,dcc, cdc, dcdc, cc+dcdc};
	}

    public int[] perfVector(Agent a) {
        Simulator.gTuple[] res = sim.interactWith(a);
        int[] ret = new int[4];
        for(Simulator.gTuple r: res) {
            if (r.c1 && r.c2) ret[0] += 1;
            else if(!r.c1 && r.c2) ret[1] += 1;
            else if(r.c1 && !r.c2) ret[2] += 1;
            else ret[3] += 1;
        }
        return ret;
    }

    public double[] bestPure() {
        return new double[] {bestTag(new PureMatch(true)), bestTag(new PureMatch(false))};
    }

    public double bestTag(Match m) {
        Tag[] tags = genAllTags();
        int maxCoop = 99999999;
        for(Tag t: tags) {
            int[] r = perfVector(new Agent(t,m));
            int nCoop = r[0] + r[3];
            if(nCoop < maxCoop) maxCoop = nCoop;
        }
        return  maxCoop / (1. * c.STATIC_POOL_SIZE);
    }

    public double[] tagDistr(Match m) {
        Tag[] tags = genAllTags();
        double[] ret = new double[tags.length];
        for(int i = 0; i < tags.length; i++) {
            int[] r = perfVector(new Agent(tags[i],m));
            int nCoop = r[0] + r[3];
            ret[i] = nCoop;
            //if(nCoop > maxCoop) maxCoop = nCoop;
        }
        return ret;
    }

    public Tag[] genAllTags() {
        Tag[] ret = new Tag[(int)Math.pow(2,c.TAG_SIZE)];
        for(int i = 0; i < ret.length; i++) {
            int[] ts = new int[c.TAG_SIZE];
            int marker = i;
            for(int j = c.TAG_SIZE-1; j >= 0; j--) {
                if(marker - Math.pow(2,j) >= 0) {
                    ts[j] = 1;
                    marker -= Math.pow(2,j);
                }
                else ts[j] = 0;
            }
            ret[i] = new Tag(ts);
        }
        return ret;
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
        StaticAgentGenerator hSAG = new StaticAgentGenerator.SAGSim1(8, 2, 4);

        int trials = 1000;
        double[] means = new double[trials];
        double[] stdevs = new double[trials];
        for(int t = 0; t < trials; t++) {
            Constraints c = new Constraints();
            EvolPopulation esim = new EvolPopulation(c);
            double at = esim.bestPure()[0];
            means[t] = at;
            //double[] at = esim.tagDistr(new PureMatch(true));
            //DescriptiveStatistics ds = new DescriptiveStatistics(at);
            //means[t] = ds.getMean();
            //stdevs[t] = ds.getStandardDeviation();
            //if(at > best) best = at;
        }
        DescriptiveStatistics ds = new DescriptiveStatistics(means);
        System.out.println("Mean:" + ds.getMean());
        System.out.println("StdDev:" + ds.getStandardDeviation());

//        ds = new DescriptiveStatistics(stdevs);
//        System.out.println("Mean:" + ds.getMean());
//        System.out.println("StdDev:" + ds.getStandardDeviation());
        /*Results.initialize();
		Constraints c = new Constraints();
		CalcGameMatrix.arrangeGameMatrixForHD(c);

        LAGenerator tGen = new EvolLAgent.Generator();
        PopulationGen tPG = new PopulationGen.LAGMatch1(8, tGen);

        //DT learner
        LAGenerator dGen = new EvolLAgent.Generator();
        PopulationGen dPG = new PopulationGen.LAG1(8, tGen);

        //WEKA learner
        LAGenerator wGen = new WekaLAgent.Generator(new WekaSetup(new Constraints()));
        PopulationGen wPG = new PopulationGen.LAGNullMatch(8,wGen);

        //ternary static
        StaticAgentGenerator tSAG = new StaticAgentGenerator.SAG1(8);

        //hamming static
        StaticAgentGenerator hSAG = new StaticAgentGenerator.SAGSim1(8, 2, 4);

        //DT static
        StaticAgentGenerator dSAG = new StaticAgentGenerator.SAGDT1(8);

        c.lGen = dGen; c.LAG = dPG; c.SAG = new StaticAgentGenerator.SAGSim1(8,2,4);
		EvolPopulation esim = new EvolPopulation(c);
		esim.evolve();

        Simulator.gTuple[] res = esim.sim.interactWith(esim.lagents[0]);
        for(Simulator.gTuple g: res) {
            //System.out.println((g.c1 == g.c2) + ":\t" + g.c1 + "\t" + g.c2 + "\t" + g.t2);
        }
//		System.out.println();
		//esim.showAllLearningAgents();
		//esim.showAllStaticAgents();
//		System.out.println("Imitations :"+Results.imitateCount);
//		System.out.println("Similarpeople :"+Results.similarCount);*/

	}

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

//	public LearningAgent[] selectTournament2() {
//		LearningAgent[] newL = new LearningAgent[c.POOL_SIZE];
//		for(int i = 0; i < c.POOL_SIZE; i++) {
//			LearningAgent a = lagents[i];
//			int rIndex;
//			while(lagents[(rIndex = rand.nextInt(c.POOL_SIZE))] == a);
//			LearningAgent b = lagents[rIndex];
//			double aF = fitness(a), bF = fitness(b);
//			if(bF > aF) {
//				LearningAgent temp = a;
//				a = b;
//				b = temp;
//			}
//
//			if(rand.nextDouble() < c.SELECT_TOURNAMENT_P)
//			{
//				newL[i] = c.lGen.genAgent(new Tag(a.t),(Match)a.m.clone());
//				if(lagents[i] != a) {
//					r.imitateCount++;
//					//newL[i].mutateMatch(c);
//				}
//				else newL[i].m = a.m;
//				newL[i].mutateTag(c);
//			}
//			else
//			{
//				newL[i] = c.lGen.genAgent(new Tag(b.t),(Match)b.m.clone());
//				if(lagents[i] != b) {
//					r.imitateCount++;
//					//newL[i].mutateMatch(c);
//				}
//				else newL[i].m = b.m;
//				newL[i].mutateTag(c);
//			}
//			//newL[i].optimizeMatch(sim.interactWith(newL[i]));
//			newL[i].learn(sim.interactWith(newL[i]));
//		}
//		return newL;
//	}

//	public LearningAgent[] mutate() {
//
//		for(LearningAgent a: lagents) {
//			if(rand.nextDouble() <= c.TAG_MUTATE_RATE) {
//				a.mutateTag(c);
//			}
//
//			if(rand.nextDouble() <= c.MATCH_MUTATE_RATE) {
//				a.mutateMatch(c);
//			}
//		}
//		return lagents;
//	}
