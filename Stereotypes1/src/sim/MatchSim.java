
package sim;

public class MatchSim extends Match {
    int ms[];
    Tag t;
    public MatchSim(int[] ms, Tag t) { this.ms = ms; }

    public boolean matchWith(Tag t2) {
	for(int i = 0; i < t.length; i++)
	    if(ms[i] == 1 && t.ts[i] != t2.ts[i]) return false;
	return true;
    }
}