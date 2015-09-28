package GameObject;

public class GArmor extends GItem{

	public void takeAction(GPlayer player) {
		player.setArmor(50);
	}
}
