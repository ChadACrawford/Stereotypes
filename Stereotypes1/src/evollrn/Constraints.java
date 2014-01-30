package evollrn;

import sim.Parameters;
import sim.StaticAgentGenerator;

import java.util.Random;

public class Constraints {
	public static final Random rand = Parameters.rand;
	
	public int POOL_SIZE = 100;
	public final int STATIC_POOL_SIZE = 1000;
	
	public final int TAG_SIZE = 8;
	
	public final int LIFETIME = 1000;
	public final boolean ELITISM = false;
	
	public double PAYOFF_CC = 4;
	public double PAYOFF_DC = 0;
	public double PAYOFF_CD = 0;
	public double PAYOFF_DD = 2;
	
	public double TAG_MUTATE_RATE = 0.001;
	public double MATCH_MUTATE_RATE = 0.001;
	public final double SELECT_TOURNAMENT_P = 0.8;
	
	public int HAMMING_MIN = 2;
	public int HAMMING_MAX = 4;
	
	public LAGenerator lGen = new EvolLAgent.Generator();
	public StaticAgentGenerator SAG = new StaticAgentGenerator.SAGSim1(TAG_SIZE,HAMMING_MIN,HAMMING_MAX);
	public PopulationGen LAG = new PopulationGen.LAG1(TAG_SIZE, lGen);

    public Selection EVOLVE_S;
    public Crossover EVOLVE_C;
    public Mutation  EVOLVE_M;
}
