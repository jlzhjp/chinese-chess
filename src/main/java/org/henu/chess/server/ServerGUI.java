
package org.henu.chess.server;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.*;
import java.net.*;

public class ServerGUI extends JFrame {

	private JPanel contentPane;
	public JPanel panel;
	public JLabel lblNewLabel;
	public JTextPane textPane;
	public JScrollPane scrollPane;
	public JTextArea textArea;
	public JButton btnNewButton;
	public JButton btnNewButton_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FlatLightLaf.setup();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
					ServerFM.getFrameManger().setFrame(frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setTitle("服务器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 435);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u914D\u7F6E\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 766, 70);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("端口号：");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel.setBounds(28, 22, 80, 27);
		panel.add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setText("8000");
		textPane.setFont(new Font("宋体", Font.PLAIN, 16));
		textPane.setBounds(101, 22, 122, 27);
		panel.add(textPane);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new TitledBorder(null, "\u65E5\u5FD7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setBounds(20, 90, 756, 298);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);//不可编辑
		
		btnNewButton = new JButton("开启服务器");
		btnNewButton.setBounds(327, 17, 122, 38);
		btnNewButton.addActionListener(new BtnStartActionListener());
		panel.add(btnNewButton);
		
		btnNewButton_1 = new JButton("关闭服务器");
		btnNewButton_1.setBounds(514, 17, 122, 38);
		btnNewButton_1.addActionListener(new BtnCloseActionListener());
		panel.add(btnNewButton_1);
		
		
	}
	
	private class BtnStartActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			int PORT = Integer.parseInt(textPane.getText().trim());
				if(ServerFM.getFrameManger().CreateServer(PORT)) {
					ServerFM.getFrameManger().setTextArea("服务器启动......");
					btnNewButton.setEnabled(false);
				}else {
					ServerFM.getFrameManger().setTextArea("服务器启动失败......");
				}
				
		}
	}
	private class BtnCloseActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//关闭服务
				if(ServerFM.getFrameManger().CloseServer()) {
					btnNewButton.setEnabled(true);
					ServerFM.getFrameManger().setTextArea("服务器关闭......");
					for(int i=0;i<ServerStart.al.size();i++) {
						ServerStart.al.get(i).closeSocket();
					}
					ServerStart.al.clear();
				}else {
					ServerFM.getFrameManger().setTextArea("服务器关闭失败！");
				}
		}
	}
}//类
