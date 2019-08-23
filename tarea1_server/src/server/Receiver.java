package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Receiver extends Thread {

	private InputStream is;
	private boolean isAlive = true;
	private BufferedReader breader;
	
	private OnMessageListener listener;
	
	public Receiver(InputStream is){
		this.is = is;
		breader = new BufferedReader(new InputStreamReader(is));
	}
	
	@Override
	public void run() {
		try {
			while(isAlive) {
				System.out.println("Esperando mensaje...");
				String line= breader.readLine();
				if(listener!=null) listener.onMessaage(line);
		
		
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void receiveFile(String path) {
		try {
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
	
	public interface OnMessageListener{
		public void onMessaage(String msg);
	}
	
	public void setListener(OnMessageListener listener) {
		this.listener = listener;
	}

}
