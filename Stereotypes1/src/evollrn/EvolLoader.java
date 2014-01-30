package evollrn;

import sim.Parameters;
import sim.StaticAgentGenerator;

import java.util.Random;

public class EvolLoader {
	public static Random rand = Parameters.rand;
	
	public static class RunStats {
		int totalCC,totalDC,totalCD,totalDD;
	}
	
	public static void runTrials(LAGenerator lgen, PopulationGen pg, StaticAgentGenerator SAG, int val) {
		double ts[] = new double[7];
		Constraints c = new Constraints();
		if(val == 0) CalcGameMatrix.arrangeGameMatrixForTS(c);
		else if(val == 1) CalcGameMatrix.arrangeGameMatrixForHD(c);
		else CalcGameMatrix.arrangeGameMatrixForDT(c);
		c.lGen = lgen; c.LAG = pg;
		c.SAG = SAG;
        int trials = 20;
		for(int t = 0; t < trials; t++) {
			EvolPopulation ep = new EvolPopulation(c);
			ep.evolve();
            if(lgen.getClass() == WekaLAgent.Generator.class) {
                ((WekaLAgent.Generator)lgen).ws1 = new WekaSetup(c);
            }
			int[] ps = ep.fitnessTable();
			for(int i = 0; i < 4; i++) ts[i] += ps[i];
            double[] pures = ep.bestPure();
            ts[5] += pures[0];
            ts[6] += pures[1];
		}
		for(int i = 0; i < 4; i++) ts[i] /= (trials * c.POOL_SIZE * c.STATIC_POOL_SIZE);
        ts[4] = ts[0] + ts[3];
        ts[5] /= trials; ts[6] /= trials;
		System.out.format("%10f %10f %10f %10f %10f %10f %10f %n", ts[0], ts[1], ts[2], ts[3], ts[4], ts[5], ts[6]);
	}
	
	public static void runHammingTest1(LAGenerator lgen, PopulationGen PG) {
		final int NUM_TRIALS = 20;
		int[][][] pm = new int[9][9][4];
		double[][] fit = new double[9][9];
		System.out.println("HAMMING DATA FOR: " + lgen.getClass() + ", " + PG.getClass());
		for(int h1 = 0; h1 <= 8; h1+=1)
			for(int h2 = h1; h2 <= 8; h2+=1) {
//				System.out.format("HAMMING DISTANCE: %d-%d%n",h1,h2);
				for(int t = 0; t < NUM_TRIALS; t++) {
					Constraints c = new Constraints();
					c.HAMMING_MIN = h1;
					c.HAMMING_MAX = h2;
					CalcGameMatrix.arrangeGameMatrixForHD(c);
					//c.lgen = new WekaLAgent.Generator(new WekaSetup(c));
					//c.LAG = new PopulationGen.LAGNullMatch(c.TAG_SIZE, c.lgen);
					c.SAG = new StaticAgentGenerator.SAGSim1(c.TAG_SIZE,c.HAMMING_MIN,c.HAMMING_MAX);
					c.lGen = lgen;
					c.LAG = PG;
					EvolPopulation e = new EvolPopulation(c);
					e.evolve();
					double[] payM = {c.PAYOFF_CC, c.PAYOFF_DC, c.PAYOFF_CD, c.PAYOFF_DD};
					int[] ps = e.fitnessTable();
					for(int i = 0; i < 4; i++) {
						pm[h1][h2][i] += ps[i];
						fit[h1][h2] += ps[i] * payM[i];
					}
				}
			}
		
		for(int h1 = 0; h1 <= 8; h1++) {
			for(int h2 = h1; h2 <= 8; h2++) {
				String s = String.format("%1d %8d %15d %15d %15d %15d %12f",
						h1,h2,pm[h1][h2][0],pm[h1][h2][1],pm[h1][h2][2],pm[h1][h2][3],fit[h1][h2]);
				System.out.println(s);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
        Results.initialize();
		//ternary learner
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

		//runTrials(tGen,tPG,tSAG,0);
		//runTrials(dGen,dPG,tSAG,0);
		//runTrials(wGen,wPG,tSAG,0);
		runTrials(tGen,tPG,hSAG,1);
		//runTrials(dGen,dPG,hSAG,1);
		//runTrials(wGen,wPG,hSAG,1);
		//runTrials(tGen,tPG,dSAG,2);
		//runTrials(dGen,dPG,dSAG,2);
		//runTrials(wGen,wPG,dSAG,2);
//		runHammingTest1(wGen, wPG);
//		runHammingTest1(tGen, tPG);
//		//WekaTest1();
//		RandTest1(dGen,dPG);
		//RandTest1(wGen,wPG);
//		RandTest1(tGen,tPG);
//		runIndpVar(dGen,dPG);
//		runGroups(tGen,tPG);
        Results.closeAll();
    }
	
	public static void RandTest1(LAGenerator lgen, PopulationGen pg) {
		System.out.println("RANDOM STATIC DATA FOR: " + lgen.getClass() + ", " + pg.getClass());
		Constraints c = new Constraints();
		c.lGen = lgen;
		c.LAG = pg;
		c.SAG = new StaticAgentGenerator.SAG1(c.TAG_SIZE);
		
		CalcGameMatrix.arrangeGameMatrixForTS(c);
		
		int[] totalPs = new int[4];
		for(int i = 0; i < 20; i++) {
			EvolPopulation ep = new EvolPopulation(c);
			ep.evolve();
			int[] ps = ep.fitnessTable();
			for(int j = 0; j < 4; j++) totalPs[j] += ps[j];
		}
		for(int j = 0; j < 4; j++) totalPs[j] /= 20;
		System.out.format("%10d %10d %10d %10d", totalPs[0],totalPs[1],totalPs[2],totalPs[3]);
		
//		//draw the tree
//		final javax.swing.JFrame jf = 
//				new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
//		jf.setSize(500,400);
//		jf.getContentPane().setLayout(new BorderLayout());
//		TreeVisualizer tv = new TreeVisualizer(null, 
//		jf.getContentPane().add(tv, BorderLayout.CENTER);
//		jf.addWindowListener(new java.awt.event.WindowAdapter() {
//				((J48)((WekaMatch)ep.lagents[0].m).cModel).graph(), new PlaceNode2());
//			public void windowClosing(java.awt.event.WindowEvent e) {
//				jf.dispose();
//			}
//		});
//		
//		//comment this out if you actually don't want to draw the tree
////		jf.setVisible(true);
////		tv.fitToScreen();
////
////		ep.showAllLearningAgents();
////		boolean[] isM = new boolean[c.STATIC_POOL_SIZE];
////		System.out.println("01234567");
////		System.out.println(ep.lagents[0].t);
////		for(int i = 0; i < c.STATIC_POOL_SIZE; i++) {
////			Agent a = ep.sim.agents[i];
////			System.out.println(a.t + ": " + a.m.matchWith(ep.lagents[0].t));
////		}
	}
	
	public static void runIndpVar(LAGenerator lgen, PopulationGen pg) {
//		double incr = 0.015;
		for(int pop = 2; pop <= 50; pop += 1) {
			for(int t = 0; t < 5; t++) {
				Constraints c = new Constraints();
				c.lGen = lgen; c.LAG = pg;
				c.POOL_SIZE = pop;
//				double nmut = mut + incr * (rand.nextDouble() - 1. / 2);
//				if(nmut < 0) nmut = 0;
//				c.MATCH_MUTATE_RATE = nmut; c.TAG_MUTATE_RATE = nmut;
				CalcGameMatrix.arrangeGameMatrixForDT(c);
				c.SAG = new StaticAgentGenerator.SAGDT1(c.TAG_SIZE);//,c.HAMMING_MIN,c.HAMMING_MAX);
				EvolPopulation ep = new EvolPopulation(c);
				ep.evolve();
				int[] ps = ep.fitnessTable();
				double total = c.POOL_SIZE * c.STATIC_POOL_SIZE;
				System.out.format("%4d %10f %10f %10f %10f%n", pop, ps[0] / total, ps[1] / total, 
						ps[2] / total, ps[3] / total);
			}
		}
	}
	
	public static void runGroups(LAGenerator lgen, PopulationGen pg) {
		for(int g = 1; g < 200; g += 1) {
			Constraints c = new Constraints();
			c.SAG = new StaticAgentGenerator.SAGGroup1(c.TAG_SIZE, g);
			c.lGen = lgen; c.LAG = pg;
			CalcGameMatrix.arrangeGameMatrixForTS(c);
			for(int t = 0; t < 3; t++) {
				EvolPopulation ep = new EvolPopulation(c);
				ep.evolve();
				int[] ps = ep.fitnessTable();
				double total = c.POOL_SIZE * c.STATIC_POOL_SIZE;
				System.out.format("%4d %10f %10f %10f %10f%n", g, ps[0] / total, ps[1] / total, 
						ps[2] / total, ps[3] / total);
			}
		}
	}
}
