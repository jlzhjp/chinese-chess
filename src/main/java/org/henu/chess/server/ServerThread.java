
package org.henu.chess.server;

import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.Result;
import org.henu.chess.common.messages.request.CreateRoomRequest;
import org.henu.chess.common.messages.request.JoinRoomRequest;
import org.henu.chess.common.messages.response.CreateRoomResponse;
import org.henu.chess.common.messages.response.JoinRoomResponse;
import org.henu.chess.common.messages.response.StartGameResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread {
    Socket socket;
    BufferedReader br = null;
    PrintWriter pw = null;
//	public static ArrayList <String>ul = new ArrayList<>();

    public static Map<String,String> room = new HashMap<>();
    public static Integer roomNum = 10;  //并发量不高，房间号就限定。否则使用Math.Random()随机数


    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //接收客户端
            var receiver = new SocketMessageReceiver(socket);

            var listener = new MessageListener().on(CreateRoomRequest.class, (request) -> { //创建房间监听
                String roomID = roomNum++ + "";//随机不重复
                String username = request.getUserName();
                ServerFM.getFrameManger().setTextArea(username + "创建了房间，房间号为：" + roomID);

                //创建房间响应
                var response = new CreateRoomResponse();
                response.setResult(Result.SUCCESS);
                response.setRoomID(roomID);
                receiver.send(response);
            }).on(JoinRoomRequest.class, (request) -> {  //加入房间监听

                System.out.println("加入房间...");
                String roomID = request.getRoomID();
                String username = request.getUserName();
                ServerFM.getFrameManger().setTextArea(username + "加入了" + roomID + "号房间");

                //判断该房间是否已经有人，则可以开始游戏
                if (room.containsKey(roomID)) {
                    JoinRoomResponse response = new JoinRoomResponse();
                    response.setResult(Result.SUCCESS);
                    receiver.send(response);
                    //开始游戏
                    sendStart(receiver,roomID,username);
                } else {
                    //没有人则加入房间
                    room.put(roomID, username);
                    //加入房间响应
                    JoinRoomResponse response = new JoinRoomResponse();
                    response.setResult(Result.SUCCESS);
                    receiver.send(response);
                }
            });
            receiver.setListener(listener);
            receiver.listen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //各个客户端发送请求开始游戏
    public void sendStart(SocketMessageReceiver receiver,String roomID,String username){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
        }

        //给多个客户端 发送 开始游戏 消息
        ServerStart.al.forEach(st -> {
            try {
                //接收客户端
                var clientR = new SocketMessageReceiver(st.socket);

                if(clientR.equals(receiver)){
                    StartGameResponse sgResponse = new StartGameResponse();
                    sgResponse.setRedPlayerName(room.get(roomID));  //房主为红方
                    sgResponse.setBlackPlayerName(username);
                    clientR.send(sgResponse);
                }else{
                    StartGameResponse sgResponse = new StartGameResponse();
                    sgResponse.setRedPlayerName(username);  //后加入的为黑
                    sgResponse.setBlackPlayerName(room.get(roomID));
                    clientR.send(sgResponse);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendMsg(String str) {
        pw.println(str);
        pw.flush();
    }

    public void closeSocket() {
        ServerFM.getFrameManger().setTextArea(socket.getInetAddress().getHostAddress() + "终止访问");
        pw.println("CLOSE");//退出则返回0
        pw.flush();
    }

}
