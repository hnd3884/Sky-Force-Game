/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.com;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketUtil {

    public static void sendViaTcp(String message) {
        try {
        	Socket socket = new Socket("127.0.0.1",6789); 
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        	bw.write(message);
        	bw.newLine();
        	bw.flush();
        	socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    }
}
