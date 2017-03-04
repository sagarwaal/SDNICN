package controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import util.ContentInfo;


public class Controller {
	
	HashMap<String,List<ContentInfo>> map;
	
	HashSet<String> delAddr;                    //to store deleted nodes
	
	
	public Controller()
	{
		map=new HashMap<String,List<ContentInfo>>();
		delAddr=new HashSet<String>();
		
	}
	
	
	public void addContent(String content,String switchAddr, String hostAddr)
	{
		
		if(delAddr.contains(switchAddr))
				delAddr.remove(switchAddr);
		
		if(delAddr.contains(hostAddr))
				delAddr.remove(hostAddr);
		
		
		if(!map.containsKey(content))
		{
			map.put(content, new LinkedList<ContentInfo>() );
		}
		
		map.get(content).add(new ContentInfo(switchAddr,hostAddr));
	
	}
	
	public void delNode(String addr)
	{
		delAddr.add(addr);
	}
	
	
	
	public ContentInfo getContent(String content)
	{
		if(!map.containsKey(content))
			return null;
		
		List<ContentInfo> l=map.get(content);
		
		ContentInfo info;
		
		
		/*
		 * if content is present, get first contentInfo, if its addr is not in deleted addresses set
		 * remove from first and add at last.(this will distribute load)
		 * 
		 * if addr of contentInfo is in delAddr list just delete it from list
		 */
		
		
		while(!l.isEmpty())
		{
			info=(ContentInfo)l.get(0);
			l.remove(0);
			
			if(!(delAddr.contains(info.getSwitchAddr()) || delAddr.contains(info.getHostAddr())))
			{
				l.add(info); 
				return info;
			}
		
		}
			
		return null;
	}
	
	
	
}
