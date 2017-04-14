package openflowswitch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import util.Packet;

public class ControllerHandler {
	
	String controllerIp;
	int controllerPort;
	Socket controllerSocket;
	
	ObjectInputStream oin;
	ObjectOutputStream oout;
	
	Object sharedLock;
	
	public ControllerHandler(String ip,int port)
	{
		controllerIp=ip;
		controllerPort=port;
		initialize();
		System.out.println("Initialization of switch controller connection completed");
		sharedLock=new Object();
	}
	
	
	
	
	
	public void initialize()
	{
		try {
			controllerSocket=new Socket(controllerIp,controllerPort);
			System.out.println("Switch connected to controller "+controllerSocket.getLocalPort());
			oout=new ObjectOutputStream(controllerSocket.getOutputStream());
			oout.flush();
			oin=new ObjectInputStream(controllerSocket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(Packet pkt) throws IOException
	{
		synchronized(sharedLock){
			oout.writeObject(pkt);
			oout.flush();
		}
	}
	
	public Packet sendReceive(Packet pkt) throws IOException, ClassNotFoundException
	{
		Packet recvMsg=null;
		synchronized(sharedLock)
		{
			oout.writeObject(pkt);
			oout.flush();
			
			recvMsg=(Packet)oin.readObject();
			
		}
		
		return recvMsg;
	}
	
	protected void finalize() throws Throwable
	{
		
		try{
			oout.close();
			controllerSocket.close();
		}
		finally
		{
			super.finalize();
		}
		
	}
	
	
	
	
	

}
