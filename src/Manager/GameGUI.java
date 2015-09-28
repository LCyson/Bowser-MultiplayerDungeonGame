package Manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import GameObject.Constants;
import GameObject.GBullet;
import GameObject.GObstacle;
import GameObject.GPlayer;
import GameObject.GTile;
import GameObject.GameObject;
import Manager.ResourceManager.imageID;

public class GameGUI extends JFrame implements Runnable
{
	public GameManager gm;
	public EnvironmentManager em;
	public ResourceManager rm;
	private PrintWriter pw;
	private BufferedReader br;
	
	public GPlayer player;
	public JLabel[][] mapLabel;
	public JLabel playerLabel;
	public JLabel lifebarLabel;
	public JLabel lifebarTxt;
	public JLabel scoreTxt;
	
	public boolean gameEnd = false;
	public int enemyRefreshCounter;
	public Vector<JLabel> enemyLabel_vec;
	public Vector<JLabel> bulletLabel_vec;
	
	public GameGUI (PrintWriter pw, BufferedReader br)
	{
		super(Constants.gameTitle);
		this.pw = pw;
		this.br = br;
		initializeVariables();
		createGUI();
		addEventHandler();
		
		enemyRefreshCounter = Constants.playerFPS * Constants.enemyRespawnTime;
		Thread gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void initiateLevel(int levelIndex)
	{
		em.setLevel(levelIndex);
		em.generateMap();
		em.generateEnemy();
		em.modifyPlayerPosition();
	}
	
	public void initializeVariables()
	{	
		player = new GPlayer("player", EnvironmentManager.GridToRenderPosition(EnvironmentManager.rollGridPosition()));
		gm = new GameManager(player, this);
		em = new EnvironmentManager(player, this);
		gm.setEM(em); em.setGM(gm);
		rm = new ResourceManager();
		initiateLevel(1);
		mapLabel = new JLabel[Constants.mapGridWidth][Constants.mapGridHeight];
		enemyLabel_vec = new Vector<JLabel>();
		bulletLabel_vec = new Vector<JLabel>();
	}
	
	public void createGUI()
	{
		setLayout(null);
		setSize(Constants.mapGridWidth*Constants.imageDimension.width,
				(Constants.mapGridHeight+1)*Constants.imageDimension.height);
		drawPlayer();
		drawEnemy();
		drawTiles();
		drawOtherElements();
	}
	
	public void drawTiles()
	{
		for(int w = 0; w < em.map_arr.length; ++w)
		{
			for (int h = 0; h < em.map_arr[w].length; ++h)
			{
				Point tempPosition = em.map_arr[w][h].getPosition();
				
				if(em.map_arr[w][h] instanceof GTile)
					mapLabel[w][h] = new JLabel(rm.getImageIcon(ResourceManager.imageID.white_floor));
				else if (em.map_arr[w][h] instanceof GObstacle)
					mapLabel[w][h] = new JLabel(rm.getImageIcon(ResourceManager.imageID.water));
				
				mapLabel[w][h].setBounds(tempPosition.x, tempPosition.y, 
										mapLabel[w][h].getPreferredSize().width, mapLabel[w][h].getPreferredSize().height);
				mapLabel[w][h].setBorder(BorderFactory.createLineBorder(Color.black));
				add(mapLabel[w][h]);
			}
		}
	}
	
	public void drawEnemy(){
		for(int e = 0; e < em.enemy_vec.size(); ++e)
		{
			Point tempPosition = em.enemy_vec.elementAt(e).getPosition();
			JLabel enemyLabel= new JLabel(rm.getImageIcon(ResourceManager.imageID.guard));
			enemyLabel.setBounds(tempPosition.x, tempPosition.y,
								enemyLabel.getPreferredSize().width, enemyLabel.getPreferredSize().height);
			enemyLabel_vec.addElement(enemyLabel);
			add(enemyLabel);
		}
	}
	
	public void drawPlayer(){
		Point tempPosition = player.getPosition();
		playerLabel = new JLabel(rm.getImageIcon(ResourceManager.imageID.stickman));
		playerLabel.setBounds(tempPosition.x, tempPosition.y, playerLabel.getPreferredSize().width, playerLabel.getPreferredSize().height);
		add(playerLabel);
	}

	//life bar and score text
	public void drawOtherElements(){
		lifebarTxt = new JLabel("Player HP :");
		lifebarTxt.setBounds(Constants.mapGridWidth/2, (Constants.mapGridHeight)*Constants.imageDimension.height, 
							lifebarTxt.getPreferredSize().width*2, lifebarTxt.getPreferredSize().height);
		lifebarLabel = new JLabel(rm.getImageIcon(ResourceManager.imageID.lifebar));
		lifebarLabel.setBounds(Constants.imageDimension.width * 3, (Constants.mapGridHeight)*Constants.imageDimension.height, 
							lifebarLabel.getPreferredSize().width, lifebarLabel.getPreferredSize().height);
		add(lifebarTxt);
		add(lifebarLabel);
		
		scoreTxt = new JLabel("Score: " + player.score);
		scoreTxt.setBounds((Constants.mapGridWidth/2 +2)*Constants.imageDimension.width, (Constants.mapGridHeight)*Constants.imageDimension.height, 
				scoreTxt.getPreferredSize().width*4, scoreTxt.getPreferredSize().height);
		add(scoreTxt);
	}
	
	public void addEventHandler()
	{
		 KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		 manager.addKeyEventDispatcher(new GKeyboardListener(gm, em));
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void registerBulletLabel(GBullet bullet)
	{
		JLabel temp = new JLabel(rm.getImageIcon(ResourceManager.imageID.bullet));
		Point tempPosition = bullet.getPosition();
		temp.setBounds(tempPosition.x, tempPosition.y, temp.getPreferredSize().width, temp.getPreferredSize().height);
		add(temp, 10);
		bulletLabel_vec.addElement(temp);
	}
	
	//keep calling this function to update object's position
	public void rendererUpdate(){
		enemyRendererUpdate();
		bulletRendererUpdate();
		//render player per frame
		playerRendererUpdate();
		otherRendererUpdate();
	}
	
	public void enemyRendererUpdate(){
		em.enemy_lock.lock();
		//render enemy per frame
		for(int i = 0; i < em.enemy_vec.size(); ++i){
			if(em.enemy_vec.elementAt(i) != null){
				render(enemyLabel_vec.elementAt(i), em.enemy_vec.elementAt(i));
			}
		}
		int j = em.enemy_vec.size();
		while(j < enemyLabel_vec.size()){
			JLabel enemyLabel = enemyLabel_vec.elementAt(j);
			enemyLabel.setVisible(false);
			enemyLabel = null;
			enemyLabel_vec.removeElementAt(j);
		}
		em.enemy_lock.unlock();
	}
	
	public void bulletRendererUpdate(){
		em.bullet_lock.lock();
		for(int i =0; i < em.bullet_vec.size(); ++i){
			if(bulletLabel_vec.elementAt(i) != null){
				render(bulletLabel_vec.elementAt(i), em.bullet_vec.elementAt(i));
			}
		}
		int j = em.bullet_vec.size();
		while(j < bulletLabel_vec.size()){
			JLabel bulletLabel = bulletLabel_vec.elementAt(j);
			bulletLabel.setVisible(false);
			bulletLabel = null;
			bulletLabel_vec.removeElementAt(j);
		}
		em.bullet_lock.unlock();
	}
	
	public void playerRendererUpdate(){
		
		if(player.invulnerable){
			playerLabel.setIcon(rm.getImageIcon(ResourceManager.imageID.stickman_grey));
		}
		else{
			playerLabel.setIcon(rm.getImageIcon(ResourceManager.imageID.stickman));
		}
		
		render(playerLabel, player);
	}
	
	public void otherRendererUpdate(){
		lifebarTxt.setText("Player HP :" + player.getHP() +"/"+ Constants.playerHP);
		float hpRatio = (float)player.getHP(); hpRatio/= (float)Constants.playerHP;		
		lifebarLabel.setBounds(Constants.imageDimension.width * 3, (Constants.mapGridHeight)*Constants.imageDimension.height, 
				(int) (lifebarLabel.getPreferredSize().width * hpRatio), lifebarLabel.getPreferredSize().height);
		scoreTxt.setText("Score: " + player.score);
	}
	
	public void render(JLabel lb, GameObject obj){
		lb.setBounds(obj.getPosition().x, obj.getPosition().y, lb.getPreferredSize().width, lb.getPreferredSize().height);
	}

	//called per frame to see if game ended
	public void checkGameEnd(){
		if(player.getHP() == 0){
			gameEnd = true;
			if(JOptionPane.showConfirmDialog(this, 
		            "You Lost! Would You Like To Play Again?", "Game Over", 
		            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
					== JOptionPane.YES_OPTION)
			{
				GameGUI game = new GameGUI(pw, br);
				game.setVisible(true);
			}
			this.dispose();
		}
		if(em.getEnemy().size() == 0){
			gameEnd = true;
			if(JOptionPane.showConfirmDialog(this, 
		            "You Win! Would You Like To Play Again?", "Game Over", 
		            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
					== JOptionPane.YES_OPTION)
			{
				GameGUI game = new GameGUI(pw, br);
				game.setVisible(true);
			}
			this.dispose();
		}
	}
	
	public void respawnEnemyLabel(){
		Point tempPosition = em.enemy_vec.elementAt(em.enemy_vec.size()-1).getPosition();
		JLabel enemyLabel= new JLabel(rm.getImageIcon(ResourceManager.imageID.guard));
		enemyLabel.setBounds(tempPosition.x, tempPosition.y,
							enemyLabel.getPreferredSize().width, enemyLabel.getPreferredSize().height);
		enemyLabel_vec.addElement(enemyLabel);
		add(enemyLabel,5);
	}
	
	public void refreshEnemy(){
		em.respawnEnemy();
		respawnEnemyLabel();
		System.out.println("Enemy refreshed!");
	}
	
	@Override
	public void run() 
	{
		while(!gameEnd){
			if(em.enemy_vec.size() < em.num_Enemy){
				enemyRefreshCounter--;
				if(enemyRefreshCounter == 0){
					refreshEnemy();
					enemyRefreshCounter = Constants.playerFPS * Constants.enemyRespawnTime;
				}
			}
			rendererUpdate();
			checkGameEnd();
			try {
				Thread.sleep(1000/Constants.playerFPS);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
