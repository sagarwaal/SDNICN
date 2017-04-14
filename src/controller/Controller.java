package controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import util.ContentInfo;
import util.ListeningPorts;


/**
 * @author sagar
 *
 */
public class Controller {
	
	private ConcurrentHashMap<String,List<ContentInfo>> map;
	
	private Set<String> delAddr;   //to store deleted nodes
	
	private TopologyManager manager;
	
	public Controller()
	{
		map=new ConcurrentHashMap<String,List<ContentInfo>>();
		//delAddr=new HashSet<String>();
		
		delAddr=ConcurrentHashMap.newKeySet();
		manager=new TopologyManager();
		
		Thread th=new Thread(new ControllerServer(ListeningPorts.CONTROLLER_PORT,this));
		th.start();
		
	}
	
	
	public TopologyManager getManager()
	{
		return manager;
	}
	
	
	public void addContent(String content,String switchAddr, String hostAddr)
	{
		System.out.println("Publishing file " +content+ " Switch-"+switchAddr+" host-"+hostAddr);
		if(delAddr.contains(switchAddr))
			delAddr.remove(switchAddr);
	
		
		if(delAddr.contains(hostAddr))
			delAddr.remove(hostAddr);
		
		map.putIfAbsent(content,new LinkedList<ContentInfo>());
		map.get(content).add(new ContentInfo(switchAddr,hostAddr));
	
	}
	
	public void delNode(String addr)
	{
		delAddr.add(addr);
	}
	
	
	
	public List<ContentInfo> getContent(String content)
	{
		if(!map.containsKey(content))
			return null;
		
		List<ContentInfo> l=map.get(content);
		
		ContentInfo info;
		/*this loop is to be corrected*/
		for(Iterator<ContentInfo> iter=l.iterator();iter.hasNext();)
		{
			info=iter.next();
			/*if(!(delAddr.contains(info.getSwitchAddr()) || delAddr.contains(info.getHostAddr())))
			{
				iter.remove();
			}*/
		}
		return l;
	}
	
}
