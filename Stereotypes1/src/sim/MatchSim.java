
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
    		if(ms[i] == t2.ts[i]) matches++;
    	return (matches >= minHam && matches <= maxHam);
    }

	@Override
	public void mutate() {
		throw new RuntimeException();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}