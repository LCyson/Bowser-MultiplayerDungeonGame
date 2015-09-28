package Manager;

import java.awt.Point;
import java.util.Vector;

import GameObject.Constants;
import GameObject.GBullet;
import GameObject.GItem;
import GameObject.GPlayer;

public class GameManager 
{
	public GPlayer player;
	private EnvironmentManager em;
	private GameGUI gGUI;
	private int speed;
	
	//constantly check and update player and enemy's status
	public GameManager(GPlayer player, GameGUI gGUI){
		this.player = player;
		speed = Constants.playerSpeed;
		this.gGUI = gGUI;
	}
	
	//Player status
	boolean isDead;
	int lifes;
	
	//inventory info
	Vector<GItem> inventory;
	
	//Player position change
	public void leftMove() {
		Point destination = new Point(player.getPosition().x-speed, player.getPosition().y);
		boolean a = em.isObjectIntersectWithObstacle(destination);
		boolean b = em.isOutOfMap(destination, player.getDimension());
		if(!a && !b){
			player.translate(-speed, 0);
			player.changeDirection(Constants.W);
		}
	}
	public void rightMove() {
		Point destination = new Point(player.getPosition().x+speed, player.getPosition().y);
		boolean a = em.isObjectIntersectWithObstacle(destination);
		boolean b = em.isOutOfMap(destination, player.getDimension());
		if(!a && !b){
			player.translate(speed, 0);
			player.changeDirection(Constants.E);
		}
	}
	public void upwardMove() {
		Point destination = new Point(player.getPosition().x, player.getPosition().y-speed);
		boolean a = em.isObjectIntersectWithObstacle(destination);
		boolean b = em.isOutOfMap(destination, player.getDimension());
		if(!a && !b){
			player.translate(0, -speed);
			player.changeDirection(Constants.N);
		}
	}
	public void downwardMove() {
		Point destination = new Point(player.getPosition().x, player.getPosition().y+speed);
		boolean a = em.isObjectIntersectWithObstacle(destination);
		boolean b = em.isOutOfMap(destination, player.getDimension());
		if(!a && !b){
			player.translate(0, speed);
			player.changeDirection(Constants.S);
		}
	}
	
	public void shoot(){
		//instantiate a bullet
		Point b_position = player.getRealPosition();
		b_position.x -= Constants.bulletDimention.width/2;
		b_position.y -= Constants.bulletDimention.height/2;
		GBullet bullet = new GBullet(player.getDirection(), b_position, em);
		em.registerBullet(bullet);
		//register the bullet to environment manager
	}

	//Inventory control
	Vector<GItem> getInventory() 
	{
		return inventory;
	}
	public void addItem(GItem it) 
	{
		inventory.add(it);
	}
	public void removeItem(GItem it) 
	{
		inventory.remove(it);
	}
    
	public void useItem(GItem it) {
		it.takeAction(player);
	}
	public void respawn(Point position) 
	{
		player.changePosition(position);
	}
	
	public void setEM(EnvironmentManager em){
		this.em = em;
	}

}