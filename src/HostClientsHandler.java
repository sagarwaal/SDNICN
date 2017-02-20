import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class HostClientsHandler implements Runnable{
	
	
	Socket connSocket;
	
	public HostClientsHandler(Socket sk) {
		// TODO Auto-generated constructor stub
		connSocket=sk;
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			ObjectInputStream oin=new ObjectInputStream(connSocket.getInputStream());
			
			while(true)
			{
				Packet pkt=(Packet)oin.readObject();
				
				switch(pkt.type)
				{
					
					case INTEREST :
						
						//string filepath 
						
						break;
						
				
					default:
					break;
						
					
					
				}
				
				
			}
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
