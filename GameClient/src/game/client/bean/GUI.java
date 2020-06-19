package game.client.bean;

import game.client.com.Communication;
import game.client.com.SocketUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI implements ActionListener {
	ArrayList<String> database = new ArrayList<String>();
	final String FILE_URL = "D:/source/java/GameClient/src/Database.txt";
	public static JFrame frame;
	JButton btnSignIn;
	JButton btnSignUp;
	JPanel pnMain;
	JLabel lbUsername, lbPassword;
	GridBagLayout gridBagLayout;
	GridBagConstraints gbc;
	JTextField password, username;
	Communication comm;

	public GUI() {
		// getDatabase();
		initContainer();
		initComponent();
		addComponent();
		addEvent();
		comm = new Communication();
		comm.Init();
	}

	private void initContainer() {
		frame = new JFrame("Sky Force Game");
		frame.setSize(new Dimension(300, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);

		pnMain = new JPanel();

		gridBagLayout = new GridBagLayout();
		gbc = new GridBagConstraints();

		pnMain.setLayout(gridBagLayout);
	}

	private void initComponent() {
		btnSignIn = new JButton("Sign in");
		btnSignUp = new JButton("Sign up");

		username = new JTextField();
		password = new JPasswordField();
		lbUsername = new JLabel("Username");
		lbPassword = new JLabel("Password");
	}

	private void addComponent() {
		frame.add(pnMain);

		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		pnMain.add(lbUsername, gbc);

		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		pnMain.add(username, gbc);

		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		pnMain.add(lbPassword, gbc);

		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 4;
		pnMain.add(password, gbc);

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 6;
		pnMain.add(btnSignIn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 6;
		pnMain.add(btnSignUp, gbc);

	}

	private void addEvent() {
		btnSignIn.setActionCommand("1");
		btnSignIn.addActionListener(this);
		btnSignUp.setActionCommand("2");
		btnSignUp.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if ("1".equals(e.getActionCommand())) {
			String uname = username.getText();
			String pw = password.getText();
			if (!uname.equals("") && !pw.equals("")) {
				//gameSetUp game = new gameSetUp("Sky Force Game!", 500, 600);
				String message = "log " + uname + "|" + pw;
				comm.Send(message);
			} else {
				JOptionPane.showMessageDialog(null, "Username or Password is empty !");
			}
		} else if ("2".equals(e.getActionCommand())) {

			JTextField username_su = new JTextField();
			JPasswordField password_su = new JPasswordField();

			Object[] message = { "Username:", username_su, "Password:", password_su };
			int option = JOptionPane.showOptionDialog(null, message, "Sign Up", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Sign Up", "Cancel" }, // this is the array
					"default");
			if (option == JOptionPane.YES_OPTION) {
				// handle if click LOGIN
				String uname = username_su.getText();
				String pw = password_su.getText();
				if (uname != "" && pw != null) {
					String messageReg = "reg " + uname + "|" + pw;
					comm.Send(messageReg);
				} else {
					JOptionPane.showMessageDialog(null, "Username or Password is empty !");
				}
			} else {
				
			}
		}
	}

	void showGUI() {
		frame.setVisible(true);
	}
}