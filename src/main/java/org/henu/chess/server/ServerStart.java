
package org.henu.chess.server;

import java.net.*;
import java.util.ArrayList;

import javax.swing.JTextArea;

import java.io.*;

public class ServerStart extends Thread{
	
	ServerSocket server;
	public static ArrayList <ServerThread>al = new ArrayList<>();
	public static ArrayList <ServerThread>player = new ArrayList<>();
	public static int chessnum = 0;
	public static int chess[][] = new int[15][15];
	public ServerStart(ServerSocket server) {
		this.server = server;
	}
	public void run() {
		while(true) {
			try {
				Socket socket = server.accept();  //开始监听，如果有客户端连接，立即生成一个Socket对象							
				ServerThread st = new ServerThread(socket);
				st.start();
//				al.add(st);
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}
}
