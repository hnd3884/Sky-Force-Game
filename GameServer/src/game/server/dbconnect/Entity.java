package game.server.dbconnect;

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

public class Entity {
	public static ArrayList<String> database = new ArrayList<String>();
	static final String FILE_URL = "D:/source/java/GameServer/src/Database.txt";
	
	public static void getDatabase() {
		File file = new File(FILE_URL);
		//database.clear();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);

		String lineRead = "";
		String line = "";
		try {
			while ((lineRead = reader.readLine()) != null) {
				line += lineRead;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] accounts = line.split("___");
		for (String acc : accounts) {
			database.add(acc);
		}
	}
	
	public static void updateDatabase(String acc) {
		File file = new File(FILE_URL);
		database.add(acc);
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
		try {
			outputStreamWriter.write("___" + acc);
			outputStreamWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkUser(String acc) {
		if(database.contains(acc)) {
			return true;
		}
		return false;
	}
}
