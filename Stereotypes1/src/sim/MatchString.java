
package sim;

import java.util.Arrays;
import java.util.Random;

public class MatchString extends Match {
	public static final Random rand = Parameters.rand;
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
	@Override
	public Object clone() {
		return new MatchString(ms.clone());
	}

	public Tag genCompliment() {
		int[] c1s = Arrays.copyOf(ms, ms.length);
		for(int i = 0; i < c1s.length; i++) if(c1s[i] == 2) c1s[i] = 0;
		return new Tag(c1s);
	}
	
	@Override
	public void mutate() {

		int ind = rand.nextInt(this.ms.length);
		this.ms[ind] = rand.nextInt(3);
	}
	@Override
	public String toString() {
		String s = "";
		for(int i =0;i<ms.length;i++)
		{
			s+=ms[i];
		}
		return s;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(int i = 0; i < ms.length; i++) hash += ms[i] * Math.pow(3, i);
		return hash;
	}
}
