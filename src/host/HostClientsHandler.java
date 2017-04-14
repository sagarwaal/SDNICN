package host;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.*;

import util.MsgType;
import util.Packet;


public class HostClientsHandler implements Runnable{
	
	
	private Socket connSocket;
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	private InputStream in;
	
	
	public HostClientsHandler(Socket sk) {
		// TODO Auto-generated constructor stub
		connSocket=sk;
		initializeStreams();
	}
	
	public void initializeStreams()
	{
		try {
			
			
			oout=new ObjectOutputStream(connSocket.getOutputStream());
			oout.flush();
			oin=new ObjectInputStream(connSocket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		try {
			Packet pkt=(Packet)oin.readObject();
			handlePacket(pkt);
			closeStreams();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
}

	public void closeStreams()
	{
		
		try {
			oout.close();
			connSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void handlePacket(Packet pkt)
	{
		switch(pkt.type)
		{
			
			case INTEREST :
				sendfile(pkt.data);
				
				
				break;
				
		
			default:
			break;
				
			
			
		}
	}
	
	
	public void sendfile(String filename)
	{
		System.out.println("Request for file "+filename);
		String filepath = DatabaseHandler.getFilePath(filename);
		Path path=Paths.get(filepath);
		
		if(filepath==null || !Files.exists(path)|| Files.isDirectory(path))
		{
			
			Packet pkt=new Packet();
			pkt.type=MsgType.NOTFOUND;
			pkt.data=null;
			
			try {
				oout.writeObject(pkt);
				oout.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			File f= new File(filepath);
			long length=f.length();
			Packet pkt=new Packet();
			pkt.type=MsgType.SUCCESS;
			pkt.data=""+length ;
			
			try {
				oout.writeObject(pkt);
				oout.flush();
				writeFileToSocket(f);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}
	
	public void writeFileToSocket(File f) throws IOException
	{
		in = new FileInputStream(f);
		int count;
		byte[] bytes = new byte[16 * 1024];
		
		while ((count = in.read(bytes)) > 0) {
            oout.write(bytes, 0, count);
        }
		in.close();
		System.out.println("File send");
	}
	
	protected void finalize() throws Throwable
	{
		closeStreams();
		super.finalize();
	}
	

}
