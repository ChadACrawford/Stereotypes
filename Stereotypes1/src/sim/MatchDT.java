package sim;

import java.util.ArrayList;

public class MatchDT extends Match {
    public static final int GENERATE_BLANK=0;
    public static final int GENERATE_RANDOM=1;

    public DTNode root;
    int tagSize;

    public MatchDT(int x, int tagSize)
    {
        this.tagSize = tagSize;
        ArrayList<Integer> listOfInts = new ArrayList<Integer>();
        for(int i = 0; i < tagSize; i++) listOfInts.add(i);
        switch (x) {
            case GENERATE_BLANK: break;
            case GENERATE_RANDOM:
                this.root = DTNode.generateRandomNode(listOfInts);
                this.root.prune();
                break;
            default:break;
        }

    }

    @Override
    public boolean matchWith(Tag t) {
        return this.root.match(t);
    }

    @Override
    public Object clone() {
        MatchDT dt = new MatchDT(GENERATE_BLANK,tagSize);
        dt.root = (DTNode) this.root.clone();
        return dt;

    }

    public void mutate()
    {
        ArrayList<Integer> listOfInts = new ArrayList<Integer>();
        for(int i = 0; i < tagSize; i++) listOfInts.add(i);
        this.root = this.root.mutate(listOfInts);
        this.root.prune();
    }
//    @Override
//    public String toString() {
//        return this.
//        //return root.toString();
//    }
    @Override
    public boolean equals(Object obj)
    {
        return this.root.equals(((MatchDT)obj).root);
    }
    @Override
    public void mutate(double prob) {
        // TODO Auto-generated method stub

    }
    @Override
    public Tag genCompliment() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }
}