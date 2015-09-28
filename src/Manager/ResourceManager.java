package Manager;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import GameObject.Constants;

public class ResourceManager 
{
	public ImageIcon stickman, water, white_floor, guard, bullet, stickman_grey, lifebar;
	private static String imagePath = Constants.imagePath;
	private Image temp;
	enum imageID{stickman, water, white_floor, guard, bullet, stickman_grey, lifebar};
	
	public ResourceManager()
	{
		loadImage();
		resizeAllImage();
	}
	
	public void loadImage()
	{
		stickman = new ImageIcon(imagePath + "stickman.png");
		water = new ImageIcon(imagePath + "water1.png");
		white_floor = new ImageIcon(imagePath + "white_floor_tile.png");
		guard = new ImageIcon(imagePath + "guard.png");
		bullet = new ImageIcon(imagePath + "bullet.png");
		stickman_grey = new ImageIcon(imagePath + "stickman_grey.png");
		lifebar = new ImageIcon(imagePath + "lifebar.png");
	}
	
	public void resizeAllImage()
	{
		stickman = resizeImage(stickman, Constants.imageDimension);
		water = resizeImage(water, Constants.imageDimension);
		white_floor = resizeImage(white_floor, Constants.imageDimension);
		guard = resizeImage(guard, Constants.imageDimension);
		bullet = resizeImage(bullet, Constants.bulletDimention);
		stickman_grey = resizeImage(stickman_grey, Constants.imageDimension);
		lifebar = resizeImage(lifebar, new Dimension(Constants.imageDimension.width*Constants.mapGridWidth/3, Constants.imageDimension.height/2));
	}
	
	public ImageIcon resizeImage(ImageIcon imageIcon, Dimension d)
	{
		temp = imageIcon.getImage();
		temp = temp.getScaledInstance(d.width, d.height, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(temp);
		return imageIcon;
	}

	public ImageIcon getImageIcon(imageID getID)
	{
		if(getID == imageID.stickman)
			return stickman;
		if(getID == imageID.water)
			return water;
		if(getID == imageID.white_floor)
			return white_floor;
		if(getID == imageID.guard)
			return guard;
		if(getID == imageID.bullet)
			return bullet;
		if(getID == imageID.stickman_grey)
			return stickman_grey;
		if(getID == imageID.lifebar)
			return lifebar;
		else
			return null;
	}
}
