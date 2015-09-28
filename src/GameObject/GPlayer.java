package GameObject;

import java.awt.Image;
import java.awt.Point;

public class GPlayer extends GameObject implements Runnable
{
	protected int hp, mp;
	protected int armor;
	public boolean invulnerable = true;
	private int invulnerableCounter;
	private int secondCounter;
	public int score;

	
	public GPlayer(String name, Point p)
	{
		super(name);
		hp = Constants.playerHP;
		mp = 70;
		armor = 0;
		score = 0;
		Direction = "North";
		position = p;
		invulnerableCounter = Constants.playerFPS * Constants.playerInvulnerableTime;
		secondCounter = Constants.playerFPS;
		Thread myThread = new Thread(this);
		myThread.start();
	}

	public void changeHP(int HP) {
		hp += HP;
	}
	public void changeMP(int MP) {
		mp += MP;
	}
	public void setArmor(int defensiveA) {
		armor += defensiveA;
	}
	
	public void getHit(int damage){
		if(!invulnerable){
			hp -= damage;
			invulnerable = true;
			if(hp <= 0)
				hp = 0;
		}
	}
	
	public void run (){
		while(!isDead){
			//add 1 score per second when player is alive
			secondCounter--;
			if(secondCounter == 0){
				secondCounter = Constants.playerFPS;
				score += 1;
			}
				
			//set a timer for player to be shortly invulnerable after being attacked
			if(invulnerable){
				invulnerableCounter--;
				if(invulnerableCounter < 0){
					invulnerableCounter = Constants.playerFPS * Constants.playerInvulnerableTime;
					invulnerable = false;
				}
			}
			try {Thread.sleep(1000/Constants.playerFPS);} 
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public int getHP(){return hp;}
}
