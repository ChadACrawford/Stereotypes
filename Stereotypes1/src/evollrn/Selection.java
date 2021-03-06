package evollrn;

import sim.Match;
import sim.Tag;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: chad
 * Date: 10/23/13
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Selection {
    public static Random rand = Constraints.rand;
    final EvolPopulation ep;
    final Constraints c;
    public Selection(EvolPopulation ep, Constraints c) {
        this.ep = ep;
        this.c = c;
    }
    public abstract LearningAgent[] select(LearningAgent[] pool);

    public static class Tournament extends Selection {
        public Tournament(EvolPopulation ep, Constraints c) {
            super(ep,c);
        }

        @Override
        public LearningAgent[] select(LearningAgent[] pool) {
            LearningAgent[] newL = new LearningAgent[c.POOL_SIZE];
            LearningAgent best = pool[0];
            double bFit = -1000d;
            for(int i = 0; i < c.POOL_SIZE; i++) {
                LearningAgent a = pool[i];
                int rIndex;
                while((rIndex = rand.nextInt(c.POOL_SIZE)) == i);
                LearningAgent b = pool[rIndex];
//                System.out.println(i + ": " + a + "\t" + b);
                double aF = ep.fitness(a), bF = ep.fitness(b);
                if(aF > bFit) { best = a; bFit = aF; }
                if(bF > aF) {
                    LearningAgent temp = a;
                    a = b;
                    b = temp;
                }

                if(rand.nextDouble() < c.SELECT_TOURNAMENT_P)
                {
                    newL[i] = c.lGen.genAgent(new Tag(a.t),(Match)a.m.clone());
                }
                else
                {
                    newL[i] = c.lGen.genAgent(new Tag(b.t),(Match)b.m.clone());
                }
            }
            if(c.ELITISM) pool[0] = c.lGen.genAgent(new Tag(best.t),(Match)best.m.clone());
//            System.out.println(pool[0]);
            return newL;
        }
    }
}
