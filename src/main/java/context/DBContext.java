
package context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
	
	String url = "jdbc:sqlserver://DESKTOP-RDMJ7GI\\MSSQLSERVER02:1433;databaseName=DongHo;encrypt=false";
	String userName = "sa";
	String password = "123";

// SQLSERVER name dunghoang: DESKTOP-9LNQ1HF
//	String url = "jdbc:sqlserver://DESKTOP-9LNQ1HF\\MSSQLSERVER02:1433;databaseName=DongHo;encrypt=false";
//	String userName = "sa";
//	String password = "dunghoang168";

	
	public Connection getConnection() throws Exception{

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			Connection conn = DriverManager.getConnection(url,userName,password);
			System.out.println(conn);
			return conn;
			
			
		}
}
