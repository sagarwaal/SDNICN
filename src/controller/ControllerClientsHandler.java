package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import util.ContentInfo;
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
		switchAddr=sk.getRemoteSocketAddress().toString();
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
		oin=new ObjectInputStream(sk.getInputStream());
		oout=new ObjectOutputStream(sk.getOutputStream());
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
		repPkt.data=(addr==null)?null:addr;
		
		oout.writeObject(repPkt);
		oout.flush();
		
			
	}
	
	
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true)
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
