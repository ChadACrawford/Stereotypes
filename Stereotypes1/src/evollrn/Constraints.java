package evollrn;

public class Constraints {
	public final int POOL_SIZE = 100;
	public final int STATIC_POOL_SIZE = 1000;
	
	public final int TAG_SIZE = 8;
	
	public final int LIFETIME = 10000;
	public final boolean ELITISM = false;
	
	public final double TAG_MUTATE_RATE = 0.005;
	public final double MATCH_MUTATE_RATE = 0.005;
	public final double SELECT_TOURNAMENT_P = 0.6;
}
