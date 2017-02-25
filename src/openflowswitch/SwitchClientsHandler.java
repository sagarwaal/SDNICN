package openflowswitch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import util.MsgType;
import util.Packet;

public class SwitchClientsHandler implements Runnable{

	Socket sk;
	String hostAddr;
	ObjectInputStream oin;
	ObjectOutputStream oout;
	ControllerHandler cHandler;
	
	public SwitchClientsHandler(Socket sk, ControllerHandler cH) {
		// TODO Auto-generated constructor stub
		this.sk=sk;
		cHandler=cH;
		initializeStreams();
		hostAddr=sk.getRemoteSocketAddress().toString();
		
	}
	
	public void initializeStreams()
	{
		try {
			oin=new ObjectInputStream(sk.getInputStream());
			oout=new ObjectOutputStream(sk.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void handlePacket(Packet pkt) throws IOException
	{
		switch(pkt.type)
		{
			case PUBLISH:
				Packet p=new Packet();
				p.type=MsgType.PUBLISH;
				p.addr=hostAddr;
				p.data=pkt.data;
				cHandler.send(p);
				break;
				
			default:
				break;
				
					
		}
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
