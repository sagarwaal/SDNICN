package openflowswitch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SwitchServerThread implements Runnable{
	
	ServerSocket sk;
	int serverPort;
	
	ControllerHandler cHandler;
	
	public SwitchServerThread(int port,ControllerHandler cH) {
		// TODO Auto-generated constructor stub
		serverPort=port;
		cHandler=cH;
		
	}
	
	public void openServerSocket()
	{
		
		try {
			sk=new ServerSocket(serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		openServerSocket();
		System.out.println("Switch Server thread started "+sk.getLocalPort());
		Socket clientSocket;
		while(true)
		{
			
			try {
				clientSocket=sk.accept();
				
				new Thread( new SwitchClientsHandler(clientSocket,cHandler)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
