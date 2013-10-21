package sim;

import java.util.Arrays;

public class MatchSim extends MatchString {
	int minHam; int maxHam;
	public MatchSim(Tag t, int minHam, int maxHam) { 
		super(Arrays.copyOf(t.ts, t.ts.length)); 
		this.minHam = minHam;
		this.maxHam = maxHam;
	}

	public boolean matchWith(Tag t2) {
		int matches = 0;
		for(int i = 0; i < ms.length; i++)
			if(ms[i] != t2.ts[i]) matches++;
		return (matches <= maxHam && matches >= minHam);
	}
	@Override
	public void mutate(double prob) {
		throw new RuntimeException();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}