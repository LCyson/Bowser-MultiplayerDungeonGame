package GameObject;

public class GPortion extends GItem {

	public void takeAction(GPlayer player) {
		player.changeHP(30);
		player.changeMP(30);
	}
}
