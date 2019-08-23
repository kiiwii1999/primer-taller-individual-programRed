package server;

import java.util.Scanner;

import server.TCPConnection.OnMessageListener;

public class MainServer implements OnMessageListener {

	public static void main( String[] args) {
		
		MainServer serverexe= new MainServer();
		
		
	}

	public MainServer() {
		TCPConnection connection= TCPConnection.getInstance().setPuerto(5555).waitForConnection();
		connection.setListener(this);
		connection.listenToMessage();
		Scanner scan = new Scanner(System.in);
		while(true) {
			String line =scan.nextLine();
			connection.sendMessage(line);
		}
	}
	
	@Override
	public void onMessage(String msg) {
		System.out.println(">>"+msg);
		
	}




}
