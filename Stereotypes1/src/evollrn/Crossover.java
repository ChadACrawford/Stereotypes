package evollrn;

import sim.Match;
import sim.MatchDT;
import sim.MatchString;
import sim.Tag;

import java.util.Random;

public abstract class Crossover {
	static Random rand = Constraints.rand;
	
	public abstract Tag cross(Tag a, Tag b);
	public Match cross(Match a, Match b) { return (Match)a.clone(); };
	
	public static class Uniform extends Crossover {
		private int[] cross(int[] a, int[] b) {
			int[] r = new int[a.length];
			for(int i = 0; i < r.length; i++) 
				if(rand.nextBoolean()) r[i] = a[i];
				else r[i] = b[i];
			return r;
		}
		
		@Override
		public Tag cross(Tag a, Tag b) {
			return new Tag(cross(a.ts,b.ts));
		}
		
		public MatchString cross(MatchString a, MatchString b) {
			return new MatchString(cross(a.ms,b.ms));
		}

        public MatchDT cross(MatchDT a, MatchDT b) {
            //TODO: Implement sexual reproduction for MatchDT
            return (MatchDT)a.clone();
        }
	}

    public static class Asexual extends Crossover {

        @Override
        public Tag cross(Tag a, Tag b) {
            return new Tag(a);
        }
    }
}
