package client;

import java.util.Scanner;

public class ClientMain implements client.Receiver.OnMessageListener{

	public ClientMain() {
		
		TCPConnection connection= TCPConnection.getInstance().setPuerto(5555);
		connection.setIp("127.0.0.1");
		connection.setMain(this);
		connection.requestConnection();
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
	public void onMessaage(String msg) {
		// TODO Auto-generated method stub
		System.out.println(">> " + msg);
	}
}
