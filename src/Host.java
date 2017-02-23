/* 
 * check database for all files, publish their name,
 * start HostServer thread to listen file requests 
 */



import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;



public class Host {
	
	final int HOST_PORT=9695;
	
	Socket switchSocket;
	ServerSocket listenSocket;
	
	
	DataOutputStream switchDout;
	String switchIp;
	int switchPort;
	
	
	public Host(String ip,int port)
	{
		switchIp=ip;
		switchPort=port;
		
		try {
			switchSocket=new Socket(ip,port);
			listenSocket=new ServerSocket(HOST_PORT);
			
			switchDout=new DataOutputStream(switchSocket.getOutputStream());
		
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		publishAll();
		
	}
	
	
	
	public void publish(String filename)
	{
		Packet pkt= new Packet();
		pkt.setType(MsgType.PUBLISH);
		pkt.setData(filename);
		
		
		try {
			
			switchDout.write(Serializer.serialize(pkt));
			switchDout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void putFile(String name, String path )
	{
		DatabaseHandler.addFile(name, path);
		
		publish(name);
	}
	
	public void getFile(String name,String path)
	{
		FileReceiver fr= new FileReceiver(name, path, switchIp, switchPort);
		
		Thread th= new Thread(fr);
		th.start();
	}
	
	
	public void publishAll()
	{
		ArrayList<String> fileList=DatabaseHandler.getFileList();
		
		for(int i=0;i<fileList.size();i++)
		{
			publish(fileList.get(i));
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	public void connect()
	{
		
	}
	

}
