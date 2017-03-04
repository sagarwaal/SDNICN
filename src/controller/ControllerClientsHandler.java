package controller;

import java.net.Socket;

import util.Packet;

public class ControllerClientsHandler implements Runnable {
	
	Socket sk;
	String switchAddr;
	Controller controller;
	
	public ControllerClientsHandler(Socket sk,Controller controller)
	{
		this.sk=sk;
		switchAddr=sk.getRemoteSocketAddress().toString();
		this.controller=controller;
	}
	
	
	
	
	
	public void handlePublishPackets(Packet pkt)
	{
		String host_addr=pkt.addr;
		
		
		
		
	}
	
	
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
