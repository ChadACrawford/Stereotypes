package evollrn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import sim.LearningAgent;

public class Results {
	public static FileWriter[] writers;
	public static final String[] filenames = 
			new String[] {
		"tag_groups.data",
		"mat_groups.data",
		"pay_stats.data",
		"identical.data",
		"similar.data"
	};

	public static final String DATA_DIRECTORY = "data/";
	public static DataWriter[] dWriters;

	public static void initialize() {
		try {
			//System.out.println(DATA_DIRECTORY);
			writers = new FileWriter[filenames.length];
			for(int i = 0; i < filenames.length; i++) {
				File f = new File(DATA_DIRECTORY + filenames[i]);
				if(!f.exists()) f.createNewFile(); 
				writers[i] = new FileWriter(f);
			}
			dWriters = new DataWriter[] {
					new DataTagGroups(),
					new DataMatchGroups(),
					new AgentFitnessScores()
					};
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeAll() {
		try {
			for(FileWriter w: writers)
				w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Group> Mgroups = new ArrayList<Group>();
	public ArrayList<Group> Tgroups = new ArrayList<Group>();
	public int imitateCount = 0;
	public int similarCount = 0; 
	final int genNumber;
	
	public double[] payoffs;
	private int pCount = 0;
	
	public Results(int genNumber, Constraints c) {
		this.genNumber = genNumber;
		payoffs = new double[c.POOL_SIZE];
	}
	
	public void finish() {
		try {
			for(int i = 0; i < dWriters.length; i++) {
				dWriters[i].write(this, writers[i]);
				writers[i].flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addPayoff(double pay) {
		payoffs[pCount++] = pay;
	}
	
	public class Group {
		int hash;
		int size = 1;
	}
	void resetGroups() { Mgroups.clear(); Tgroups.clear(); }
	void addMember(LearningAgent a) {
		boolean isInM = false;
		for(Group g: Mgroups) if(g.hash == a.m.hashCode()) { g.size++; isInM = true; }
		if(!isInM) {
			Group g = new Group();
			g.hash = a.m.hashCode();
			Mgroups.add(g);
		}
		
		boolean isInT = false;
		for(Group g: Tgroups) if(g.hash == a.t.hashCode()) { g.size++; isInT = true; }
		if(!isInT) {
			Group g = new Group();
			g.hash = a.t.hashCode();
			Tgroups.add(g);
		}
	}

	static String genRow(double... cols) {
		String a = "";
		for(double s: cols) {
			a += s + "\t";
		}
		return a;
	}
	
	static abstract class DataWriter {
		abstract void write(Results r, FileWriter fw) throws IOException;
	}
	
	static class DataTagGroups extends DataWriter {
		void write(Results r, FileWriter fw) throws IOException {
			double [] gsizes = new double[r.Tgroups.size()];
			for(int i = 0; i < r.Tgroups.size(); i++) gsizes[i] = r.Tgroups.get(i).size;
			DescriptiveStatistics ds = new DescriptiveStatistics(gsizes);
			double mean = ds.getMean(),
					stdev = ds.getStandardDeviation(),
					ubound = mean+stdev,
					lbound = mean-stdev,
					min = ds.getMin(),
					max = ds.getMax();
			fw.write(genRow(r.genNumber,mean,stdev,ubound,lbound,min,max));
		}
	}
	
	static class DataMatchGroups extends DataWriter {
		void write(Results r, FileWriter fw) throws IOException {
			double[] gsizes = new double[r.Mgroups.size()];
			for(int i = 0; i < r.Mgroups.size(); i++) gsizes[i] = r.Mgroups.get(i).size;
			DescriptiveStatistics ds = new DescriptiveStatistics(gsizes);
			double mean = ds.getMean(),
					stdev = ds.getStandardDeviation(),
					ubound = mean+stdev,
					lbound = mean-stdev,
					min = ds.getMin(),
					max = ds.getMax();
			fw.write(genRow(r.genNumber,mean,stdev,ubound,lbound,min,max));
		}
	}
	
	static class AgentFitnessScores extends DataWriter {
		@Override
		void write(Results r, FileWriter fw) throws IOException {
			DescriptiveStatistics ds = new DescriptiveStatistics(r.payoffs);
			double mean = ds.getMean(),						//2
					stdev = ds.getStandardDeviation(),		//3
					ubound = mean+stdev,					//4
					lbound = mean-stdev,					//5
					min = ds.getMin(),						//6
					max = ds.getMax();						//7
			String row = genRow(r.genNumber,mean,stdev,lbound,min,max,ubound);
			System.out.println(row);
			fw.write(row + "\n");
		}
	}
}
