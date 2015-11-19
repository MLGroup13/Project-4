import java.util.ArrayList;
import java.lang.Math;
public class DBScan {

	
	public void DBSCAN(ArrayList<Node> SetOfPoints, int eps, int MinPts){
		ArrayList<Node> C = new ArrayList();
		for(int i = 0; i < SetOfPoints.size(); i++){
			Node P = SetOfPoints.get(i);
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
				C.addAll(new ArrayList<Node>());
				ExpandCluster(SetOfPoints, P, NeighborPts, C, eps, MinPts);
			}
		}
	}	
	
	public void ExpandCluster(ArrayList<Node> points, Node p, ArrayList<Node> neighborhood, ArrayList<Node> C, int eps, int MinPts){
		ArrayList<Node> Cprime = null;
		Cprime.add(p);
		ArrayList<Node> ClusterSupreme = null;
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
	
	public ArrayList<Node> RegionQuery(Node P, ArrayList<Node> points, int eps){
		int x2 = P.getX();
		int y2 = P.getY();
		ArrayList<Node> neighbors = new ArrayList<>();
		for(int i = 0; i < points.size(); i++){
			Node temp = points.get(i);
			int x1 = temp.getX();
			int y1 = temp.getY();
			
			if( (Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) < Math.pow(eps, 2)){
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

}
