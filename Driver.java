import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Driver 
{

	public static void main(String[] args) 
	{
		String line = null;
		String dataset;
		String result;
		
		// create Scanner instance to receive info from user 
		Scanner input = new Scanner(System.in);
		
		// prompt user for name of data file
		System.out.println("Enter name of dataset file with extension");
		dataset = input.nextLine();
		
		// prompt user for name of file to save results in
		System.out.println("Enter name of file to save results. (Extension will be added automatically)");
		result = input.nextLine();
		
		System.out.println("Enter delimiter used by dataset");
		String delim = input.nextLine();
		
		// create file reader for dataset file and wrap it in a buffer
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(dataset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		PrintWriter fileWriter = null;
		// create file writer for result file  
		try {
			fileWriter = new PrintWriter(new FileOutputStream(result + ".txt"), true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/* CLUSTERING PHASE
		 * 
		 */
		// prompt for number of examples for training 
		int example;
		System.out.println("How many examples for clustering?");
		example = input.nextInt();
		
		int features;
		System.out.println("How many features?");
		features = input.nextInt();
		
		int clusters;
		System.out.println("How many clusters?");
		clusters = input.nextInt();
		
		int particles;
		System.out.println("How many particles?");
		particles = input.nextInt();
				
		float [][] examples = new float[example][features];
		for(int i = 0; i < example; i++)
		{
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] data = line.split(delim);
			for (int j = 0; j < features; j++)
			{
				examples[i][j] = Float.parseFloat(data[j]);
			}
		}
		//KMeans kmeans = new KMeans(examples, clusters);
		//Cluster [] clustered = kmeans.cluster();
		
		PSO pso = new PSO(examples, clusters, particles);
		Cluster [] clustered = pso.cluster();
		
		/* EVALUATION PHASE
		 * 
		 */
		ClusterEvaluation clusterEval = new ClusterEvaluation(); 
		fileWriter.println("Results");
		fileWriter.println("Particles");
		fileWriter.println(particles);
		fileWriter.println("Iterations");
		fileWriter.println(pso.getIterations());
		fileWriter.print("Cluster, NumMembers, ");
		for (int i = 0; i < clustered.length-1; i++)
			fileWriter.print("S" + i + ", ");
		fileWriter.print("Cohesion");
		fileWriter.println();
		for (int i = 0; i < clustered.length; i++)
		{
			fileWriter.print(i + " ");
			fileWriter.print(clustered[i].getNumMembers() + " ");
			for (int j = 0; j < clustered.length; j++)
			{
				if (i != j)
				{
					fileWriter.print(clusterEval.clusterSeparation(clustered[i].getMu(), clustered[j].getMu(), 
							clustered[i].getStd(), clustered[j].getStd()) + " ");
				}
			}
			fileWriter.print(clusterEval.cohesion(clustered[i].getNumMembers(), clustered[i].getStd()));
			fileWriter.println();
		}
		
		/* CLOSE file reader and writers
		 * 
		 */
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
