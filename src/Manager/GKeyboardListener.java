package Manager;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class GKeyboardListener implements KeyEventDispatcher
{
	GameManager gm; 
	EnvironmentManager em;
	
	public GKeyboardListener (GameManager gm, EnvironmentManager em)
	{this.gm = gm; this.em = em;}
	
	public boolean dispatchKeyEvent(KeyEvent e) 
    {
		int keyCode = e.getKeyCode();
        if (e.getID() == KeyEvent.KEY_PRESSED) {
        	//listen to the arrow keys
			if(keyCode >= 37 && keyCode <= 40)
			{
				switch(keyCode)
				{
				case 37 /*left*/:
					gm.leftMove();
					break;
				case 38 /*up*/:
					gm.upwardMove();
					break;
				case 39 /*right*/:
					gm.rightMove();
					break;
				case 40 /*down*/:
					gm.downwardMove();
					break;
				}
			}
        } 
        else if (e.getID() == KeyEvent.KEY_RELEASED) {
        	if(keyCode == KeyEvent.VK_SPACE){
        		gm.shoot();
        	}
        } 
        else if (e.getID() == KeyEvent.KEY_TYPED) {
        	
        }
        return false;
    }
}
