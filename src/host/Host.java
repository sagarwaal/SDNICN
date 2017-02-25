package host;
/* 
 * check database for all files, publish their name,
 * start HostServer thread to listen file requests 
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import util.MsgType;
import util.Packet;



public class Host {
	
	final int HOST_PORT=9695;
	
	Socket switchSocket;
	
	HostServerThread serverThread;
	
	
	ObjectOutputStream switchOout;
	String switchIp;
	int switchPort;
	
	
	public Host(String ip,int port)
	{
		switchIp=ip;
		switchPort=port;
		
		initialize();
		publishAll();
		
	}
	
	public void initialize()
	{
		try {
			switchSocket=new Socket(switchIp,switchPort);
			serverThread=new HostServerThread(HOST_PORT); 
			switchOout=new ObjectOutputStream(switchSocket.getOutputStream());
			
			Thread th= new Thread(serverThread);
			th.start();
			
			
		
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
		pkt.type=MsgType.PUBLISH;
		pkt.data=filename;
		
		
		try {
			switchOout.writeObject(pkt);
			switchOout.flush();
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
	
	protected void finalize() throws Throwable
	{
		
		try{
			switchOout.close();
			switchSocket.close();
		}
		finally
		{
			super.finalize();
		}
		
	}

}
