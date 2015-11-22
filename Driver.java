import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
		
		// create file writer for training result file  
		try {
			PrintWriter fileWriter = new PrintWriter(new FileOutputStream(result + ".train.txt"), true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// create file writer for testing result file
		try {
			PrintWriter fileWriterO = new PrintWriter(new FileOutputStream(result + ".test.txt"), true);
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
		KMeans kmeans = new KMeans(examples, clusters);
		kmeans.cluster();
		
		
		
		/* TESTING PHASE
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
