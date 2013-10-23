package sim;


import evollrn.Constraints;

import java.util.ArrayList;

public class DTNode
{
	int checkBit;
	boolean isLeaf;
	boolean result;
	DTNode branch1;
	DTNode branch0;

	boolean findResult(Tag t)
	{
		if(this.isLeaf)
			return result;
		else
		{
			if(t.ts[checkBit]==1)
				return branch1.findResult(t);
			else
				return branch0.findResult(t);
		}	
	}
	public static DTNode generateRandomNode(ArrayList<Integer> checkList)
	{
		DTNode n = new DTNode();
		checkList = new ArrayList<Integer>(checkList);
		if(Constraints.rand.nextBoolean())
		{
			n.makeLeaf();
		}
		else
		{	
			if(checkList.size()==0)
				n.makeLeaf();
			else
			{
				n.isLeaf = false;
				n.checkBit = checkList.get(Constraints.rand.nextInt(checkList.size()));
				checkList.remove(checkList.indexOf(n.checkBit));
				n.branch0 = generateRandomNode(new ArrayList<Integer>(checkList));
				n.branch1 = generateRandomNode(new ArrayList<Integer>(checkList));
			}
		}
		return n;
	}

	public void makeLeaf()
	{
		this.isLeaf = true;
		this.result = Constraints.rand.nextBoolean();
	}
	public boolean match(Tag t) {
		if(this.isLeaf)
			return this.result;
		if(t.ts[this.checkBit]==1)
			return branch1.match(t);
		else 
			return branch0.match(t); 
	}
	@Override
	protected Object clone()
	{
		DTNode copy = new DTNode();
		copy.isLeaf = this.isLeaf;
		copy.result = this.result;

		if(!this.isLeaf)
		{
			copy.branch0 = (DTNode) this.branch0.clone();
			copy.branch1 = (DTNode) this.branch1.clone();
			copy.checkBit = this.checkBit;
		}
		return copy;
	}
	@Override
	public String toString() 
	{

		if(this.isLeaf)
			return ""+this.result;
		else
			return "("+checkBit+" "+ this.branch0+" "+this.branch1+")";
	}

	@Override
	public boolean equals(Object obj) 
	{
		DTNode otherNode = (DTNode) obj;
		if(this.isLeaf && otherNode.isLeaf)
			return this.result == otherNode.result;
		if(this.isLeaf != otherNode.isLeaf)
		{
			return false;
		}
		if(this.branch0.equals(otherNode.branch0) && this.branch1.equals(otherNode.branch1) && this.checkBit == otherNode.checkBit)
			return true;
		return false;
	}
	public DTNode mutate(ArrayList<Integer> checkList) {
		DTNode mutated = (DTNode) this.clone();
		checkList = new ArrayList<Integer>(checkList);

		if(Constraints.rand.nextBoolean())
			return generateRandomNode(checkList);
		if(this.isLeaf)
		{
			return generateRandomNode(checkList);
		}
		else
		{
			checkList.remove(checkList.indexOf(this.checkBit));
			if(Constraints.rand.nextBoolean())
			{
				mutated.branch0 = mutated.branch0.mutate(checkList);
			}
			else
			{
				mutated.branch1 = mutated.branch1.mutate(checkList);
			}
		}
		return mutated;
	}
	
	public void prune()
	{
		if(!this.isLeaf)
		{
			this.branch0.prune();
			this.branch1.prune();
			if(this.branch0.isLeaf && this.branch1.isLeaf)
			{
				if(this.branch0.result == this.branch1.result)
				{
					this.result=branch0.result;
					this.isLeaf=true;
					this.branch0=null;
					this.branch1=null;
				}
			}
		}
	}
	public int getCount() {
		if(this.isLeaf)	
			return 1;
		else
			return this.branch0.getCount()+this.branch1.getCount()+1;
	}
	public int getDepth()
	{
		if(this.isLeaf)
			return 1;
		else
			return 1+ Math.max(this.branch0.getDepth(), this.branch1.getDepth());
	}

}