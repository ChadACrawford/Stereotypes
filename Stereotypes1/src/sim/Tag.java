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
}