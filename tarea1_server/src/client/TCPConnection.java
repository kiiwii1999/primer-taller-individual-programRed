package client;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import client.Receiver.OnMessageListener;





public class TCPConnection {

	private OnMessageListener main;
	
	private ServerSocket server;
	private Socket socket;
	private int puerto;
	private String ip;
	
	private Sender sender;
	private Receiver receiver;
	
	private static TCPConnection  instance;
	
	private  TCPConnection() {}
	
	public static TCPConnection getInstance() {
		return (instance == null)? instance = new TCPConnection():instance;
	}
	
	public void setIp(String ip) {
		this.ip=ip;
	}
	
	
	public void setMain(OnMessageListener main) {
		this.main = main;
	}
	
	
	public static TCPConnection setPuerto(int puerto) {
		instance.puerto = puerto;
		return instance;
	}
	
	public static TCPConnection waitForConnection() {
		try {
			instance.server= new ServerSocket(instance.puerto);
			
			System.out.println("Esperando conexion...");
			instance.socket = instance.server.accept();
			System.out.println("Conexion aceptada");
			instance.initReaderAndWriter();
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return instance;
	}
	
	public void requestConnection() {
		try {
			
			System.out.println("Solicitando Conexion...");
			socket= new Socket(ip,puerto);
			System.out.println("Conexion aceptada");
			initReaderAndWriter();
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initReaderAndWriter() {
		
			try {
				sender = new Sender(socket.getOutputStream());
				receiver = new Receiver(socket.getInputStream());
				receiver.start();
				receiver.setListener(main);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}

	
	
	
	// metodo de envio
	public void sendMessage(String message) {
		sender.sendMessage(message);
	}
	
	
	
	
	
	
		
}
