import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class HostThread implements Runnable {
	
	int serverPort;
	protected boolean isStopped = false;
    protected Thread runningThread= null;
	
	ServerSocket sk=null;
	public HostThread(int port)
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
		
		while(true)
		{
			try {
				Socket clientSocket = sk.accept();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		
	}

}
