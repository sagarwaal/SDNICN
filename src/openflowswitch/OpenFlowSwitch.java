package openflowswitch;

import util.ListeningPorts;

public class OpenFlowSwitch {
	
	
	SwitchServerThread server;
	ControllerHandler cHandler;
	
	String controllerIp;
	int controllerPort;
	
	
	public OpenFlowSwitch(String controllerIp,int controllerPort)
	{
		this.controllerIp=controllerIp;
		this.controllerPort=controllerPort;
		//this.switchPort=ListeningPorts.SWITCH_PORT;
		cHandler=new ControllerHandler(controllerIp, controllerPort);
		System.out.println("Starting server thread for switch");
		server=new SwitchServerThread(ListeningPorts.SWITCH_PORT, cHandler);
		
		Thread th=new Thread(server);
		th.start(); 
		
	}
	
	
}
