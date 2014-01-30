package evollrn;

/**
 * Created with IntelliJ IDEA.
 * User: chad
 * Date: 10/21/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */

import sim.*;

import java.util.Random;

public abstract class Mutation {
    private static Random rand = Parameters.rand;
    public final Constraints c;
    public Mutation(Constraints c) {this.c = c;}
    public abstract void mutate(Tag t);
    public void mutate(Match m) {}
    public static class Bit extends Mutation {
        public Bit(Constraints c) { super(c); }

        public void mutate(Tag t) {
            for(int i = 0; i < t.ts.length; i++)
                if(rand.nextDouble() < c.TAG_MUTATE_RATE)
                    t.ts[i] = (t.ts[i] == 1 ? 0:1);
        }

        public void mutate(MatchString m) {
            for(int i = 0; i < m.ms.length; i++)
                if(rand.nextDouble() < c.MATCH_MUTATE_RATE)
                    m.ms[i] = rand.nextInt(3);
        }

        public void mutate(MatchDT m) {
            m.mutate();
        }
    }
}
