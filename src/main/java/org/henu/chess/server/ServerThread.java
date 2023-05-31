
package org.henu.chess.server;

import java.net.*;
import java.util.ArrayList;

import javax.swing.JTextArea;

import java.io.*;

public class ServerThread extends Thread{
	Socket socket;
	BufferedReader br = null;
	PrintWriter pw = null;
//	public static ArrayList <String>ul = new ArrayList<>();
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")));
			pw.println("登陆服务器成功！");
			pw.flush();
			String str = "";
			
			while((str = br.readLine())!=null) {
				String [] commands = str.split("\\|");
				if(commands[0].equals("OFFLINE")) {
					ServerFM.getFrameManger().setTextArea(socket.getInetAddress().getHostAddress()+"退出访问");
					ServerStart.al.remove(this);//synchronized
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread d = ServerStart.al.get(i);
						d.pw.println("DEL"+"|"+this.getName());//退出则返回0
						d.pw.flush();
					}
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread r = ServerStart.al.get(i);
						r.pw.println("MSG"+"|"+"系统消息"+"|"+this.getName()+"退出");
						r.pw.flush();
					}
					pw.println("OFFLINE");//退出则返回0
					pw.flush();
					break;
				}else if(commands[0].equals("LOGIN")) {
					//发送给新登录的客户端所有用户名
					this.setName(commands[1]);
					ServerFM.getFrameManger().setTextArea(socket.getInetAddress().getHostAddress()+"访问，用户名为："+this.getName());
//					ul.add(commands[1]);
					String susername = "USERLISTS";
//					System.out.println(ServerStart.al.size());
//					if(ServerStart.al.size()>0) {
						for(int i=0;i<ServerStart.al.size();i++) {
							susername = susername + "|" +ServerStart.al.get(i).getName();
						}
						pw.println(susername);
						pw.flush();
						for(int i=0;i<ServerStart.chess.length;i++) {
							for(int j=0;j<ServerStart.chess[0].length;j++) {
								pw.println("CHESS"+"|"+i+"|"+j+"|"+ServerStart.chess[i][j]);
								pw.flush();
							}
						}
//					}
					//发送原来客户端新登陆的用户名
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerStart.al.get(i).pw.println("ADD"+"|"+this.getName());
						ServerStart.al.get(i).pw.flush();
					}
					ServerStart.al.add(this);
					if(ServerStart.player.size() == 2) {
						pw.println("LP");
						pw.flush();
					}
				}else if(commands[0].equals("MSG")) {
					String SendName = commands[1];
					String ReciName = commands[2];
					String info = commands[3];
					if(ReciName.equals("ALL")) {
						for(int i=0;i<ServerStart.al.size();i++) {
							ServerThread r = ServerStart.al.get(i);
							if(r.getName().equals(SendName))
								continue;
							r.pw.println("MSG"+"|"+SendName+"|"+info);
							r.pw.flush();
							ServerFM.getFrameManger().setTextArea(SendName+"发送消息["+info+"]到"+ReciName);
						}
					}else {
						ServerThread rClient = ServerFM.getFrameManger().getThreadByName(ReciName);
						rClient.pw.println("MSG"+"|"+SendName+"|"+info);
						rClient.pw.flush();
						ServerFM.getFrameManger().setTextArea(SendName+"发送消息["+info+"]到"+ReciName);
					}
				}else if(commands[0].equals("CHESS")) {
					String SendName = commands[1];
					String x = commands[2];
					String y = commands[3];
					String B_or_W = commands[4];
					ServerStart.chess[Integer.parseInt(x)][Integer.parseInt(y)] = Integer.parseInt(B_or_W);
					if(B_or_W.equals("1")) {
						pw.println("OLDER"+"|"+"0");
						pw.flush();
						for(int i=0;i<ServerStart.player.size();i++) {
							ServerThread s = ServerStart.player.get(i);
							if(s.getName().equals(SendName))
								continue;
							s.pw.println("OLDER"+"|"+"1");
							s.pw.flush();
						}
						ServerFM.getFrameManger().setTextArea(SendName+"在["+(Integer.parseInt(x)+1)+"]行 ["+(Integer.parseInt(y)+1)+"]列放置黑棋");
					}
					if(B_or_W.equals("2")) {
						pw.println("OLDER"+"|"+"0");
						pw.flush();
						for(int i=0;i<ServerStart.player.size();i++) {
							ServerThread s = ServerStart.player.get(i);
							if(s.getName().equals(SendName))
								continue;
							s.pw.println("OLDER"+"|"+"1");
							s.pw.flush();
						}
						ServerFM.getFrameManger().setTextArea(SendName+"在["+(Integer.parseInt(x)+1)+"]行 ["+(Integer.parseInt(y)+1)+"]列放置白棋");
					}
						
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread r = ServerStart.al.get(i);
						if(r.getName().equals(SendName)) {
							continue;
						}
						r.pw.println("CHESS"+"|"+x+"|"+y+"|"+B_or_W);
						r.pw.flush();
					}
				}else if(commands[0].equals("PLAY")) {
					if(ServerStart.chessnum%2 == 0) {
						ServerStart.chessnum++;
						ServerStart.player.add(this);
						pw.println("PLAY"+"|"+"1"+"|"+commands[1]);
						pw.flush();
						ServerFM.getFrameManger().setTextArea(commands[1]+"准备游戏分配黑子先手");
					}else if(ServerStart.chessnum%2 == 1) {
						ServerStart.chessnum++;
						ServerStart.player.add(this);
						pw.println("PLAY"+"|"+"2"+"|"+commands[1]);
						pw.flush();
						ServerFM.getFrameManger().setTextArea(commands[1]+"准备游戏分配白字子后手");
					}
				}else if(commands[0].equals("NOPLAY")){
					ServerStart.player.remove(this);
					if(commands[2].equals("1"))
						ServerStart.chessnum = 0;
					if(commands[2].equals("2"))
						ServerStart.chessnum = 1;
					ServerFM.getFrameManger().setTextArea(commands[1]+"取消准备");
				}else if(commands[0].equals("OVER")){
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread r = ServerStart.al.get(i);
						r.pw.println("OVER"+"|"+commands[1]);
						r.pw.flush();
					}
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread r = ServerStart.al.get(i);
						r.pw.println("MSG"+"|"+"系统消息"+"|"+this.getName()+"退出，"+commands[1]+"获胜");
						r.pw.flush();
					}
					ServerStart.player.clear();
					for(int i=0;i<ServerStart.chess.length;i++) {
						for(int j=0;j<ServerStart.chess[0].length;j++) {
							ServerStart.chess[i][j] = 0;
						}
					}	
				}else if(commands[0].equals("PSIZE")) {
					if(ServerStart.player.size()==2) {
						for(int i=0;i<ServerStart.player.size();i++) {
							ServerStart.player.get(i).pw.println("LPP");
							ServerStart.player.get(i).pw.flush();
						}
						for(int i=0;i<ServerStart.al.size();i++) {
							ServerStart.al.get(i).pw.println("LP");
							ServerStart.al.get(i).pw.flush();
						}
					}
					pw.println("PSIZE"+"|"+ServerStart.player.size());
					pw.flush();
					ServerFM.getFrameManger().setTextArea(commands[1]+"请求玩家数量");
				}else if(commands[0].equals("RS")){
					ServerStart.player.remove(this);
					ServerStart.al.remove(this);
					String s = null;
					for(int i=0;i<ServerStart.player.size();i++) {
						s = ServerStart.player.get(i).getName();
					}
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread r = ServerStart.al.get(i);
						r.pw.println("OVER"+"|"+s);
						r.pw.flush();
					}
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerThread r = ServerStart.al.get(i);
						r.pw.println("MSG"+"|"+"系统消息"+"|"+this.getName()+"退出，"+s+"获胜");
						r.pw.flush();
					}
					ServerStart.player.clear();
					for(int i=0;i<ServerStart.chess.length;i++) {
						for(int j=0;j<ServerStart.chess[0].length;j++) {
							ServerStart.chess[i][j] = 0;
						}
					}
				}else {
					pw.println("命令无效！");
					pw.flush();
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(br != null)
					br.close();
				if(pw != null)
					pw.close();
				if(socket != null)
					socket.close();
//				ss.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void sendMsg(String str){
		pw.println(str);
		pw.flush();
	}
	public void closeSocket() {
		ServerFM.getFrameManger().setTextArea(socket.getInetAddress().getHostAddress()+"终止访问");
		pw.println("CLOSE");//退出则返回0
		pw.flush();
	}
	
}
