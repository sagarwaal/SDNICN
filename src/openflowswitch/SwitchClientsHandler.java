package openflowswitch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	DataInputStream din;
	DataOutputStream dout;
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
			
			din=new DataInputStream(sk.getInputStream());
			dout=new DataOutputStream(sk.getOutputStream());
		} catch (IOException e) {
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
	
	public void handleInterestPacket(Packet pkt) throws ClassNotFoundException, IOException
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
	
	public void recvAndFwdFile(String hostAddr, int hostPort, String filename) throws UnknownHostException, IOException, ClassNotFoundException
	{
		Socket host_sk=new Socket(hostAddr,hostPort);
		ObjectInputStream host_oin=new ObjectInputStream(host_sk.getInputStream());
		ObjectOutputStream host_oout=new ObjectOutputStream(host_sk.getOutputStream());
		DataInputStream host_din=new DataInputStream(host_sk.getInputStream());
		DataOutputStream host_dout=new DataOutputStream(host_sk.getOutputStream());
		
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
			byte[] arr = new byte[1024*1024];
			
			int n;
			
			while((n=host_din.read(arr))!=-1)
			{
				dout.write(arr,0,n);
			}
			
			dout.flush();
		}
		host_oin.close();
		host_oout.close();
		host_din.close();
		host_dout.close();
		host_sk.close();
	}
	
	public void closeStreams() throws IOException
	{
		oin.close();
		oout.close();
		din.close();
		dout.close();
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(sk.isConnected())
		{
			try {
				Packet pkt=(Packet)oin.readObject();
				handlePacket(pkt);
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	protected void finalize() throws Throwable
	{
		closeStreams();
		super.finalize();
	}

}
