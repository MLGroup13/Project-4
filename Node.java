public class Node {
	
	private boolean visited = false;
	private boolean Noise = false;
	private int X;
	private int Y;

	
	public Node(boolean visited, boolean Noise, int X, int Y){
		this.X = X;
		this.Y = Y;
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
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
	}
	
}