
package context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {

	
	String url = "jdbc:sqlserver://DESKTOP-RDMJ7GI\\MSSQLSERVER02:1433;databaseName=DongHo;encrypt=false";
	String userName = "sa";
	String password = "123";

	
	public Connection getConnection() throws Exception{

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			return DriverManager.getConnection(url,userName,password);
			
		}

	
}
