package openflowswitch;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import util.ListeningPorts;
import util.MsgType;
import util.Packet;

public class SwitchClientsHandler implements Runnable{

	Socket sk;
	String hostAddr;
	ObjectInputStream oin;
	ObjectOutputStream oout;
	//DataInputStream din;
	//DataOutputStream dout;
	ControllerHandler cHandler;
	
	public SwitchClientsHandler(Socket sk, ControllerHandler cH) {
		// TODO Auto-generated constructor stub
		this.sk=sk;
		cHandler=cH;
		initializeStreams();
		hostAddr=sk.getInetAddress().getHostAddress();
		
	}
	
	public void initializeStreams()
	{
		try {
			oout=new ObjectOutputStream(sk.getOutputStream());
			oout.flush();
			oin=new ObjectInputStream(sk.getInputStream());
			
			
		}catch(EOFException e)
		{
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void handlePacket(Packet pkt) throws IOException, ClassNotFoundException
	{
		switch(pkt.type)
		{
			case PUBLISH:
				handlePublishPacket(pkt);
				break;
			
			case INTEREST:
				handleInterestPacket(pkt);
				break;
				
			default:
				break;
				
					
		}
	}
	
	public void handlePublishPacket(Packet pkt) throws IOException
	{
		Packet p=new Packet();
		p.type=MsgType.PUBLISH;
		p.addr=hostAddr;
		p.data=pkt.data;
		cHandler.send(p);
		
	}
	
	public void handleInterestPacket(Packet pkt) throws ClassNotFoundException,EOFException, IOException
	{
		Packet p= new Packet();
		p.type=MsgType.INTEREST;
		p.addr=hostAddr;
		p.data=pkt.data;
		
		Packet recvMsg=cHandler.sendReceive(p);  //packet received from controller
		
		if(recvMsg.type==MsgType.NOTFOUND)
		{
			oout.writeObject(new Packet(MsgType.NOTFOUND, null));
			oout.flush();
		}
		else
		{
			String hostAddr=recvMsg.addr;
			
			int hostPort;
			if(recvMsg.data!=null)
				hostPort=Integer.parseInt(recvMsg.data);
			else
				hostPort=ListeningPorts.HOST_PORT;
			
			String filename=pkt.data;
			
			recvAndFwdFile(hostAddr, hostPort, filename);
			
			
		}
		
	}
	
	public void recvAndFwdFile(String hostAddr, int hostPort, String filename) throws UnknownHostException,EOFException, IOException, ClassNotFoundException
	{
		Socket host_sk=new Socket(hostAddr,hostPort);
		ObjectOutputStream host_oout=new ObjectOutputStream(host_sk.getOutputStream());
		host_oout.flush();
		ObjectInputStream host_oin=new ObjectInputStream(host_sk.getInputStream());
		
		
		
		host_oout.writeObject(new Packet(MsgType.INTEREST,filename));
		host_oout.flush();
		
		Packet pkt=(Packet)host_oin.readObject();
		
		if(pkt==null||pkt.type!=MsgType.SUCCESS)
		{
			oout.writeObject(new Packet(MsgType.NOTFOUND,null));
			oout.flush();
			
		}
		else
		{
			oout.writeObject(new Packet(MsgType.SUCCESS,pkt.data));
			oout.flush();
			
			byte[] arr = new byte[1024];
			
			
			long size=Integer.parseInt(pkt.data);
			
			
			long chunks= size/1024;
			int lastChunk=(int)(size-(chunks*1024));
			for (long i = 0; i < chunks; i++) {
			    host_oin.read(arr);
				
				
			    oout.write(arr);
			}
			host_oin.read(arr,0,lastChunk);
			oout.write(arr);
			
			
			host_oout.flush();
			oout.flush();
			
			System.out.println("File written");
			
		}
		host_oin.close();
		//host_oout.close();
		host_sk.close();
	}
	
	public void closeStreams() throws EOFException, IOException
	{
		
		//oout.close();
		oin.close();
		sk.close();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(sk.isConnected())
		{
			try {
				Packet pkt=(Packet)oin.readObject();
				handlePacket(pkt);
				
			}catch (EOFException e)
			{
				System.err.append("Connection lost with "+sk.getInetAddress().getHostAddress()+"\n");
				break;
			}
			
			catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		try {
			closeStreams();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void finalize() throws Throwable
	{
		closeStreams();
		super.finalize();
	}

}
