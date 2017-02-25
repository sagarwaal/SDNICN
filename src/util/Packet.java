package util;


public class Packet {
	
	private String id;
	public MsgType type;
	public String data;
	public String addr;
	
	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	
}
