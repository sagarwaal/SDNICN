package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ControllerServer implements Runnable{
	
	
	ServerSocket sk;
	Controller controller;
	
	
	public ControllerServer(int port,Controller controller)
	{
		this.controller=controller;
		
		try {
			sk=new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void run() {
		
		Socket clientSocket;
		while(true)
		{
			try {
				clientSocket=sk.accept();
				Thread th=new Thread(new ControllerClientsHandler(clientSocket, controller));
				th.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		// TODO Auto-generated method stub
		
	}

}
