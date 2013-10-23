package evollrn;


public class State {

	public int nCC;
	public int nDC;
	public int nCD;
	public int nDD;

	public static String getHeader()
	{
		return "#CC \t #DC \t #CD \t DD";
	}
	@Override
	public String toString() 
	{
		return nCC+"\t"+nDC+"\t"+nCD+"\t"+nDD;
	}
}
