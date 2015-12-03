import java.util.ArrayList;
import java.util.List;

public class Cluster 
{
	private float [] mu;
	private List<Integer> member = new ArrayList<>();
	private float std;	// float to store the cluster's standard deviation
	
	public Cluster(float [] m)
	{
		mu = m;
	}
	
	public void add(int i)
	{
		member.add(i);
	}
	
	// remove member with value i from member if it contains member with value i
	public void remove(int i)
	{
		if(member.contains(i))
		{
			member.remove(member.indexOf(i));
		}
	}
	
	public void clearMembers()
	{
		member.clear();
	}
	
	public int [] getMembers()
	{
		int m [] = new int[member.size()];
		
		for (int i = 0; i < member.size(); i++)
		{
			m[i] = member.get(i);
		}
		
		return m;
	}
	
	public int getNumMembers()
	{
		return member.size();
	}
	
	public void calcMu(float [][] d)
	{
		for (int i = 0; i < mu.length; i++)
		{
			float newMu = 0;
			for (int j = 0; j < member.size(); j++)
			{
				newMu = newMu + d[member.get(j)][i];
			}
			newMu = newMu / member.size();
			if (member.size() > 0)
				mu[i] = newMu;
		}
	}
	
	public void setMu(float [] m)
	{
		mu = m;
	}
	
	public float [] getMu()
	{
		return mu;
	}
	
	public int getMuLength()
	{
		return mu.length;
	}
	
	public void setStd(float s)
	{
		std = s;
	}
	
	public float getStd()
	{
		return std;
	}
	
	public void printMu()
	{
		System.out.print("Mu:");
		for (int i = 0; i < mu.length; i++)
		{
			System.out.print(mu[i] + ",");
		}
	}
	
	public void printMembers()
	{
		System.out.print("Members:");
		for (int i = 0; i < member.size(); i++)
		{
			System.out.print(member.get(i) + ",");
		}
	}
}
