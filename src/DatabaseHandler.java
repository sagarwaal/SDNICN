import java.sql.* ;
import java.util.ArrayList;

public class DatabaseHandler {
	
	Connection conn;
	
	public DatabaseHandler()
	{
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			conn=DriverManager.getConnection("jdbc:sqlite:host.db");
			
			Statement st= conn.createStatement();
			String sql= "CREATE IF NOT EXISTS HOSTTABLE "+
						"(NAME VARCHAR(250) , PATH VARCHAR(250)" +
						")";
			
			st.executeUpdate(sql);
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		
	}
	
	
	public void addFile(String name , String path)
	{
		String sql="Insert into HOSTTABLE (name,path) VALUES (?,?)" ;
		
		try{
		PreparedStatement st=conn.prepareStatement(sql);
		st.setString(1, name);
		st.setString(2, path);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getFileList()
	{
		ArrayList<String> fileList= new ArrayList<String>() ;
		
		String sql="Select name from HOSTTABLE ";
		
		ResultSet rs;
		
		try{
		Statement st=conn.createStatement();
		rs=st.executeQuery(sql);
		while(rs.next())
		{
			fileList.add(rs.getString("name"));
			
		}
		
		
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return fileList;
		
	}
	
	
	public String getFilePath(String filename)
	{
		return null;
	}
	
	
	
	
	
	
	
	
	
	

}
