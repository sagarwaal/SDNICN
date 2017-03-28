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
			System.out.println("Host server thread started "+sk.getInetAddress());
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
				System.out.println("New connection from "+clientSocket.getRemoteSocketAddress());
				new Thread(new HostClientsHandler(clientSocket)).start(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
				
			
		}
		
		
	}

}
