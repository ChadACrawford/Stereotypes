package evollrn;

import sim.Match;
import sim.Simulator;
import sim.Tag;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class WekaMatch extends Match {

	WekaSetup ws;
	Instances trSet;
	Classifier cModel;
	
	public WekaMatch(WekaSetup ws) {
		this.ws = ws;
	}
	
	@Override
	public boolean matchWith(Tag t) {
		if(cModel == null) {
			//System.out.println("no model");
			return true;
		}
		try {
			double p = cModel.classifyInstance(ws.convTag(t, true,trSet));
//			if(Parameters.rand.nextDouble() < 0.001) System.out.println(p);
			return p < 0.5;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void trainModel(Simulator.gTuple[] perf) {
		J48 nModel = new J48();
		nModel.setUnpruned(true);
		cModel = nModel;
		try {
//			NeuralNetwork nModel = new NeuralNetwork(null, trSet, null);
//			cModel = nModel;
			trSet = ws.genTrainingSet(perf);
			cModel.buildClassifier(trSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object clone() {
		WekaMatch wm = new WekaMatch(ws);
        wm.trSet = trSet; wm.cModel = cModel;
        return wm;
	}
	
	public void mutate(double prob) {
		return;
	}

	public boolean equals(Object o) { return true; }
	
	@Override
	public Tag genCompliment() {
		return null;
	}

    @Override
    public int hashCode() {
        return 0;
    }
}
