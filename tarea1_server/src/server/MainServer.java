package server;

import java.util.Scanner;

public class MainServer implements server.Receiver.OnMessageListener {

	public static void main( String[] args) {
		
		MainServer serverexe= new MainServer();
		
		
	}

	public MainServer() {
		TCPConnection connection= TCPConnection.getInstance().setPuerto(5555);
		connection.setMain(this);
		connection.waitForConnection();		
		Scanner scan = new Scanner(System.in);
		while(true) {
			String line =scan.nextLine();
			connection.sendMessage(line);
		}
	}

	@Override
	public void onMessaage(String msg) {
		System.out.println(">> " + msg);
		
	}
	
	



}
