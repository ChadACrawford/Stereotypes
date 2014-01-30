package sim;

import evollrn.Constraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class StaticAgentGenerator {
	public final int tagSize;
	public static final Random rand = Constraints.rand;

	public StaticAgentGenerator(int tagSize) 
	{
		this.tagSize = tagSize; 
	}

	public abstract StaticAgent[] generate(int amount);

	public static int[] randStr(int size, int rng) {
		int[] ret = new int[size];
		for(int i = 0; i < size; i++) ret[i] = rand.nextInt(rng);
		return ret;
	}

	// Random tag, random match.
	public static class SAG1 extends StaticAgentGenerator {
		public SAG1(int tagSize) {
			super(tagSize);
		}
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				Match m = new MatchString(randStr(tagSize,3));
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}

	// Random tag, general match.
	public static class SAG2 extends StaticAgentGenerator {
		final double PROB_WILD = 0.2;
		public SAG2(int tagSize) {
			super(tagSize);
		}

		
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			int[] baseMtch = randStr(tagSize,2);
			System.out.println("Base: " + new MatchString(baseMtch));
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				int[] nMtch = Arrays.copyOf(baseMtch,baseMtch.length);
				for(int j = 0; j < nMtch.length; j++)
					if(rand.nextDouble() < PROB_WILD) 
						nMtch[j] = 2;
				Match m = new MatchString(nMtch);
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}

	// Random Match, general Tag.
	public static class SAG3 extends StaticAgentGenerator {
		final double PROB_WILD = 0.2;
		public SAG3(int tagSize) {
			super(tagSize);
		}

		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			int[] baseTag = randStr(tagSize,2);
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(Arrays.copyOf(baseTag,tagSize));
				Match m = new MatchString(randStr(tagSize,3));
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}
	
	// general Match, same Tag.
	public static class SAG4 extends StaticAgentGenerator {
		final double PROB_WILD = 0.2;
		public SAG4(int tagSize) {
			super(tagSize);
		}
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			int[] baseTag = randStr(tagSize,2);
			int[] baseMtch = randStr(tagSize,2);
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(Arrays.copyOf(baseTag,tagSize));
				int[] nMtch = Arrays.copyOf(baseMtch,baseMtch.length);
				for(int j = 0; j < nMtch.length; j++) 
					if(rand.nextDouble() < PROB_WILD) 
						nMtch[j] = 2;
				Match m = new MatchString(nMtch);
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}

	//Hamming Distance
	public static class SAGSim1 extends StaticAgentGenerator {
        int minHam, maxHam;
		public SAGSim1(int tagSize, int minHam, int maxHam) {
            super(tagSize);
            this.minHam = minHam;
            this.maxHam = maxHam;
		}
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];

			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				Match m = new MatchSim(t,minHam,maxHam);
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}
	// Random tag, random match.
	public static class SAGDT1 extends StaticAgentGenerator {
		public SAGDT1(int tagSize) {
			super(tagSize);
		}

		
		public StaticAgent[] generate(int amount) {
			StaticAgent[] agents = new StaticAgent[amount];
			for (int i = 0; i < amount; i++) {
				Tag t = new Tag(randStr(tagSize,2));
				Match m = new MatchDT(MatchDT.GENERATE_RANDOM, tagSize);
				agents[i] = new StaticAgent(t,m);
			}
			return agents;
		}
	}

    public static class SAGGroup1 extends StaticAgentGenerator {
        public int TSET_SIZE = 5; //size of the unique identifiers for this set
        public class Group {
            int[] tag;
            public Group() {
                tag = new int[tagSize];
                for(int i = 0; i < tag.length; i++) tag[i] = 2;
            }
            public boolean subsetOf(Group g2) {
                for(int i = 0; i < tag.length; i++)
                    if(tag[i] != 2 && tag[i] != g2.tag[i]) return false;
                return true;
            }
        }
        public final int NUM_GROUPS;
        public final ArrayList<Group> gSet = new ArrayList<Group>();

        /**
         * Generates a set of groups such that the
         * members of one group is not a subset of
         * the members of another group.
         */
        public void genGroups() {
            while(gSet.size() < NUM_GROUPS) {
                Group g = randGroup();
                boolean notSub = true;
                for(Group g2: gSet) if(g.subsetOf(g2)) { notSub = false; break; }
                if(notSub) gSet.add(g);
            }
        }
        public Group randGroup() {
            Group g = new Group();
            for(int i = 0; i < TSET_SIZE; i++) {
                int r = rand.nextInt(tagSize);
                if(g.tag[r] == 2) g.tag[r] = rand.nextInt(2);
                else i--;
            }
            return g;
        }

        public SAGGroup1(int tagSize, int numGroups) {
            super(tagSize);
            NUM_GROUPS = numGroups;

            genGroups();
        }

        @Override
        public StaticAgent[] generate(int amount) {
            StaticAgent[] agents = new StaticAgent[amount];
            for(int i = 0; i < amount; i++) {
                int rg = rand.nextInt(gSet.size());
                Group g = gSet.get(rg);
                int[] ts = new int[tagSize];
                int[] ms = new int[tagSize];
                for(int j = 0; j < tagSize; j++) {
                    ms[j] = g.tag[j];
                    if(g.tag[j] == 2)
                        ts[j] = rand.nextInt(2);
                    else ts[j] = g.tag[j];
                }
                Tag t = new Tag(ts);
                Match m = new MatchString(ms);
                m.mutate(0.1);
                agents[i] = new StaticAgent(t,m);
            }
            return agents;
        }
    }
}