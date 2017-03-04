package util;

public class ContentInfo {

	private String switchAddr;
	private String hostAddr;
	
	
	public ContentInfo(String switchAddr, String hostAddr)
	{
		this.switchAddr=switchAddr;
		this.hostAddr=hostAddr;
	}
	
	
	
	
	
	
	public String getSwitchAddr() {
		return switchAddr;
	}






	public String getHostAddr() {
		return hostAddr;
	}






	public boolean equals(Object obj){
		
		if(!(obj instanceof ContentInfo))
			return false;
		
		ContentInfo rhs=(ContentInfo)obj;
		
		return switchAddr.equals(rhs.switchAddr) && hostAddr.equals(rhs.hostAddr) ;
		
		
			
	}
	
	
}
