import java.util.ArrayList;
import java.util.Arrays;
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
	
	public void cluster()
	{	
		Random random = new Random();
		Cluster [] cluster = new Cluster[K];
		float [][] prevCluster = new float[K][data[0].length-1];
		
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
			// create float arrays mu, pMu with size equal to length of data instance minus class label
			float [] mu = new float[data[0].length-1];
			float [] pMu = new float[data[0].length-1];
			for (int j = 0; j < mu.length; j++)
			{
				mu[j] = (random.nextFloat()*range) + min; // initialize mus to random number in dataset range
				pMu[j] = 0; // initialize all mu's to all 0s;
			}
			cluster[i] = new Cluster(mu);
			prevCluster[i] = pMu;
		}
		
		// randomly assign instances to clusters
		for (int i = 0; i < data.length; i++)
		{
			int r = random.nextInt(K);
			cluster[r].add(i);
		}
		
		for (int i = 0; i < K; i++)
		{
			cluster[i].calcMu(data);
			System.out.println("Cluster" + i);
			cluster[i].printMu();
			cluster[i].printMembers();
			System.out.println();
		}
		
		int iteration = 0;	// variable to keep track of number of iterations
		
		// apply K-means until the centroids converge  
		while(iteration < 10)
		{
			System.out.println("Iteration" + iteration);
			
			// record the previous values of the centroids
			for (int i = 0; i < cluster.length; i++)
			{
				prevCluster[i] = cluster[i].getMu();
			}
			
			for (int i = 0; i < data.length; i++)
			{
				int clusterIndex;
				clusterIndex = cluster(cluster, data[i]);
				for (int j = 0; j < cluster.length; j++)
				{
					cluster[j].remove(i);
				}
				cluster[clusterIndex].add(i);
			}
			
			for (int i = 0; i < K; i++)
			{
				System.out.println("Cluster" + i);
				cluster[i].printMu();
				cluster[i].printMembers();
				System.out.println();
			}
			
			for (int i = 0; i < K; i++)
			{
				cluster[i].calcMu(data);
				System.out.println("Cluster" + i);
				cluster[i].printMu();
				cluster[i].printMembers();
				System.out.println();
			}
			iteration++;
		}
	}
	
	private int cluster(Cluster [] c, float d[])
	{
		float distance = Float.POSITIVE_INFINITY;
		int cluster = 0;
		
		for (int i = 0; i < K; i++)
		{
			float sum = 0;
			float [] mu = c[i].getMu();
			for (int j = 0; j < mu.length; j++)
			{
				sum = sum + Math.abs(d[j] - mu[j]);
			}
			sum = (float) Math.sqrt(sum*sum);
			
			if (sum < distance)
			{
				distance = sum;
				cluster = i;
			}
		}
		return cluster;
	}
	
	private boolean equals(float [][] pCluster, Cluster[] cCluster)
	{
		int count = 0;
		
		for (int i = 0; i < cCluster.length; i++)
		{
			if (Arrays.equals(pCluster[i], cCluster[i].getMu()))
			{
				count++;
			}
		}
		
		if (count == cCluster.length)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
