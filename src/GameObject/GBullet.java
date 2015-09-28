package GameObject;

import java.awt.Dimension;
import java.awt.Point;

import Manager.EnvironmentManager;

public class GBullet extends GameObject implements Runnable{
	private String Direction;
	private EnvironmentManager eManager;
	private int row, col;
	
	public GBullet(String Direction, Point Position, EnvironmentManager eManager) {
		this.Direction = Direction;
		this.eManager = eManager;
		position = Position;
		Thread t = new Thread(this); t.start();
	}
	
	public void run() {
		while (!isDead) {
			eManager.checkBulletCollide(this);
			ChangePosition();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ChangePosition() {
		if ( Direction.equals(Constants.N) ) {
			position.y -= 10;
		} else if ( Direction.equals(Constants.S) ) {
			position.y += 10;
		} else if ( Direction.equals(Constants.W) ) {
			position.x -= 10;
		} else if ( Direction.equals(Constants.E) ) {
			position.x += 10;
		}
	}
	
	public Dimension getDimension()
	{return Constants.bulletDimention;}
	
//end of bullet class
}
