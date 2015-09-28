package Server;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Manager.GameGUI;

public class LogInGUI extends JFrame{
	
	private JButton loginButton;
	private JButton createAccountButton;
	private JTextField passwordTextField;
	private JTextField usernameTextField;
	private JPanel jp;
	private GameGUI mainWindow;

    private PrintWriter pw;
    private BufferedReader br;
    private Socket socket = null;
	
	private JPanel gameSettingPanel;
	JLabel my_ip_Label;
	JLabel my_name_Label;
	JTextField my_name_txtf;
	JCheckBox host_game_btn;
	JTextField enter_ip_txtf;
	JCheckBox custm_port_btn;
	JTextField port_txtf;
	
	//String for informations
		String my_ip_str;
		String other_ip_str;
		String port_str;
		String my_name_str;
		String other_name_str;
	
	
	public LogInGUI() {
		super("User Login");
		createGUI();
		addActionListeners();
		setVisible(true);
		ConnectServer();
	}
	
	public void ConnectServer() {
		try {
			socket = new Socket("localhost", 5000);			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

		try {
			pw = new PrintWriter(socket.getOutputStream());
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			br = new BufferedReader(isr);			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	public void createGUI() {
		setSize(500,500);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		jp = new JPanel();
		jp.setLayout(new GridLayout(3,1));
		JPanel row1 = new JPanel();
		JLabel usernameLabel = new JLabel("Username: ");
		usernameTextField = new JTextField(15);
		row1.add(usernameLabel);
		row1.add(usernameTextField);
		JPanel row2 = new JPanel();
		JLabel passwordLabel = new JLabel("Password: ");
		passwordTextField = new JTextField(15);
		row2.add(passwordLabel);
		row2.add(passwordTextField);
		JPanel row3 = new JPanel();
		row3.setLayout(new GridLayout(1,2));
		loginButton = new JButton("Login");
		createAccountButton = new JButton("Create Account");
		row3.add(loginButton);
		row3.add(createAccountButton);
		jp.add(row1);
		jp.add(row2);
		jp.add(row3);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		
		Border border = BorderFactory.createLineBorder(Color.black);
		jp.setBorder(border);

		add(jp,gbc);
		
		gameSettingPanel = new JPanel();
		my_ip_Label = new JLabel("Your IP: " + my_ip_str + "	");
		//set my player name
		my_name_Label = new JLabel("Name: "); my_name_str = "Player1"; my_name_txtf = new JTextField(my_name_str); 
		//set host by myself or connect to other's ip
		host_game_btn = new JCheckBox("Host Game  Enter an IP: "); other_ip_str = "localhost"; enter_ip_txtf = new JTextField(other_ip_str);
		//set custom port number
		custm_port_btn = new JCheckBox("Custom Port  Port: "); port_str = "6789"; port_txtf = new JTextField(port_str); port_txtf.setEnabled(false);
	}
	
	public void addActionListeners() {
		loginButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {			
				if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(jp,"Missing fields of info.", "Message",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}
				String user = usernameTextField.getText();
				String pass = passwordTextField.getText();
				if(checkPassword(user,pass)) {
					JOptionPane.showMessageDialog(jp,"Logging in.", "Message",
							JOptionPane.PLAIN_MESSAGE);
					jp.setVisible(false);
					
					//create GameGUI Window
					mainWindow = new GameGUI(pw, br);
					mainWindow.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(jp,"Username or password is incorrect.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		
		createAccountButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {				
				if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(jp,"Missing fields of info.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					FileWriter fw = new FileWriter("user.txt",true);
					PrintWriter pw = new PrintWriter(fw);
					if(checkUserName(usernameTextField.getText())) {
						JOptionPane.showMessageDialog(jp,"Username is already taken!", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						pw.println(" \n" + usernameTextField.getText());
						pw.println(passwordTextField.getText());
						pw.flush();
						pw.close();
						fw.close();
						JOptionPane.showMessageDialog(jp,"Creating account.", "Message",
								JOptionPane.PLAIN_MESSAGE);
//						jp.setVisible(false);
					}
				} catch (IOException ioe) {
					System.out.println("IOException: " + ioe.getMessage());
				}
			}
		});
	}
	
	public boolean checkUserName(String str) {
		try {
			FileReader fr = new FileReader("user.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null) {
				System.out.println(line);
				if(str.equals(line)) {
					return true;
				}
				br.readLine();
				line = br.readLine();
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		
		return false;
	}
	
	public boolean checkPassword(String user, String pass) {
		try {
			FileReader fr = new FileReader("user.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null) {
				if(user.equals(line)) {
					//found user
					//now check password
					String password = br.readLine();
					if(pass.equals(password)){
						//passwords match
						return true;
					}
				} else {
					line = br.readLine();
				}
			}
			return false;
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		return false;
	}
	
	
	public static void main(String [] args) {
		LogInGUI lg = new LogInGUI();
	}
}
