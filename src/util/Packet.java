package util;


public class Packet {
	
	private String id;
	public MsgType type;
	public String data;
	public String addr;
	
	public Packet()
	{
		
	}
	
	public Packet(MsgType type, String data)
	{
		this.type=type;
		this.data=data;
	}
	
	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	
}
