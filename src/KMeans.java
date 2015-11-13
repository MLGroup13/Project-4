import java.util.ArrayList;
import java.util.Random;

public class KMeans 
{
	private float [][] data;
	private int K;
	
	public KMeans(float [][] d, int k)
	{
		data = d;
		K = k;
	}
	
	public float [] cluster()
	{	
		Random random = new Random();
		float [] mu = new float [K]; // array of centroids
		ArrayList [] clusters = new ArrayList[4];
		
		float min = Float.POSITIVE_INFINITY;
		float max = 0, range = 0;
		
		// calculate the minimum and maximum feature values 
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < data[i].length; j++)
			{
				if (data[i][j] < min)
					min = data[i][j];
				if (data[i][j] > max)
					max = data[i][j];
			}
		}
		range = max - min;
		
		// randomly initialize the mu's to a range within the datasets range
		for (int i = 0; i < K; i++)
		{
			mu[i] = (random.nextFloat()*range) + min;
		}
		
		for (int i = 0; i < data.length; i++)
		{
			int index = cluster(mu, data[i]);
			clusters[index].add(data[i]);
		}
		
		for(int i = 0; i < clusters.length; i++)
		{
			System.out.println(clusters[i]);
		}
		
		return mu;
	}
	
	private int cluster(float m [], float d[])
	{
		float distance = Float.POSITIVE_INFINITY;
		int mu_index = 0;
		float sum = 0;
		for (int i = 0; i < m.length; i++)
		{
			for (int j = 0; j < d.length; j++)
			{
				sum = sum + Math.abs(d[j] - m[i]);
			}
			if (sum < distance)
			{
				distance = sum;
				mu_index = i;
			}
		}
		
		return mu_index;
	}
}
