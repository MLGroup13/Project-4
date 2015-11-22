import java.util.ArrayList;
import java.util.List;

public class Cluster 
{
	float [] mu;
	List<Integer> member = new ArrayList<>();
	
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
