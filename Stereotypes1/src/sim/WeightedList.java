package sim;

import java.util.ArrayList;
import java.util.Random;

public class WeightedList<T> {
	static Random rand = new Random();
	
	class WTuple { T item; double weight; }
	ArrayList<WTuple> items = new ArrayList<WTuple>();
	double size = 0;
	
	public void add(T item, double weight) {
		WTuple wt = new WTuple();
		wt.item = item; wt.weight = weight;
		items.add(wt);
		size += weight;
	}
	
	public T probSelect() {
		double r = rand.nextDouble() * size;
		T i1 = items.get(0).item;
		for(WTuple i2: items) {
			i1 = i2.item;
			if((r -= i2.weight) <= 0) break;
		}
		return i1;
	}
}