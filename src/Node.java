public class Node {
	
	private boolean visited = false;
	private boolean Noise = false;
	private float[] values;

	
	public Node(boolean visited, boolean Noise, float[] values){
		this.values = values;
		this.Noise = Noise;
		this.visited = visited;
	}
	
	public boolean checkVisited(){
		return visited;
	}
	
	public void markVisited(){
		visited = true;
	}
	
	public void markNoise(){
		Noise = true;
	}

	public float[] getValues(){
		return values;
	}
	
	public float getCenter(){
		float average = 0;
		for(int i = 0; i < values.length; i++){
			average += values[i];
		}
		average = average / values.length;
		return average;
	}
	public float stdDev(){
		double std = 0;
		float average = getCenter();
		for(int i = 0; i < values.length; i++){
			std += Math.pow((values[i] - average), 2);
		}
		std = std / values.length;
		std = Math.sqrt(std);
		float stdDev = (float) std;
		return stdDev;
	}
	
}