package GameObject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class GameObject extends JPanel
{
	//basic GameObject variables
	protected ImageIcon gImageIcon;
	protected String gName;
	protected String Direction;
	
	//The location and size for rendering
	protected Rectangle renderBound;
	protected Point position;
	
	//true location only the object knows
	private int width = 25, height = 25;
	
	public boolean isDead = false;
	
	public GameObject() {
		
	}
	
	public GameObject(String name)
	{
		this.gName = name;
		setLayout(null);
	}
	
	//set the imageIcon for this object
	//should be called by the environment/game manager, 
	//when they instantiate the GameObject
	public void setImage(ImageIcon imageIcon)
	{this.gImageIcon = imageIcon;}
	
	//not very necessary at the moment
	public void setSize(int width, int height)
	{
		this.width = width; this.height = height;
	}
	
	//probably not necessary
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		g.drawImage(gImageIcon.getImage(), 0, 0, this);
	}
	
	//change the rendering position by x,y
	public void translate(int x, int y)
	{
		position.x += x;
		position.y += y;
	}
	
	//return the object's rendering position
	public Point getPosition()
	{return position;}
	
	//return the object's real position
	public Point getRealPosition() {
		return new Point(position.x+(Constants.imageDimension.width/2), position.y+(Constants.imageDimension.height/2));
	}
	
	
	//change position 
	public void changePosition(Point p) {
		position = p;
	}
	
	public String getDirection()
	{return Direction;}
	
	public void changeDirection(String dir)
	{
		if(!dir.equals(Constants.N) && !dir.equals(Constants.S) && !dir.equals(Constants.E) && !dir.equals(Constants.W))
			return;
		else
			this.Direction = dir;
	}
	
	public void setName(String name){
		gName = name;
	}
	
	public String getName(){
		return gName;
	}
	public Dimension getDimension()
	{return Constants.imageDimension;}
	
	public Point getCurrentGrid(){
		return Constants.positionToGrid(getRealPosition());
	}
	
	//should be called per update frame
	//make object to move toward given render position at given speed
	//return true if arrived
	public boolean moveTowards(Point p, int speed){
		if(!position.equals(p)){
			boolean xMatch = false; boolean yMatch = false;
			int xDistance = p.x - position.x;
			int yDistance = p.y - position.y;
		
			if(Math.abs(yDistance) != 0){
				if(Math.abs(yDistance) < speed){
					position.y = p.y;
					yMatch = true;
				}else{
					int sign = yDistance/Math.abs(yDistance);
					position.y += sign*speed;
				}
			}
			else{
				if(Math.abs(xDistance) < speed){
					position.x = p.x;
					xMatch = true;
				}else{
					int sign = xDistance/Math.abs(xDistance);
					position.x += sign*speed;
				}
			}

			return (xMatch && yMatch);
		}
		else return true;
	}
	//same as above
	public void moveTowards(GameObject g, int speed){
		moveTowards(g.getPosition(), speed);
	}
	
}