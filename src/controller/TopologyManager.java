package controller;
import java.util.Iterator;
import java.util.List;

import util.ContentInfo;
import util.ListeningPorts;
public class TopologyManager {

	
	
	public String getBestPath(List<ContentInfo>nodes,String hostAddr,String switchAddr )
	{
		
		/*
		 * Modify this method for computing shortest Path
		 */
		
		if(nodes==null)
			return null;
		
		
		ContentInfo info;
		
		
		
		
		for(Iterator<ContentInfo> iter=nodes.iterator();iter.hasNext();)
		{
			info=iter.next();
			
			if(info.getSwitchAddr()==switchAddr)
				return info.getHostAddr()+":"+ListeningPorts.HOST_PORT;
		}
		
		
		
		return nodes.get(0).getSwitchAddr()+":"+ListeningPorts.SWITCH_PORT;
	}
	
	
	
	
	
	
	
}
