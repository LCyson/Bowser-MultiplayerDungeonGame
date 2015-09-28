package GameObject;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import Manager.EnvironmentManager;

public class GEnemy extends GameObject implements Runnable
{
	private int playerDist, HP;
	private EnvironmentManager em;
	private GPlayer player;
	
	Queue<GameObject> path;
	GNode nextNode;
	Point nextGrid;
	GameObject nextTile;
	
	private boolean isChasing = false;
	private int roamCounter, chaseCounter;
	
	//given target's position(Point), the enemy will find the shortest path to chase the player
	public GEnemy(String name, Point p, EnvironmentManager em) 
	{
		super(name);
		position = p;
		HP = Constants.enemyHP;
		this.em = em;
		Random rand = new Random();
		roamCounter = rand.nextInt(Constants.enemyRoamTime * Constants.enemyFPS);
		chaseCounter = rand.nextInt(Constants.enemyChaseTime * Constants.enemyFPS);
	}
	
	public void activated(GPlayer player){
		this.player = player;
		path = new LinkedList<GameObject>();
		calculatePath(); nextTile = path.poll();
		Thread t = new Thread(this);
		t.start();
	}	

	public void run() {
		while(!isDead){
			if(isChasing){
				chasePlayerAI();
				chaseCounter--;
				if(chaseCounter == 0){
					chaseCounter = Constants.enemyChaseTime * Constants.enemyFPS;
					isChasing = false;
				}
			}
			else{
				roam();
				roamCounter--;
				if(roamCounter == 0){
					roamCounter = Constants.enemyRoamTime * Constants.enemyFPS;
					isChasing = true;
				}
			}
			attackPlayer();
			try {
				Thread.sleep(1000/Constants.enemyFPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//randomly roam, when not so close
	public void roam(){
		boolean arrived = moveTowards(nextTile.position, Constants.enemySpeed);
		if(arrived){
			nextTile = em.girdIndexToTile(findRandomNearByGrid(this.getCurrentGrid()));
		}
	}
	
	//enemy explodes
	public void attackPlayer() {
		//animation here
		if(em.isIntersectWithPlayer(this.position, Constants.imageDimension)){
			em.enemyHitPlayer(this);
		}
	}
	
	//change enemy's HP
	public void changeHP(int damage) {
		HP -= damage;
		if(HP <= 0)
			isDead = true;
	}
	
	//get hit by 1 bullet
	public void getHit(){
		HP -= Constants.bulletDamage;
		CheckDeath();
	}
	
	//check Death
	public boolean CheckDeath() {
		if (HP <= 0) {
			System.out.println(gName+" is Dead!");
			isDead = true;
			return true;
		}
		return false;
	}
	
	//should be called per update frame
		//make object to move toward given render position at given speed
		//return true if arrived
		
		public void chasePlayerAI(){
			boolean arrived = moveTowards(nextTile.position, Constants.enemySpeed);
			if(arrived){
				calculatePath();
				if(!path.isEmpty())
					nextTile = path.poll();
			}
		}
		public void calculatePath(){
			path.clear();
			Point destinationGrid = player.getCurrentGrid();
			Point currentGrid = this.getCurrentGrid();
			if(currentGrid.equals(destinationGrid)) return;
			calculatePathHelper(currentGrid, destinationGrid);
		}
		
		public void calculatePathHelper(Point currentGrid, Point destinationGrid){
			Point nextGrid = findNextCloserGrid(currentGrid, destinationGrid);
			if(nextGrid == null) return;
			if(nextGrid.equals(destinationGrid)){
				path.add(em.girdIndexToTile(nextGrid));
				return;}
			
			else{
				path.add(em.girdIndexToTile(nextGrid));
				calculatePathHelper(nextGrid, destinationGrid);
			}
		}
		
		public Point findNextCloserGrid(Point currentGrid, Point destinationGrid){
			
			int up, down, right, left;
			up = down = right = left = Integer.MAX_VALUE;
			Point upGrid, downGrid, rightGrid, leftGrid;
			upGrid = downGrid = rightGrid = leftGrid = new Point(1,1);
			if(currentGrid.y > 1){
				upGrid = new Point(currentGrid.x, currentGrid.y-1);
				if(em.girdIndexToTile(upGrid) instanceof GTile){
					up = getGridDistance(upGrid, destinationGrid);
				}
			}
			if(currentGrid.y < (Constants.mapGridHeight)){
				downGrid = new Point(currentGrid.x, currentGrid.y+1);
				if(em.girdIndexToTile(downGrid) instanceof GTile){
					down = getGridDistance(downGrid, destinationGrid);
				}
			}
			if(currentGrid.x > 1){
				leftGrid = new Point(currentGrid.x-1, currentGrid.y);
				if(em.girdIndexToTile(leftGrid) instanceof GTile){
					left = getGridDistance(leftGrid, destinationGrid);
				}
			}
			if(currentGrid.x < (Constants.mapGridWidth))
			{
				rightGrid = new Point(currentGrid.x+1, currentGrid.y);
				if(em.girdIndexToTile(rightGrid) instanceof GTile){
					right = getGridDistance(rightGrid, destinationGrid);
				}
			}
			int min = minOfFour(up, down, right, left);
			
			if(up == min && !path.contains(em.girdIndexToTile(upGrid)) ){
				return upGrid;
			}
			else if(down == min & !path.contains(em.girdIndexToTile(downGrid)))
				return downGrid;
			else if(left == min & !path.contains(em.girdIndexToTile(leftGrid)))
				return leftGrid;
			else if(right == min & !path.contains(em.girdIndexToTile(rightGrid)))
				return rightGrid;
			else return null;
		}
		public int getGridDistance(Point currentGrid, Point destinationGrid){
			int yGridDistance = destinationGrid.y - currentGrid.y;
			int xGridDistance = destinationGrid.x - currentGrid.x;
			int total = Math.abs(xGridDistance) + Math.abs(yGridDistance);
			return total;
 		}
		public Point findRandomNearByGrid(Point currentGrid){
			int up, down, right, left;
			up = down = right = left = -1;
			Point upGrid, downGrid, rightGrid, leftGrid;
			upGrid = downGrid = rightGrid = leftGrid = new Point(1,1);
			if(currentGrid.y > 1){
				upGrid = new Point(currentGrid.x, currentGrid.y-1);
				if(em.girdIndexToTile(upGrid) instanceof GTile){
					up = 1;
				}
			}
			if(currentGrid.y < (Constants.mapGridHeight)){
				downGrid = new Point(currentGrid.x, currentGrid.y+1);
				if(em.girdIndexToTile(downGrid) instanceof GTile){
					down = 1;
				}
			}
			if(currentGrid.x > 1){
				leftGrid = new Point(currentGrid.x-1, currentGrid.y);
				if(em.girdIndexToTile(leftGrid) instanceof GTile){
					left = 1;
				}
			}
			if(currentGrid.x < (Constants.mapGridWidth))
			{
				rightGrid = new Point(currentGrid.x+1, currentGrid.y);
				if(em.girdIndexToTile(rightGrid) instanceof GTile){
					right = 1;
				}
			}
			Random rand = new Random();
			int roll;
			boolean decided = false;

				roll = rand.nextInt(4)+1;
				if(roll == 1 && up == 1)
					return upGrid;
				if(roll == 2 && down == 1)
					return downGrid;
				if(roll == 3 && left == 1)
					return leftGrid;
				if(roll == 4 && right == 1)
					return rightGrid;
				else
					return currentGrid;
		}
		
		public int minOfFour(int a, int b, int c, int d){
			int min = a;
			if(b<min)
				min = b;
			if(c<min)
				min = c;
			if(d<min)
				min = d;
			return min;
		}
}
