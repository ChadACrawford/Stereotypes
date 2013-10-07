package sim;

import java.util.Random;

import evollrn.Constraints;

import sim.Match;
import sim.Tag;

public class MatchDT extends Match {
	public static final Random rand = Parameters.rand;
	final int TAG_SIZE;
	
	public MatchDT(int[] ms, int TAG_SIZE) {
		this.ms = ms;
		this.TAG_SIZE = TAG_SIZE;
	}
	@Override
	public boolean matchWith(Tag t) {
		return match(t, 0);
	}
	public boolean match(Tag t, int index)
	{
		if(ms[index]<0)
			return ms[index]==-1;
		if(t.ts[ms[index]]==0)
			return match(t,2*index+1);
		else 
			return match(t,2*index+2);
	}
	@Override
	public Object clone() {
		return new MatchDT(ms.clone(), TAG_SIZE);
	}
	@Override
	public void mutate() {
		int ind= rand.nextInt(this.ms.length);
		while(this.ms[ind] <0)
			ind = rand.nextInt(this.ms.length);
		this.ms[ind] = rand.nextInt(TAG_SIZE);
	}
	@Override
	public String toString() {
		return writeNode(0);
	}
	
	public String writeNode(int i)
	{
		if(ms[i]<0)
			return ""+ms[i];
		else return "("+ms[i]+"("+writeNode(i*2+1)+")("+writeNode(i*2+2)+"))";
	}
	
	public int hashCode() {
		int hash = 0;
		for(int i = 0; i < ms.length; i++) hash += (ms[i] + 1) + (TAG_SIZE+1) * i;
		return hash;
	}
	
	@Override
	public Tag genCompliment() {
		return null;
	}
}
