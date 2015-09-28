package Manager;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import GameObject.Constants;
import GameObject.GBullet;
import GameObject.GEnemy;
import GameObject.GNode;
import GameObject.GObstacle;
import GameObject.GPlayer;
import GameObject.GTile;
import GameObject.GameObject;

public class EnvironmentManager 
{
	private int chance_Tile = 90, chance_Obstacle = 10;
	private int num_Enemy_Begin = 5;
	public  int num_Enemy;
	private int num_Enemy_Range = 3;
	private int level = 1;
	
	//Map elements container
	public GameObject[][] map_arr;
	public Vector<GObstacle> obstacle_vec;
	public Vector<GBullet> bullet_vec;
	public Vector<GEnemy> enemy_vec;
	private int bullet_id = 0;
	
	GPlayer player;
	private GameManager gm;
	private GameGUI gGUI;
	private GameSimulation pathSimulator;
	private GNode[][] pathNodes;
	
	public Lock bullet_lock = new ReentrantLock();
	public Lock enemy_lock = new ReentrantLock();
	
	public EnvironmentManager(GPlayer player, GameGUI gGUI){
		this.player = player;
		this.gGUI = gGUI;
		bullet_vec = new Vector<GBullet>();
		obstacle_vec = new Vector<GObstacle>();
		enemy_vec = new Vector<GEnemy>();
		
	}
 
	public void generatePathSimulator(){
		pathSimulator = new GameSimulation(this);
		pathNodes = pathSimulator.getGNodes();
	}
	
	//trigger enemy to catch player
	public boolean detectPlayer(Point position) {
		if ( Math.abs(player.getPosition().x - position.x) < 200 && Math.abs(player.getPosition().y - position.y) < 200) {
			return true;
		}
		return false;
	}
	
	//first to call, generate map randomly
	public void generateMap()
	{
		map_arr = new GameObject[Constants.mapGridHeight][Constants.mapGridWidth];
		
		for (int h = 0; h < Constants.mapGridHeight; ++h){	
			for (int w = 0; w < Constants.mapGridWidth; ++w){
				Constants.mapElement thisGrid = rollGridType();
				if(thisGrid == Constants.mapElement.obstacle){
					GObstacle obs = new GObstacle(h+"-"+w, GridToRenderPosition(new Point(w,h)));
					map_arr[h][w] = obs;
					obstacle_vec.add(obs);
				}
				else if (thisGrid == Constants.mapElement.tile){
					map_arr[h][w] = new GTile(h+"-"+w,GridToRenderPosition(new Point(w,h)));
				}
			}
		}
		generatePathSimulator();
	}
	
	//second to call. Only after the map is generated
	public void generateEnemy(){
		Random rand = new Random();
		num_Enemy = rand.nextInt(num_Enemy_Range) + 1 + num_Enemy_Begin + level;
		for(int i = 0; i < num_Enemy; ++i){
			Point initPosition;
			do{
				initPosition = GridToRenderPosition(rollGridPosition());
			}while(isPositionOverlapWithObstacle(initPosition));
			GEnemy enemy = new GEnemy("" + i, initPosition, this);
			enemy.activated(player);
			enemy_vec.add(enemy);
		}
	}
	
	/*
	 * use at the beginning of initializing map
	 * to check if player is standing on an obstacle
	 * change the player position if so
	 */
	public void modifyPlayerPosition(){
		while(isPositionOverlapWithObstacle(player.getPosition()) | isPositionOverlapWithEnemy(player.getPosition())){
			player.changePosition(GridToRenderPosition(rollGridPosition()));
		}
	}
	
	public void registerBullet(GBullet bullet)
	{
		bullet_lock.lock();
		bullet.setName("" + bullet_id);
		bullet_id ++;
		bullet_vec.add(bullet);
		gGUI.registerBulletLabel(bullet);
		bullet_lock.unlock();
	}
	public void deregisterBullet(GBullet bullet)
	{
		bullet_lock.lock();
		bullet.isDead = true;
		bullet_vec.remove(bullet);
		bullet_lock.unlock();
	}
	public void deregisterEnemy(GEnemy enemy){
		enemy_lock.lock();
		enemy_vec.remove(enemy);
		enemy_lock.unlock();
	}
	public void respawnEnemy(){
		Point initPosition;
		do{
			initPosition = GridToRenderPosition(rollGridPosition());
		}while(isPositionOverlapWithObstacle(initPosition));
		GEnemy enemy = new GEnemy("" + enemy_vec.size(), initPosition, this);
		enemy.activated(player);
		enemy_vec.add(enemy);
	}
	
	//randomly generate next tile to be tile or obstacle
	public Constants.mapElement rollGridType(){
		Random rand = new Random();
		int result = rand.nextInt(100) + 1;
		if(0 < result && result <= chance_Obstacle)
			return Constants.mapElement.obstacle;
		if(chance_Obstacle < result && result <= chance_Obstacle + chance_Tile)
			return Constants.mapElement.tile;
		else return Constants.mapElement.tile;
	}
	
	//get GEnemy Object
	public Vector<GEnemy> getEnemy() {
		return enemy_vec;
	}
	
	//randomly generate a initial grid position (to place an enemy)
	public static Point rollGridPosition(){
		Random rand = new Random();
		int x = rand.nextInt(Constants.mapGridWidth);
		int y = rand.nextInt(Constants.mapGridHeight);
		Point result = new Point(x,y);
		return result;
	}
	
	public static Point GridToRenderPosition(Point p){
		Point ret = p;
		ret.x *= Constants.imageDimension.width;
		ret.y *= Constants.imageDimension.height;
		return ret;
	}
	
	//take in render position point as parameter
	public boolean isPositionOverlapWithObstacle(Point p)
	{
		for(int i = 0; i < obstacle_vec.size(); ++i){
			if(p.equals(obstacle_vec.elementAt(i).getPosition())){
				return true;
			}
		}
		return false;
	}
	
	//take in render position point as parameter
	public boolean isPositionOverlapWithEnemy(Point p)
	{
		for(int i =0; i < num_Enemy; ++i){
			if(p.equals(enemy_vec.elementAt(i).getPosition()))
				return true;
		}
		return false;
	}
	
	//take in render position point of an enemy or player instance as parameter
	//calculate if it is overlap with any obstacle
	public boolean isObjectIntersectWithObstacle(Point p)
	{
		Rectangle r = new Rectangle(p, Constants.imageDimension);
		for(int i = 0; i < obstacle_vec.size(); ++i){
			Rectangle obs_r = new Rectangle(obstacle_vec.elementAt(i).getPosition(), Constants.imageDimension);
			if(r.intersects(obs_r))
				return true;
		}
		return false;
	}
	
	/*
	 * check whether bullet is out of map
	 * take render position p and object render dimension d as parameter
	 */
		public boolean isOutOfMap(Point p, Dimension d)
	{
		int upperBound = 0; int lowerBound = (Constants.mapGridHeight)*Constants.imageDimension.height;
		int leftBound = 0; int rightBound = (Constants.mapGridWidth)*Constants.imageDimension.width;
		if(p.x >= leftBound && p.x+d.width <= rightBound){
			if(p.y >= upperBound && p.y + d.height <= lowerBound){
				return false;
			}
			else return true;
		}
		return true;
	}
		
	public boolean isIntersectWithPlayer(Point p, Dimension d){
		Rectangle r = new Rectangle(p, d);
		Rectangle player_r = new Rectangle(player.getPosition(), Constants.imageDimension);
		if(r.intersects(player_r))
			return true;
		return false;
	}	
	
	public boolean isIntersectWithEnemy(Point p, Dimension d){
		Rectangle r = new Rectangle(p, d);
		for(int i = 0; i < enemy_vec.size(); i++){
			GEnemy enemy = enemy_vec.elementAt(i);
			Rectangle ene_r = new Rectangle(enemy.getPosition(), Constants.imageDimension);
			if(r.intersects(ene_r)){
				return true;
			}
		}
		return false;
	}
	
	//check whether bullet hits enemy or obstacle, return true if it does
	public boolean checkBulletCollide(GBullet bullet){
		Rectangle r = new Rectangle(bullet.getPosition(), Constants.bulletDimention);
		//see if hit obstacle
		for(int i = 0; i < obstacle_vec.size(); ++i){
			Rectangle obs_r = new Rectangle(obstacle_vec.elementAt(i).getPosition(), Constants.imageDimension);
			if(r.intersects(obs_r)){
				deregisterBullet(bullet);
				return true;
			}
		}
		//see if hit enemy
		for(int i = 0; i < enemy_vec.size(); i++){
			GEnemy enemy = enemy_vec.elementAt(i);
			Rectangle ene_r = new Rectangle(enemy.getPosition(), Constants.imageDimension);
			if(r.intersects(ene_r)){
				bulletHitEnemy(enemy);
				deregisterBullet(bullet);
				return true;
			}
		}
		//see if bullet out of map
		if(isOutOfMap(bullet.getPosition(), bullet.getDimension())){
			deregisterBullet(bullet);
			return true;
		}
		return false;
	}
	
	//Do things when an enemy is hit by a bullet
	public void bulletHitEnemy(GEnemy enemy){
		enemy.getHit();
		if(enemy.isDead){
			deregisterEnemy(enemy);
			player.score += Constants.killEnemyScore;
		}
	}
	
	public void enemyHitPlayer(GEnemy enemy){
		player.getHit(Constants.enemyDamage);
		
	}
	
	//clean up the map, erase all elements
	public void clearMap(){
		for (int h = 0; h < Constants.mapGridHeight; ++h){	
			for (int w = 0; w < Constants.mapGridWidth; ++w){
				map_arr[h][w] = null;
			}
		}
	}
	
	//set up level index
	public void setLevel(int levelIndex){
		level = levelIndex;
	}
	
	//receive a game manager, call right after the environment manager is initialized
	public void setGM(GameManager gm){
		this.gm = gm;
	}
	
	public GameSimulation getPathSimulator(){
		return pathSimulator;
	}
	
	public Stack<GNode> findShortestPath(GameObject me, GameObject target)
	{
		Point myGird = Constants.positionToGrid(me.getPosition());
		
		Point targetGrid = Constants.positionToGrid(target.getPosition());
		return pathNodes[myGird.y-1][myGird.x-1].findShortestPath(pathNodes[targetGrid.y-1][targetGrid.x-1]);
	}
	
	public GNode positionToNode(Point p){
		Point grid = Constants.positionToGrid(p);
		return pathNodes[grid.y-1][grid.x-1];
	}
	
	public GameObject girdIndexToTile(Point grid){
		return map_arr[grid.y-1][grid.x-1];
	}
	
	
}