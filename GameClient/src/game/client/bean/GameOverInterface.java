package game.client.bean;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import game.client.com.Communication;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameOverInterface extends JFrame {

	public static JPanel contentPane;
	public static JTable table;
	public static gameSetUp gamesS;
	private String username;

	public GameOverInterface(gameSetUp comm, String username) {
		this.gamesS = comm;
		this.username = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 379, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Ranking table");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(12, 13, 341, 41);
		contentPane.add(lblNewLabel);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Player", "Score"},
			},
			new String[] {
				"Player", "Score"
			}
		));
		table.setBounds(12, 56, 341, 192);
		contentPane.add(table);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		btnQuit.setBounds(86, 261, 189, 33);
		contentPane.add(btnQuit);
	}
}
