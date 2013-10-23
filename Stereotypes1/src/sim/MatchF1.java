
package sim;

import java.util.Arrays;

public class MatchF1 extends MatchString {
    int rngSim; 
    int ham;
    public MatchF1(Tag t, int rngSim, int ham) { 
    	super(Arrays.copyOf(t.ts, t.ts.length));
    	this.rngSim = rngSim;
    	this.ham = ham;
    	}

    public boolean matchWith(Tag t2) {    	
    	for(int i = 0; i < rngSim; i++) if(ms[i] != t2.ts[i]) return false;
    	int dist = ms.length - rngSim;
    	for(int i = rngSim; i < ms.length; i++) if(ms[i] == t2.ts[i]) dist--;
    	return dist <= ham;
    }

	@Override
	public void mutate(double prob) {
		throw new RuntimeException();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}