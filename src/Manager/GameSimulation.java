package Manager;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.JTable;

import GameObject.Constants;
import GameObject.GNode;
import GameObject.GObstacle;
import GameObject.GTile;
import GameObject.GameObject;

public class GameSimulation {
	
	EnvironmentManager eManager;
	
	private ArrayList<GameObject> nGObjects;
//	private ArrayList<FactoryWorker> mFWorkers;
	private GNode nGNodes[][];
//	private Map<String, FactoryNode> mFNodeMap;

	GameSimulation(EnvironmentManager eManager) {
		this.eManager = eManager;
		nGNodes = new GNode[Constants.mapGridWidth][Constants.mapGridHeight];
		
		//Create the nodes of the factory
		for(int height = 0; height < Constants.mapGridHeight; height++) {
			for(int width = 0; width < Constants.mapGridWidth; width++) {
				nGNodes[height][width] = new GNode(height,width);

				if(eManager.map_arr[height][width] instanceof GTile){
					nGNodes[height][width].setObject(eManager.map_arr[height][width]);
					}
			}
		}
		
		//Link all of the nodes together
		for(GNode[] nodes: nGNodes) {
			for(GNode node : nodes) {
				int col = node.getCol();
				int row = node.getRow();
				if(col != 0) node.addNeighbor(nGNodes[row][col-1]);
				if(col != Constants.mapGridWidth-1) node.addNeighbor(nGNodes[row][col+1]);
				if(row != 0) node.addNeighbor(nGNodes[row-1][col]);
				if(row != Constants.mapGridHeight-1) node.addNeighbor(nGNodes[row+1][col]);
			}	
		}
	}
	
	public GNode[][] getGNodes() {
		return nGNodes;
	}
//end of GameSimulation class	
}
