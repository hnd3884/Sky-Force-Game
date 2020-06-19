/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketUtil {

    public static void sendViaTcp(Socket socket, String message) {
        try {
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        	bw.write(message);
        	bw.newLine();
        	bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    }
}
