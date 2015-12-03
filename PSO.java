import java.util.Random;

public class PSO 
{
	private float [][] data;	// 2D array of floats to store the data to cluster
	private int K;	// integer to store number of clusters 
	private int N;  // integer to store number of particles
	private int iterations;	// integer to record number of iterations till convergence
	
	public PSO(float [][] d, int k, int n)
	{
		data = d;
		K = k;
		N = n;
	}
	
	public Cluster[] cluster()
	{
		Random random = new Random();
		Cluster [][] swarm = new Cluster[N][K];	// 2D array to store particles of clusters 
		Cluster [][] pbest = new Cluster[N][K];	// 2D array to store personal best of each particle in swarm
		Cluster [] gbest = new Cluster[K];		// array to store global best of swarm
		
		float min = Float.POSITIVE_INFINITY;
		float max = 0, range = 0;
		
		int muDimension = data[0].length-1;
		
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
		
		int fittest = 0;  // variable to store index of best initial particle 	
		float fitness = Float.POSITIVE_INFINITY;
		for (int h = 0; h < N; h++)
		{
			System.out.println("Particle" + h);
			// randomly initialize the mu's to a range within the datasets range
			for (int i = 0; i < K; i++)
			{
				// create float arrays mu, pMu with size equal to length of data instance minus class label
				float [] mu = new float[muDimension];
				float [] pMu = new float[muDimension];
				for (int j = 0; j < mu.length; j++)
				{
					mu[j] = (random.nextFloat()*range) + min; // initialize mus to random number in dataset range
					pMu[j] = 0; // initialize all mu's to all 0s;
				}
				swarm[h][i] = new Cluster(mu);
				pbest[h][i] = new Cluster(mu);
				//prevCluster[i] = pMu;
			}
			
			// randomly assign instances to clusters
			for (int i = 0; i < data.length; i++)
			{
				int r = random.nextInt(K);
				swarm[h][r].add(i);
				pbest[h][r].add(i);
			}
			
			for (int i = 0; i < K; i++)
			{
				System.out.println("Swarm");
				swarm[h][i].calcMu(data);
				System.out.println("Cluster" + i);
				swarm[h][i].printMu();
				swarm[h][i].printMembers();
				
				System.out.println("Pbest");
				pbest[h][i].calcMu(data);
				System.out.println("Cluster" + i);
				pbest[h][i].printMu();
				pbest[h][i].printMembers();
				System.out.println();
			}
			
			
			float local_fitness = fitness(swarm[h]);
			if (local_fitness < fitness)
			{
				fitness = local_fitness;
				fittest = h;
			}
		}
		
		for (int i = 0; i < K; i++)
		{
			float mu[] = swarm[fittest][i].getMu();
			gbest[i] = new Cluster(mu);
			
			int members[] = swarm[fittest][i].getMembers();
			for (int j = 0; j < members.length; j++)
			{
				gbest[i].add(members[j]);
			}
			System.out.println("Gbest");
			gbest[i].printMu();
			gbest[i].printMembers();
		}
		
		
		int iteration = 0;					   // variable to keep track of number of iterations
		
		// Velocity parameters
		float prevVelocity[][][] = new float[N][K][muDimension];   // variable to keep track of each particle's previous velocity
		float curVelocity[][][] = new float[N][K][muDimension];    // variable to keep track of each particle's current velocity
		float omega = 0.9f;				       // variable to store the inertia
		float phi1 = 1;						   // variable to store strength of social component
		float phi2 = 2;						   // variable to store strength of cognitive component
		
		while(iteration < 1000)
		{
			prevVelocity = curVelocity;
			float gBestFit = fitness(gbest);	      // global best fitness
			for(int i = 0; i < N; i++)
			{
				float pFit = fitness(swarm[i]);	      // current fitness of current particle  
				float pBestFit = fitness(pbest[i]);	  // personal best fitness of current particle
					  
				if (pFit < pBestFit)
				{
					for (int j = 0; j < K; j++)
					{
						float mu[] = swarm[i][j].getMu();
						pbest[i][j].setMu(mu);
						pbest[i][j].clearMembers();
						
						int members[] = swarm[i][j].getMembers();
						for (int k = 0; k < members.length; k++)
						{
							pbest[i][j].add(members[k]);
						}
					}
				}
				
				if (pFit < gBestFit)
				{
					for (int j = 0; j < K; j++)
					{
						float mu[] = swarm[i][j].getMu();
						gbest[j].setMu(mu);
						gbest[j].clearMembers();
						
						int members[] = swarm[i][j].getMembers();
						for (int k = 0; k < members.length; k++)
						{
							gbest[j].add(members[k]);
						}
					}
				}
				
				for (int j = 0; j < K; j++)
				{
					float gmu[] = gbest[j].getMu();
					float smu[] = swarm[i][j].getMu();
					float pmu[] = pbest[i][j].getMu();
					float nmu[] = new float[muDimension];
					for (int k = 0; k < muDimension-1; k++)
					{
						curVelocity[i][j][k] = omega*prevVelocity[i][j][k] + (random.nextFloat()*phi1)*(gmu[k] - smu[k])
								+ (random.nextFloat()*phi2)*(pmu[k] - smu[k]);

						nmu[k] = smu[k] + curVelocity[i][j][k];
					}
					swarm[i][j].setMu(nmu);
					swarm[i][j].clearMembers();
					
				}
				
				for (int j = 0; j < data.length; j++)
				{
					int clusterIndex = cluster(swarm[i], data[j]);
					swarm[i][clusterIndex].add(j);
				}
			}
			iteration++;
		}
		iterations = iteration;
		calcStd(gbest);
		return gbest;
	}
	
	// method to calculate fitness function according to minimization of the quantization error
	private float fitness(Cluster [] c)
	{
		float fitness = 0;
		for (int i = 0; i < c.length; i++)
		{
			int members[] = c[i].getMembers();
			float mu[] = c[i].getMu();
			float sum = 0;
			for (int j = 0; j < members.length; j++)
			{
				float sumMem = 0;
				for (int k = 0; k < mu.length; k++)
				{
					sumMem = sumMem + Math.abs(data[j][k] - mu[k]);
				}
				sum = sum + sumMem;
			}
			sum = sum/c[i].getNumMembers();
			fitness = fitness + sum;
		}
		
		return fitness/c.length;
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
	
	/*
	 *  method which calculates the standard deviation of the cluster objects and sets 
	 *  the value as part of the corresponding cluster 
	 *  Cluster [] c - array of cluster objects each containing a single cluster
	 */
	private void calcStd(Cluster [] c)
	{
		for (int i = 0; i < c.length; i++)
		{
			float deviation = 0;
			int members[] = c[i].getMembers();
			float mu[] = c[i].getMu();
			
			for (int j = 0; j < members.length; j++)
			{
				float mSum = 0;
				for (int k = 0; k < mu.length; k++)
				{
					mSum = (float) (mSum + Math.pow((data[j][k] - mu[k]), 2));
				}
				deviation = (float) (deviation + Math.sqrt(mSum));
			}
			deviation = (float) Math.sqrt(deviation / members.length);
			c[i].setStd(deviation);
		}
	}
	
	public int getIterations()
	{
		return iterations;
	}
}
