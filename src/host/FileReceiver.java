package host;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import util.MsgType;
import util.Packet;


public class FileReceiver implements Runnable {
	private String filename;
	private String path;
	private String switchName;
	private int switchPort;
	private boolean flag;
	
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
			pkt.type=MsgType.INTEREST;
			pkt.data=filename;
			
			
			
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
				oout.close();
				sk.close();
				System.out.println("Error: File "+filename+" not received");
				return;
				
			}
			
			//DataInputStream din =new DataInputStream(new BufferedInputStream(sk.getInputStream()));
			
			FileOutputStream fos = new FileOutputStream(path);
			
			byte[] arr=new byte[1024];
			
			long size=Integer.parseInt(recvPkt.data);
			long chunks= size/1024;
			int lastChunk=(int)(size-(chunks*1024));
			for (long i = 0; i < chunks; i++) {
			    oin.read(arr);
			    fos.write(arr);
			}
			
			oin.read(arr,0,lastChunk);
			
			fos.write(arr);
			
			
			fos.close();
			oout.close();
			sk.close();
			
			
						
		
			flag=true;
			System.out.println("File: "+filename+" (Size:"+size+") successfully received");
			
			
			
			
			
	} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
		
	}
	
	public boolean isFileReceived()
	{
		return flag;
	}
	

}
