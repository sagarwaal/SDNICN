package host;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class HostServerThread implements Runnable {
	
	int serverPort;
	protected boolean isStopped = false;
    protected Thread runningThread= null;
	
	ServerSocket sk=null;
	public HostServerThread(int port)
	{
		serverPort=port;
		
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
		
		openServerSocket();
		Socket clientSocket;
		while(true)
		{
			
			try {
				clientSocket = sk.accept();
				new HostClientsHandler(clientSocket).run(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
				
			
		}
		
		
	}

}
