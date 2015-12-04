import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

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
		
		String fileName;
		System.out.println("Enter name of file to save DBScan results. (Extension will be added automatically");
		fileName = input.nextLine();
		
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
		PrintWriter fileWriter2 = null;
		// create file writer for result file  
		try {
			fileWriter = new PrintWriter(new FileOutputStream(result + ".txt"), true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			fileWriter2 = new PrintWriter(new FileOutputStream(fileName + ".txt"), true);
		}
		catch (FileNotFoundException e) {
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
		
		float radius;
		System.out.println("What do you want to set as the radius for dbscan?");
		radius = input.nextFloat();
		
		int eps;
		System.out.println("What are your minimum number of points for dbscan?");
		eps = input.nextInt();		
		
		ArrayList<Node> points = new ArrayList();
		
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
			for(int k = 0; k < features; k++){
				Node temp = new Node(false, false, examples[i]);
				points.add(temp);
			}
		}
		
		DBScan scan = new DBScan(points, radius, eps);
		scan.clusterize();
		ArrayList<ArrayList<Node>> cluster = scan.getCluster();

		KMeans kmeans = new KMeans(examples, clusters);
		Cluster [] clustered = kmeans.cluster();
		
		/* EVALUATION PHASE
		 * 
		 */
		ClusterEvaluation clusterEval = new ClusterEvaluation(); 
		fileWriter.println("Results");
		fileWriter.println("Iterations");
		fileWriter.println(kmeans.getIterations());
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
		System.out.println("--------");
		ArrayList<Node> temp;
		ArrayList<Node> temp0;
		float[] temp1;
		float[] temp2;
		float temp3;
		float temp4;
		System.out.println(cluster.size());
		
		for(int s = 0; s < cluster.size(); s++){
			temp = cluster.get(s);
			fileWriter2.print("cluster" + s + " ");
			for(int t = 0; t < cluster.size(); t++){
				temp0 = cluster.get(t);
				temp1 = temp.get(t).getValues();
				temp2 = temp0.get(s).getValues();
				temp3 = temp.get(t).stdDev();
				temp4 = temp0.get(s).stdDev();
				if(t != s){
					fileWriter2.print(Math.abs(clusterEval.clusterSeparation(temp1,temp2,temp3, temp4)) + " ");
				}
				
			}
			fileWriter2.print("cohesion:" + clusterEval.cohesion(temp.size(), radius));
			fileWriter2.println();
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
