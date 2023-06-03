package org.henu.chess.server;

import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.Result;
import org.henu.chess.common.messages.request.CreateRoomRequest;
import org.henu.chess.common.messages.request.JoinRoomRequest;
import org.henu.chess.common.messages.response.CreateRoomResponse;
import org.henu.chess.common.messages.response.JoinRoomResponse;
import org.henu.chess.common.messages.response.StartGameResponse;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器端线程
 */
public class ServerStart extends Thread {

    ServerSocket server;
    public static ArrayList<ServerThread> al = new ArrayList<>();
    public static ArrayList<ServerThread> player = new ArrayList<>();

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
                al.add(st);
            } catch (Exception e) {
//				e.printStackTrace();
            }
        }
    }
}