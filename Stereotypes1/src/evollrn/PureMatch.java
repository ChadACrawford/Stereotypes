package evollrn;

import sim.Match;
import sim.Tag;

/**
 * Created by chad on 1/28/14.
 */
public class PureMatch extends Match {

    boolean strategy;
    public PureMatch(boolean s) {
        strategy = s;
    }

    @Override
    public boolean matchWith(Tag t) {
        return strategy;
    }

    @Override
    public void mutate(double prob) {

    }

    @Override
    public Tag genCompliment() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
