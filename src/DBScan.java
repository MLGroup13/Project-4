import java.util.ArrayList;
import java.lang.Math;
public class DBScan {
	
	private ArrayList<Node> SetOfPoints;
	private float eps;
	private int MinPts;
	
	public DBScan(ArrayList<Node> SetOfPoints, float eps, int MinPts)
	{
		this.SetOfPoints = SetOfPoints;
		this.eps = eps;
		this.MinPts = MinPts;
	}
	
	private ArrayList<ArrayList<Node>> Cluster = new ArrayList();
	
	public void clusterize(){
		System.out.println(SetOfPoints.size());
		for(int i = 0; i < SetOfPoints.size(); i++){
			Node P = SetOfPoints.get(i);
			if(P.checkVisited() == true){
				continue;
			}
			if(P.checkVisited() == false){
				P.markVisited();
			}
			ArrayList<Node> NeighborPts = null;
			NeighborPts = RegionQuery(P, SetOfPoints, eps);
			if (NeighborPts.size() < MinPts){
				P.markNoise();
			}
			else{
				//C.add(e); // C = next cluster
				Cluster.add(NeighborPts);
				ExpandCluster(SetOfPoints, P, NeighborPts, Cluster, eps, MinPts);
			}
		}
	}	
	
	public void ExpandCluster(ArrayList<Node> points, Node p, ArrayList<Node> neighborhood, ArrayList<ArrayList<Node>> Cluster, float eps, int MinPts){
		ArrayList<Node> Cprime = new ArrayList();
		Cprime.add(p);
		ArrayList<Node> ClusterSupreme = new ArrayList();
		ClusterSupreme.addAll(Cprime);
		ClusterSupreme.addAll(neighborhood);
		for(int i = 0; i < neighborhood.size(); i++){
		Node Pprime = neighborhood.get(i);
		if(Pprime.checkVisited() == false){
			Pprime.markVisited();
			ArrayList<Node> NeighborPtsPrime = null;
			NeighborPtsPrime = RegionQuery(Pprime, points, eps);
			if (NeighborPtsPrime.size() >= MinPts){
				neighborhood.addAll(NeighborPtsPrime);
			}	
		}
		if (checkCluster(Pprime, ClusterSupreme)){
			Cprime.add(Pprime);
		}
		}
	}
	
	public ArrayList<Node> RegionQuery(Node P, ArrayList<Node> points, float eps){
		ArrayList<Node> neighbors = new ArrayList<>();
		for(int i = 0; i < points.size(); i++){
			Node temp = points.get(i);
			double distance = 0;
			for(int j = 0; j < P.getValues().length; j++){
				distance += Math.pow((P.getValues()[j] - temp.getValues()[j]), 2);
			}
			if( distance < Math.pow(eps, 2)){
				neighbors.add(temp);
			}
		}
		return neighbors;
	}
	
	public boolean checkCluster(Node p, ArrayList<Node> d){
		for(int i = 0; i < d.size(); i++){
			if(d.get(i) == p){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<ArrayList<Node>> getCluster(){
		return Cluster;
	}
}
