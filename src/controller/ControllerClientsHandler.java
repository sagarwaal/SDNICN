package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import util.MsgType;
import util.Packet;

public class ControllerClientsHandler implements Runnable {
	
	Socket sk;
	String switchAddr;
	Controller controller;
	ObjectInputStream oin;
	ObjectOutputStream oout;
	
	public ControllerClientsHandler(Socket sk,Controller controller)
	{
		this.sk=sk;
	
		switchAddr=sk.getInetAddress().getHostAddress();
		this.controller=controller;
		
		try {
			initializeStreams();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void initializeStreams() throws IOException
	{
		oout=new ObjectOutputStream(sk.getOutputStream());
		oout.flush();
		oin=new ObjectInputStream(sk.getInputStream());
		
	}
	
	
	
	public void handlePublishPackets(Packet pkt)
	{
		controller.addContent(pkt.data,pkt.addr,switchAddr);
	}
	
	public void handleInterestPackets(Packet pkt) throws IOException
	{
		String addr=controller.getManager().getBestPath(controller.getContent(pkt.data), pkt.addr, switchAddr);
		
		Packet repPkt=new Packet();
		
		repPkt.type=(addr==null)?MsgType.NOTFOUND:MsgType.SUCCESS ;
		repPkt.data=(addr==null)?null:addr.split(":")[1];
		repPkt.addr=(addr==null)?null:addr.split(":")[0];
		
		
		oout.writeObject(repPkt);
		oout.flush();
		
			
	}
	
	
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(sk.isConnected())
		{
			try {
				Packet pkt=(Packet) oin.readObject();
				
				switch(pkt.type)
				{
					case INTEREST:
						handleInterestPackets(pkt);
						break;
					case PUBLISH:
						handlePublishPackets(pkt);
						break;
				default:
					break;
				}
				
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				controller.delNode(switchAddr);
			}
		}
		
	}
	
	

}
