
package sim;

public MatchString extends Match {
    public int[] ms;
    public MatchString(int[] ms) {
	this.ms = ms;
    }

    public boolean matchWith(Tag t) {
	for(int i = 0; i < t.length; i++)
	    if (ms[i] != 2 && ms[i] != t.ts[i]) return false;
	return true;
    }
}