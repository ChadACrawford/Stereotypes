package sim;

import java.util.Arrays;

public class Tag {
	public int[] ts;
	public Tag(int[] ts) {
		this.ts = ts;
	}
	public Tag(Tag t) {
		this.ts = Arrays.copyOf(t.ts,t.ts.length);
	}
	@Override
	public String toString() {
		String s = "";
		for(int i =0;i<ts.length;i++)
		{
			s+=ts[i];
		}
		return s;
	}
	@Override
	public boolean equals(Object obj) 
	{
		Tag other = (Tag) obj;
		for(int i = 0;i<ts.length;i++)
		{
			if(other.ts[i]!=this.ts[i])
				return false;
		}
		return true;
	}
	public int hashCode() {
		int hash = 0;
		for(int i = 0; i < ts.length; i++) {
			hash += ts[i] * (int)Math.pow(2,i);
		}
		return hash;
	}
}