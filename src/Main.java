import host.Host;

import java.util.Scanner;

import openflowswitch.OpenFlowSwitch;
import controller.Controller;
import util.ListeningPorts;


public class Main{
	
		private static Scanner sc;
		public static void main(String[] args)
		{
			System.out.println("Choose mode\n1) Controller\n2) Switch\n3) Host");
			sc=new Scanner(System.in);
			switch(sc.nextInt())
			{
				case 1:
					launchController();
					break;
				case 2:
					launchSwitch();
					break;
				case 3:
					launchHost();
					break;
				default:
					launchHost();
			}
	
		}
		
		public static void launchHost()
		{
			System.out.println("Enter ip of switch");
			String ip=sc.next();
			new Host(ip,ListeningPorts.SWITCH_PORT);
		}
		
		public static void launchController()
		{
			new Controller();
		}
		
		public static void launchSwitch()
		{
			System.out.println("Enter ip of controller");
			String ip=sc.next();
			new OpenFlowSwitch(ip,ListeningPorts.CONTROLLER_PORT);
		}
}
