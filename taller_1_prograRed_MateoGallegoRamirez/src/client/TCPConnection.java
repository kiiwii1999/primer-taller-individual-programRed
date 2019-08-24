package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;



public class TCPConnection {
	
	public static final String REMOTE_IPCONFIG = "remoteIpconfig";
	public static final String INTERCFACE = "interface";
	public static final String WHAT_TIME_IS_IT = "whatTimeIsIt";
	public static final String RTT = "RTT";
	public static final String SPEED = "speed";
	
	
	private ServerSocket server;
	private Socket socket;
	private BufferedWriter bwriter;
	private BufferedReader breader;
	private int puerto;
	private String ip;
	
	private static TCPConnection  instance;
	
	private  TCPConnection() {}
	
	public static TCPConnection getInstance() {
		return (instance == null)? instance = new TCPConnection():instance;
	}
	
	public void setIp(String ip) {
		this.ip=ip;
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
			breader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bwriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	public void listenToMessage() {
		
		Thread h = new Thread(() -> {
			try {
				while(true) {
					System.out.println("Esperando mensaje...");
					String line= breader.readLine();
					if(line.equals(REMOTE_IPCONFIG)) {
//						System.out.println(":. se pidio la ip");
						sendMessage(InetAddress.getLocalHost().getHostAddress());
//						System.out.println(":. se envio la ip: " + Comand);
					}else if(line.equals(INTERCFACE)) {
						sendMessage(getInterfaces());
					}else if(line.equals(WHAT_TIME_IS_IT)) {
						sendMessage(getHour());
					}
					if(listener!=null) listener.onMessage(line);
			
			
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}}) ;
		h.start();
		
		
	}
	
	
	private String getInterfaces() {
		String line ="";
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			boolean stop = false;
			
			while(interfaces.hasMoreElements() && !stop) {
				NetworkInterface interN = interfaces.nextElement();
				if (interN.isUp()) {
					line = line + interN.getName();
					stop = true;
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return line;
	}
	
	
	private String getHour() {
		int seg, min,hora;
		Calendar time = new GregorianCalendar();
		seg = time.get(Calendar.SECOND);
		min = time.get(Calendar.MINUTE);
		hora = time.get(Calendar.HOUR);
		
		return seg + " : " +min + " : " + hora;
	}

	public void sendMessage(String msg) {
		new Thread(
				()-> {
					
					try {
						System.out.println("Enviando mensaje...");
						
						bwriter.write(msg+"\n");
						bwriter.flush();
							
						System.out.println("Mensaje enviado");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				).start();
		
	}
	
	public void sendFile(String path) {
		File f= new File(path);
		try {
			FileInputStream fis = new FileInputStream(f);
			OutputStream os = socket.getOutputStream();
			
			int bytesLeidos=0;
			byte[]buffer = new byte[512];
			while((bytesLeidos =fis.read(buffer))!= -1) {
				os.write(buffer, 0,bytesLeidos);
			}
			fis.close();
			os.close();
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void receiveFile(String path) {
		try {
			InputStream is= socket.getInputStream();
			FileOutputStream fos= new FileOutputStream(new File(path));
			
			int bytesLeidos=0;
			byte[]buffer = new byte[900];
			while((bytesLeidos =is.read(buffer))!= -1) {
				fos.write(buffer, 0,bytesLeidos);
			}
			fos.close();
			is.close();
			System.out.println(System.currentTimeMillis());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	//PATRON OBSERVER
	private OnMessageListener listener;
	public interface OnMessageListener{
		
		public  void onMessage(String msg);
	}

	public void setListener(OnMessageListener listener) {
		this.listener=listener;
	}
	
	
}
