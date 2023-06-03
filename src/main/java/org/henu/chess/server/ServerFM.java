
package org.henu.chess.server;

import java.net.*;

/**
 * 单例类
 */
public class ServerFM {
	private ServerFM() {
		
	}
	private static final ServerFM fm = new ServerFM();
	//只返回一个对象
	public static ServerFM getFrameManger() {
		return fm;
	}

	//设置那个界面
	ServerMain sg;
	public void setFrame(ServerMain sg) {
		this.sg = sg;
	}


	ServerSocket server = null;
	ServerStart ss = null;

	//创建服务器
	public boolean CreateServer(int PORT) {
		try {
			//开启服务器端口
			server = new ServerSocket(PORT);
			ss = new ServerStart(server);
			ss.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean CloseServer() {
		try {
			//开启服务器端口
			server.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setTextArea(String str) {
		sg.textArea.append(str+"\r\n");
	}

	//根据线程名获取线程
	public ServerThread getThreadByName(String name) {
		for(int i=0;i < ServerStart.al.size();i++) {
			if(ServerStart.al.get(i).getName().equals(name)) {
				return ServerStart.al.get(i);
			}
		}
		return null;
	}
}
