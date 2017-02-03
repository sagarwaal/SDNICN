import java.sql.* ;

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
	
	public void getFileList()
	{
	
	}
	
	
	
	
	
	
	
	
	
	
	

}
