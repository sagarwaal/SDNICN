package openflowswitch;

import util.ListeningPorts;

public class OpenFlowSwitch {
	
	
	SwitchServerThread server;
	ControllerHandler cHandler;
	
	String controllerIp;
	int controllerPort;
	int switchPort;
	
	public OpenFlowSwitch(String controllerIp,int controllerPort)
	{
		this.controllerIp=controllerIp;
		this.controllerPort=controllerPort;
		this.switchPort=ListeningPorts.SWITCH_PORT;
		initialize();
		new Thread(server).start(); 
	}
	
	public void initialize()
	{
		cHandler=new ControllerHandler(controllerIp, controllerPort);
		server=new SwitchServerThread(switchPort, cHandler);
		
	}

}
