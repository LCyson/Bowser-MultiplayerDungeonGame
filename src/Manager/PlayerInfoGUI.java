package Manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class PlayerInfoGUI extends JFrame {
	
	public PlayerInfoGUI() {
		
		super("Player Info");
		setSize(600, 400);
		setLocation(300, 150);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1,2));
		
		JLabel infoLabel = new JLabel("Info", JLabel.CENTER);
		northPanel.add(infoLabel);
		
		JLabel itemsLabel = new JLabel("Items", JLabel.CENTER);
		northPanel.add(itemsLabel);
		
		add(northPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,2));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();
		
		JLabel nameLabel = new JLabel("Name:", JLabel.CENTER);
		JLabel name = new JLabel("Jonathan", JLabel.CENTER);
		
		JLabel healthLabel = new JLabel("Health:", JLabel.CENTER);
		JLabel health = new JLabel("150 HP", JLabel.CENTER);
		
		JLabel magicLabel = new JLabel("Magic:", JLabel.CENTER);
		JLabel magic = new JLabel("75 MP", JLabel.CENTER);
		
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.gridx = 0;
		gbc2.gridy = 0;
		// gbc2.gridwidth = 3;
		gbc2.ipadx = 30;
		gbc2.ipady = 20;
		infoPanel.add(nameLabel, gbc2);
		
		gbc2.gridx = 4;
		gbc2.gridy = 0;
		infoPanel.add(name, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		infoPanel.add(healthLabel, gbc2);
		
		gbc2.gridx = 4;
		gbc2.gridy = 1;
		infoPanel.add(health, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy = 2;
		infoPanel.add(magicLabel, gbc2);
		
		gbc2.gridx = 4;
		gbc2.gridy = 2;
		infoPanel.add(magic, gbc2);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		// gbc.gridheight = 1;
		// gbc.gridwidth = 1;
		Border border = BorderFactory.createLineBorder(Color.black);
		infoPanel.setBorder(border);
		leftPanel.add(infoPanel,gbc);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridBagLayout());
		GridBagConstraints gbc3 = new GridBagConstraints();
		JLabel weaponLabel = new JLabel("Weapon", JLabel.CENTER);
		JLabel armorLabel = new JLabel("Armour", JLabel.CENTER);
		gbc3.gridx = 0;
		gbc3.gridy = 0;
		// gbc3.gridwidth = 3;
		gbc3.ipadx = 31;
		gbc3.ipady = 5;
		weaponLabel.setBorder(border);


		panel2.add(weaponLabel, gbc3);
		gbc3.gridx = 1;
		gbc3.gridy = 0;

		armorLabel.setBorder(border);
		panel2.add(armorLabel, gbc3);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		leftPanel.add(panel2,  gbc);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridBagLayout());
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.fill = GridBagConstraints.HORIZONTAL;
		gbc4.gridx = 0;
		gbc4.gridy = 0;
		gbc4.gridheight = 1;
		gbc4.gridwidth = 1;
		gbc4.ipadx = 35;
		gbc4.ipady = 10;
		
		// if j = 0 or 1 then it is a weapon
		// if j = 2 or 3 then it is an armor
		
		for (int i = 0; i < 3; i++) {
			gbc4.gridx = 0;
			for (int j = 0; j < 4; j++) {
				// if the player has an item
				JLabel weaponImageLabel = new JLabel(" ");
				weaponImageLabel.setBorder(border);
				panel3.add(weaponImageLabel, gbc4);
				gbc4.gridx++;
			}
			gbc4.gridy++;
		}
		
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		leftPanel.add(panel3, gbc);
	
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc7 = new GridBagConstraints();
		gbc7.fill = GridBagConstraints.HORIZONTAL;
		gbc7.gridx = 0;
		gbc7.gridy = 0;
		gbc7.gridheight = 1;
		gbc7.gridwidth = 1;
		gbc7.ipadx = 30;
		gbc7.ipady = 30;
		
		for (int i = 0; i < 4; i++) {
			gbc7.gridx = 0;
			for (int j = 0; j < 4; j++) {
				// if the player has an item
				JLabel itemImageLabel = new JLabel(" ");
				itemImageLabel.setBorder(border);
				rightPanel.add(itemImageLabel, gbc7);
				gbc7.gridx++;
			}
			gbc7.gridy++;
		}
		
		centerPanel.add(leftPanel);
		centerPanel.add(rightPanel);
		
		
		add(centerPanel, BorderLayout.CENTER);
		
		
		
		
		setVisible(true);
		
	}
	
	public static void main(String [] args) {
		
		PlayerInfoGUI pig = new PlayerInfoGUI();
		
	}

}
