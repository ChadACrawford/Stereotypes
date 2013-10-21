
package sim;

import evollrn.Constraints;

import java.util.Arrays;

public class MatchString extends Match {
	
	public MatchString(int[] ms) {
		this.ms = ms;
	}

	public boolean matchWith(Tag t) {
		boolean isMatch = true;
		for(int i = 0; i < ms.length; i++)
			if (ms[i] != 2 && ms[i] != t.ts[i]) { isMatch = false; break; }
//		if(!isMatch)
//			for(int i = 0; i < ms.length; i++) {
//				if(t.ts[i] != 0) return false;
//			}
		return isMatch;
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
	public void mutate(double prob) {
		//int ind = rand.nextInt(this.ms.length);
		//this.ms[ind] = rand.nextInt(3);
		for(int i = 0; i < ms.length; i++) {
			if(Constraints.rand.nextDouble() < prob) {
				//ms[i] = (ms[i] == 0 ? 1:0);
				ms[i] = Constraints.rand.nextInt(3);
			}
		}
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
