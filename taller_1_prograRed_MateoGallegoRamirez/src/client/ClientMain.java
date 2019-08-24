package client;

import java.util.Scanner;

import client.TCPConnection.OnMessageListener;

public class ClientMain implements OnMessageListener{

	public ClientMain() {
		
		TCPConnection connection= TCPConnection.getInstance().setPuerto(5555);
		connection.setIp("127.0.0.1");
		connection.requestConnection();
		connection.setListener(this);
		connection.listenToMessage();
		Scanner scan = new Scanner(System.in);
		while(true) {
			String line =scan.nextLine();
			connection.sendMessage(line);
		}
	}
	
	public static void main(String[] args) {
		ClientMain c = new ClientMain();		
	}

	@Override
	public void onMessage(String msg) {
		System.out.println(">> " + msg);
		
	}
}
