package evollrn;

import sim.Simulator;
import sim.Tag;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public class WekaSetup {
	
	private static ArrayList<String> attrVals = new ArrayList<String>();
	private static ArrayList<String> clssVals = new ArrayList<String>();
	private static final boolean __INITIALIZED = CLASS_INIT();
	
	private static boolean CLASS_INIT() {
		attrVals.add("0");
		attrVals.add("1");
		clssVals.add("C");
		clssVals.add("D");
		return true;
	}
	
	private ArrayList<Attribute> attrSet = new ArrayList<Attribute>();
	private Attribute clssObj;
	Constraints c;
	
	public WekaSetup(Constraints c) {
		this.c = c;
		initialize();
	}
	
	public void initialize() {
		for(int i = 0; i < c.TAG_SIZE; i++)
			attrSet.add(new Attribute("tag_" + i, attrVals));
		clssObj = new Attribute("class", clssVals);
		attrSet.add(clssObj);
	}
	
	public String strInt(int a) {
		return (a == 0 ? "0":"1");
	}
	
	public Instances genTrainingSet(Simulator.gTuple[] perf) {
		Instances trSet = new Instances("Training set",attrSet,c.STATIC_POOL_SIZE);
		trSet.setClassIndex(attrSet.size()-1);
		for(Simulator.gTuple p: perf) trSet.add(convTag(p.t2,p.c2,trSet));
		return trSet;
	}
	
	public Instance convTag(Tag t, boolean C, Instances trSet) {
		Instance inst = new DenseInstance(t.ts.length+1);
		for(int i = 0; i < t.ts.length; i++) {
			inst.setValue(attrSet.get(i), t.ts[i]);
		}
		inst.setValue(clssObj, (C ? clssVals.get(0):clssVals.get(1)));
		inst.setDataset(trSet);
		return inst;
	}
}
