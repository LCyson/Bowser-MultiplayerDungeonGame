package GameObject;

public abstract class GItem extends GameObject {
	
	public void show(boolean b) {
		this.setVisible(b);
	}
	
	public abstract void takeAction(GPlayer player);
}
