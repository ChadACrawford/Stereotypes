
package sim;

import java.util.Arrays;

public class MatchString extends Match {
	public int[] ms;
	public MatchString(int[] ms) {
		this.ms = ms;
	}

	public boolean matchWith(Tag t) {
		for(int i = 0; i < ms.length; i++)
			if (ms[i] != 2 && ms[i] != t.ts[i]) return false;
		return true;
	}
	
	public static MatchString copy(Match m) {
		int[] mref =((MatchString)m).ms;
		return new MatchString(Arrays.copyOf(mref, mref.length));
	}
}