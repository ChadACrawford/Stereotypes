package evollrn;

import java.util.Random;

import sim.Parameters;

public class Constraints {
	public static final Random rand = Parameters.rand;
	
	public final int POOL_SIZE = 100;
	public final int STATIC_POOL_SIZE = 1000;
	
	public final int TAG_SIZE = 8;
	
	public final int LIFETIME = 200;
	public final boolean ELITISM = false;
	
	public final double TAG_MUTATE_RATE = 0.02;
	public final double MATCH_MUTATE_RATE = 0.10;
	public final double SELECT_TOURNAMENT_P = 1.0;
}
