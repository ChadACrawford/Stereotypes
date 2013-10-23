package evollrn;

public class CalcGameMatrix {
	public static double calcHmmForRange(int min,int max,Constraints c)
	{
		double result = 0;
		for(int i=min;i<=max;i++)
		{
			result+=calcHammingProb(i,c);
		}
		return result;
	}
	public static double calcHammingProb(int d, Constraints c)
	{
		return Math.pow(0.5, c.TAG_SIZE-d)*Math.pow(0.5,d)*C(c.TAG_SIZE,d);
	}
	public static int C(int S,int K)
	{
		int result =1;
		for(int i=S;i>K;i--)
		{
			result*=i;
		}
		for(int i=2;i<=(S-K);i++)
		{
			result/=i;
		}
		return result;
	}

	public static void arrangeGameMatrixForTS(Constraints c)
	{
		double K = Math.pow(2.0/3,c.TAG_SIZE);
		double a=1;
		double b=-(a*K)/(1-K);
		// a*k +(1-k)*b ~= 0
		c.PAYOFF_CC = 1;
		c.PAYOFF_DC = c.PAYOFF_CC-a;
		c.PAYOFF_DD = 0;
		c.PAYOFF_CD = c.PAYOFF_DD+b;

//		System.out.println(c.PAYOFF_CC);
//		System.out.println(c.PAYOFF_DC);
//		System.out.println(c.PAYOFF_CD);
//		System.out.println(c.PAYOFF_DD);
	}
	public static void arrangeGameMatrixForHD(Constraints c)
	{
		double K = calcHmmForRange(c.HAMMING_MIN,c.HAMMING_MAX, c);
		double a=1;
		double b=-(a*K)/(1-K);
//		System.out.println(a);
//		System.out.println(b);
		// a*k +(1-k)*b ~= 0
		c.PAYOFF_CC = 1;
		c.PAYOFF_DC = c.PAYOFF_CC-a;
		c.PAYOFF_DD = 0;
		c.PAYOFF_CD = c.PAYOFF_DD+b;
	}
	public static void arrangeGameMatrixForDT(Constraints c)
	{
		double K = 0.5;
		double a=1;//rand.nextDouble();
		double b=-(a*K)/(1-K);
		// a*k +(1-k)*b ~= 0
		c.PAYOFF_CC = 1;
		c.PAYOFF_DC = c.PAYOFF_CC-a;
		c.PAYOFF_DD = 0;
		c.PAYOFF_CD = c.PAYOFF_DD+b;

//		System.out.println(c.PAYOFF_CC);
//		System.out.println(c.PAYOFF_DC);
//		System.out.println(c.PAYOFF_CD);
//		System.out.println(c.PAYOFF_DD);
	}
}
