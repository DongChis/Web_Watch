
package context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {

	
	String url = "jdbc:sqlserver://DESKTOP-9LNQ1HF\\SQLEXPRESS:1433;databaseName=DongHo;encrypt=false";
	String userName = "sa";
	String password = "dunghoang168";

	
	public Connection getConnection() throws Exception{

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			Connection conn = DriverManager.getConnection(url,userName,password);
			System.out.println(conn);
			return conn;
			
			
		}
}
