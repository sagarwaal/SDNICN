package host;
import java.sql.* ;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.*;
public class DatabaseHandler {
	
	Connection conn;
	
	private static final String CONN_URI="jdbc:sqlite:host.db" ;
	private static final String DRIVER_CLASS_NAME="org.sqlite.JDBC";
	
	private static DataSource ds;
	
	private static void initialize()
	{
		PoolProperties p = new PoolProperties();
		
		p.setUrl(CONN_URI);
		p.setDriverClassName(DRIVER_CLASS_NAME);
		p.setInitialSize(10);
		
		
		synchronized (ds){
			if(ds==null){
				ds=new DataSource();
				ds.setPoolProperties(p);
			}
			
		}
		createTable();
		
	}
	
	
	
	
	
	public DatabaseHandler()
	{
		initialize();
	}
	
	public static void createTable()  //correct try-catch block
	{
		Connection con=null;
		String sql= "CREATE IF NOT EXISTS HOSTTABLE "+
				"(NAME VARCHAR(250) , PATH VARCHAR(250)" +
				")";
		try {
			con = ds.getConnection();
			
			
			Statement st= con.createStatement();
			
			
			st.executeUpdate(sql);
			st.close();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public static void addFile(String name , String path)
	{
		String sql="Insert into HOSTTABLE (name,path) VALUES (?,?)" ;
		
		if(ds==null)
			initialize();
		
		try{
		Connection con=ds.getConnection();
		PreparedStatement st=con.prepareStatement(sql);
		st.setString(1, name);
		st.setString(2, path);
		st.executeUpdate();
		st.close();
		con.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getFileList()
	{
		ArrayList<String> fileList= new ArrayList<String>() ;
		
		String sql="Select name from HOSTTABLE ";
		
		ResultSet rs;
		
		if(ds==null)
			initialize();
		
		try{
		Connection con=ds.getConnection();
		Statement st=con.createStatement();
		rs=st.executeQuery(sql);
		
		while(rs.next())
		{
			fileList.add(rs.getString("name"));
			
		}
		
		st.close();
		con.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return fileList;
		
	}
	
	
	public static String getFilePath(String filename)
	{
		String sql="Select path from HOSTTABLE where name=?";
		
		if(ds==null)
			initialize();
		
		Connection con;
		String path=null;
		try {
			con = ds.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, filename);
			
			ResultSet rs= ps.executeQuery();
			
			
			
			if(rs.next())
			{
				path=rs.getString("path");
			}
			
			ps.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return path;
	}
	
	
	
	
	
	
	
	
	
	

}
