package sim;

public abstract class Match {
    private static int ID_COUNT = 0;
    protected final int id = ID_COUNT++;
	public int ms[];
	public Match() {

	}

	public abstract boolean matchWith(Tag t);
	@Override
	public Object clone()  {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public abstract void mutate(double prob);
	
	@Override
	public boolean equals(Object obj) 
	{
		Match other = (Match) obj;
		for(int i = 0;i<ms.length;i++)
		{
			if(other.ms[i]!=this.ms[i])
				return false;
		}
		return true;
	}
	
	public abstract Tag genCompliment();

    public String toString() {
          return this.getClass().toString() + "@" + id;
    }
}