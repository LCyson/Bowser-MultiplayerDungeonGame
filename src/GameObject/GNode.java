package GameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GNode extends GameObject {

	//Each node can own a GameObject, doing so will render it in its place
	private GameObject nGObject;
	
	//This lock is used to lock the node or the object within the node
	private Lock nodeLock;
	
	//Used for path finding
	private ArrayList<GNode> nNeighbors;
	
	final int col;
	final int row;
	
	private int path;
	
	private static final Color[] pathColors = {
		new Color(0,0,255),
		new Color(0,128,255),
		new Color(0,255,255),
		new Color(0,255,128),
		new Color(0,255,0),
		new Color(128,255,0),
		new Color(255,255,0),
		new Color(255,128,0),
		new Color(255,0,0)
	};
	
	//instance constructor
	{
		nGObject = null;
		nodeLock = new ReentrantLock();
		nNeighbors = new ArrayList<GNode>();
		path = 0;
	}
	
	public GNode(int col, int row) {
		super(); //each node is 1 tile
		this.col = col;
		this.row = row;
	}

	public void setObject(GameObject inGObject) {
		nGObject = inGObject;
	}
	
	public GameObject getObject() {
		return nGObject;
	}

	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	public void addNeighbor(GNode neighbor) {
			nNeighbors.add(neighbor);
	}
	
	public ArrayList<GNode> getNeighbors() {
		return nNeighbors;
	}
	
	public boolean aquireNode() {
		return nodeLock.tryLock();
	}
	public void releaseNode() {
		nodeLock.unlock();
	}
	
	public void mark() {
		path++;
	}
	
	public void unMark() {
		path--;
	}

	//Below this line is the methods for path finding
	
	//We need a wrapper class for path finding
	//Otherwise, two threads couldn't path find at a time
	class PathNode {
		public GNode fNode; //the actual node
		public int gScore; //cost from the start of the best known path
		public int hScore; //Manhattan distance to the end
		public int fScore; //g+h
		public PathNode parent;
	}
	private int heuristicCostEstimate(GNode gameNode) {
		//Manhattan distance between the nodes
		//This method assumes we are path finding TO "this"
		return ((int)Math.abs(this.col - gameNode.col) + (int)Math.abs(this.row - gameNode.row));
	}
	private PathNode lowestFScore(ArrayList<PathNode> openList) {
		PathNode toReturn = null;
		int lowest = Integer.MAX_VALUE;
		for(PathNode pn : openList) {
			if(pn.fScore < lowest) {
				toReturn = pn;
				lowest = pn.fScore;
			}
		}
		return toReturn;
	}
	private Stack<GNode> makePath(PathNode start, PathNode end) {
		Stack<GNode> shortestPath = new Stack<GNode>();
		PathNode current = end;
		shortestPath.add(end.fNode);
		while(current.fNode != start.fNode) {
			shortestPath.add(current.parent.fNode);
			current = current.parent;
		}
		return shortestPath;
	}
	private PathNode containsNode(ArrayList<PathNode> list, GNode node) {
		for(PathNode pn : list) {
			if(pn.fNode == node) return pn;
		}
		return null;
	}
	
	//A* path finding
	public Stack<GNode> findShortestPath(GNode mDestinationNode) {
		ArrayList<PathNode> openList = new ArrayList<PathNode>();
		ArrayList<PathNode> closedList = new ArrayList<PathNode>();
		
		PathNode start = new PathNode();
		start.fNode = this;
		start.gScore = 0;
		start.hScore = heuristicCostEstimate(start.fNode);
		start.fScore = start.gScore + start.hScore;
		openList.add(start);		
		
		while(!openList.isEmpty()) 
		{
			PathNode current = lowestFScore(openList);
			if(current.fNode == mDestinationNode) { 
				Stack<GNode> path = makePath(start, current);
				Iterator<GNode> iter = path.iterator();
				while(iter.hasNext()) {
					iter.next().mark();
				}
				for (GNode p: path) {
					System.out.println(p.getRow() + " " + p.getCol() );
				}
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for(GNode neighbor : current.fNode.nNeighbors) {
				if(neighbor.getObject() instanceof GObstacle) {
					 continue;
				}
				if(containsNode(closedList,neighbor) != null) continue;
				int temp_gScore = current.gScore + 1;//nodes always have distance 1 in our case
				PathNode neighborPathNode = containsNode(openList,neighbor);
				if(neighborPathNode == null || (temp_gScore < neighborPathNode.gScore)) {
					if(neighborPathNode == null) neighborPathNode = new PathNode();
					neighborPathNode.fNode = neighbor;
					neighborPathNode.parent = current;
					neighborPathNode.gScore = temp_gScore;
					neighborPathNode.hScore = heuristicCostEstimate(neighbor);
					neighborPathNode.fScore = neighborPathNode.gScore + neighborPathNode.hScore;
					if(containsNode(openList,neighbor) == null) {
						openList.add(neighborPathNode);
					}
				}
			}
		}
		return new Stack<GNode>();//no path exists
	}
	
}
