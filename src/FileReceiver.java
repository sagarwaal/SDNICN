import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class FileReceiver implements Runnable {
	String filename;
	String path;
	String switchName;
	int switchPort;
	boolean flag;
	
	public  FileReceiver(String filename, String path, String switchName, int switchPort ) {
		// TODO Auto-generated constructor stub
		this.filename=filename;
		this.path=path;
		this.switchName=switchName;
		this.switchPort=switchPort;
		flag=false;
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			Socket sk= new Socket(switchName,switchPort);
			
			Packet pkt= new Packet();
			pkt.setType(MsgType.INTEREST);
			pkt.setData(filename);
			
			
			
			ObjectOutputStream oout=new ObjectOutputStream(sk.getOutputStream());
			oout.writeObject(pkt);
			oout.flush();
			
			
			
			ObjectInputStream oin= new ObjectInputStream(sk.getInputStream());
			
			
			Packet recvPkt=null;
			try {
				recvPkt = (Packet)oin.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(recvPkt==null || recvPkt.type!=MsgType.SUCCESS)
			{
				flag=false;
				sk.close();
				return;
				
			}
			
			DataInputStream din =new DataInputStream(new BufferedInputStream(sk.getInputStream()));
			
			FileOutputStream fos = new FileOutputStream(path);
			
			byte[] arr = new byte[1024*1024];
			
			int n;
			while((n=din.read(arr))!=-1)
			{
				fos.write(arr,0,n);
			}
			
			
			sk.close();
			flag=true;
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
