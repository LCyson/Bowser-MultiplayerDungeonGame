package GameObject;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class Constants {
	public static final String imagePath = "resource/image/";
	public static final String imageName = "stickman.png";
	public static final String S = "South";
	public static final String N = "North";
	public static final String W = "West";
	public static final String E = "East";
	
	//enum 
	public static enum mapElement{obstacle, tile};
	
	//map dimensions
	public static Dimension imageDimension = new Dimension(50,50); /*the pixel length for each tile*/
	public static Dimension bulletDimention = new Dimension(15, 15);
	public static int mapGridWidth = 12, mapGridHeight = 12; /*the number of grids*/
	public static String gameTitle = "Temp Game Title";
	

	//object movement
	public static int playerSpeed = 10;
	public static int enemySpeed = 3;
	public static int closeEnough = 5;
	
	//bullet
	public static int bulletDamage = 20;
	public static int enemyHP = 60;
	
	//enemyBehavior
	public static int enemyChaseTime = 4;
	public static int enemyRoamTime = 4;
	public static int enemyFPS = 20;
	public static int playerHP = 100;
	public static int enemyDamage = 15;
	public static int playerFPS = 30;
	public static int playerInvulnerableTime = 2;
	
	//other
	public static int killEnemyScore = 20;
	public static int enemyRespawnTime = 8;
	

	//convert render position to grid index position
	public static Point positionToGrid(Point p) {
		Point grid = new Point(p.x, p.y);
		grid.x = p.x / imageDimension.width + 1;
		grid.y = p.y / imageDimension.height + 1;
		if (grid.x > mapGridWidth) grid.x = mapGridWidth;
		if (grid.x < 0) grid.x = 0;
		if (grid.y > mapGridHeight) grid.y = mapGridHeight;
		if (grid.y < 0) grid.y = 0;
		
		return grid;
	}
	
	//check whether two objects are in the same tile
	public static boolean checkOverlap(GameObject gb1, GameObject gb2) {
		if ( positionToGrid(gb1.getPosition()).
				equals
			(positionToGrid(gb2.getPosition())))
			return true;
		else
			return false;
	}
}