package controller;

import java.io.EOFException;
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
		}
		catch(EOFException e)
		{
			
		}
		catch (IOException e) {
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
	
	public void closeStreams() throws EOFException,IOException
	{
		
		//oout.close();
		oin.close();
		sk.close();
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
				
				
			}catch(EOFException e)
			{
				System.err.append("Connection lost with client "+sk.getInetAddress().getHostAddress()+"\n");
				controller.delNode(switchAddr);
				break;
			}
			
			catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			closeStreams();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
