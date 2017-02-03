import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Host {
	
	final int HOST_PORT=9695;
	
	Socket switchSocket;
	ServerSocket listenSocket;
	
	
	
	public Host(String host,int port)
	{
		try {
			switchSocket=new Socket(host,port);
			listenSocket=new ServerSocket(HOST_PORT);
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void publish(String filename)
	{
		Packet pkt= new Packet();
		pkt.setType(MsgType.PUBLISH);
		pkt.setData(filename);
		
		DataOutputStream dout;
		try {
			dout = new DataOutputStream(switchSocket.getOutputStream());
			dout.write(Serializer.serialize(pkt));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void putFile(String uri, String path )
	{
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void connect()
	{
		
	}
	

}
