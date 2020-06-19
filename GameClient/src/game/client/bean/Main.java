package game.client.bean;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import game.client.com.Communication;

public class Main {
	
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.showGUI();
		
	}
}
