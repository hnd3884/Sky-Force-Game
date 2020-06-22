package game.client.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import game.client.bean.Command;
import game.client.bean.GUI;
import game.client.bean.GameOverInterface;
import game.client.bean.Player;
import game.client.bean.gameSetUp;

public class Communication {
	public static String sentence;
	public static int server_port = Config.PORT;
	public static String server_ip = Config.LOCAL_IP;
	public static Socket clientSocket = null;
	public static BufferedReader inFromUser;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static String playerName;
	public static gameSetUp gamesS;
	public LinkedList<Command> queueCommands = new LinkedList<Command>();

	public Communication() {
		try {
			clientSocket = new Socket(server_ip, server_port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Receive() {
		Runnable run = new Runnable() {
			public void run() {
				try {
					while (true) {
						String message = inFromServer.readLine();
						// gamesS.start();
						if (message.startsWith(Config.RENDERENEMY_CODE)) {
							gamesS.manager.RenderEnemy(message);
						} else if (message.startsWith(Config.MOVE_CODE)) {
							message = message.substring(4);
							String[] query = message.split("\\|");
							for (Player fr : gamesS.manager.friends) {
								if (fr.getUserName().equals(query[0])) {
									if (query[1].equals("left")) {
										fr.TurnLeftByHost();
									} else if (query[1].equals("right")) {
										fr.TurnRightByHost();
									} else if (query[1].equals("fire")) {
										fr.FireByHost();
									}
								} else
									break;
							}
						} else if (message.startsWith(Config.STATUS_IN_GAME)) {
							message = message.substring(4);
							if (message.equals("gameover")) {
								gamesS.manager.setStart(false);
								gamesS.manager.setReadyToPlay(false);
								gamesS.manager.setGameStatus("Game Over!");
								GameOverInterface gameover = new GameOverInterface(gamesS, playerName);

								List<Player> scoreRank = new ArrayList<Player>();
								scoreRank.add(gamesS.manager.player);
								for (Player fr : gamesS.manager.friends) {
									scoreRank.add(fr);
								}
								Collections.sort(scoreRank, Comparator.comparing(Player::getScore));

								DefaultTableModel model = (DefaultTableModel) gameover.table.getModel();
								// model.addRow(new Object[]{gamesS.manager.player.getUserName(),
								// gamesS.manager.GetScore()});
								for (int i = scoreRank.size() - 1; i >= 0; i--) {
									model.addRow(new Object[] { scoreRank.get(i).getUserName(),
											scoreRank.get(i).getScore() });
								}
								gameover.setVisible(true);
							} else {
								String[] mess = message.split("\\|");
								if (mess[1].equals("score")) {
									// System.out.println("player "+mess[0]+" scored!");
									for (Player fr : gamesS.manager.friends) {
										if (fr.getUserName().equals(mess[0])) {
											fr.Score();
											break;
										}
									}
								}
							}
						} else if (message.startsWith(Config.STARTGAME_CODE)) {
							message = message.substring(4);
							if (message.equals("failed")) {
								gamesS.manager.setGameStatus("Wait for other players!");
							} else if (message.startsWith("waithost")) {
								String[] usernames = message.split("\\|");
								for (int i = 1; i < usernames.length; i++) {
									if (!usernames[i].equals(gamesS.manager.player.getUserName())) {
										gamesS.manager.addFriend(usernames[i]);
									}
								}

								gamesS.manager.setGameStatus("Wait host start game!");
							} else if (message.startsWith("ready")) {
								String[] usernames = message.split("\\|");
								for (int i = 1; i < usernames.length; i++) {
									if (!usernames[i].equals(gamesS.manager.player.getUserName())) {
										gamesS.manager.addFriend(usernames[i]);
									}
								}

								gamesS.manager.setGameStatus("Press enter to start!");
								gamesS.manager.setReadyToPlay(true);
								gamesS.manager.player.setHost(true);
							} else if (message.equals("ok")) {
								// gamesS.manager.renderFriends();
								gamesS.manager.StartByHost();
							}
						} else if (message.startsWith(Config.LOGIN_CODE)) {
							message = message.substring(4);
							String[] mess = message.split("\\|");
							if (mess[0].equals("ok")) {
								GUI.frame.setVisible(false);
								playerName = mess[1];
								gamesS = new gameSetUp("Sky Force Game!", 500, 600);
								gamesS.init(playerName);
								gamesS.start();
							} else {
								JOptionPane.showMessageDialog(null, "Username or Password is incorrect !");
							}
						} else if (message.startsWith(Config.REGISTER_CODE)) {
							message = message.substring(4);
							if (message.equals("ok")) {
								JOptionPane.showMessageDialog(null, "Register success!");
							} else if (message.equals("failed")) {
								JOptionPane.showMessageDialog(null, "Username is exist!");
							}
						}
					}
				} catch (Exception e) {

				}
			}
		};

		Thread receive = new Thread(run);
		receive.start();
	}

	public static void Send(String message) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			bw.write(message);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Init() {
		try {
			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			Receive();

		} catch (Exception e) {
			System.out.println("Cannot connect to TCP Server.\n Please check the server and run tcpclient again.");
			System.exit(0);
		}
	}
}
