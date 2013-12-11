import java.sql.*;
import java.util.ArrayList;

public class DBMS {
	
	private Connection 	_con;
	
	public DBMS(String hostname) {
		_con = connectToDB(hostname);
	}
	
	public Connection getConnection() {
		return _con;
	}
	
	private Connection connectToDB(String hostname) {
		Connection con = null;
		try {
		System.out.println("Connecting to Database DB2 ...");
		con = DriverManager.getConnection("jdbc:db2://" + hostname + ".prakinf.tu-ilmenau.de:50000/Bank2", 
				"tis2", "tis211#");
		
		DatabaseMetaData metadata = con.getMetaData();

		boolean customer = false;
		boolean account = false;
		
		ResultSet resultSet;
		resultSet = metadata.getTables(null, null, "%", null);
		while(resultSet.next()) {
		    
		    if(resultSet.getString("TABLE_NAME").equals("KUNDEN")) {
		    	System.out.println("Found existing table: " + resultSet.getString("TABLE_NAME"));
		    	customer = true;
		    }
		    if(resultSet.getString("TABLE_NAME").equals("KONTEN")) {
		    	System.out.println("Found existing table: " + resultSet.getString("TABLE_NAME"));
		    	account = true;
		    }
		    
		}
		
		System.out.println("customer = " + customer);
		System.out.println("account = " + account);
		
		if(!customer) {
			System.out.println("Creating table for customers in given database...");
		      Statement stmt = con.createStatement();
		      
		      String sql = "CREATE TABLE KUNDEN " +
		                   "(KundenNr INTEGER not NULL GENERATED ALWAYS AS IDENTITY (START WITH 0 INCREMENT BY 1, NO CACHE), " +
		                   " Vorname VARCHAR(255), " + 
		                   " Nachname VARCHAR(255), " +
		                   " PRIMARY KEY ( KundenNr ))"; 

		      stmt.executeUpdate(sql);
		      System.out.println("Created table for customers in given database...");
			
		}
		
		if(!account) {
			System.out.println("Creating table for accounts in given database...");
		      Statement stmt = con.createStatement();
		      
		      String sql = "CREATE TABLE KONTEN " +
		                   "(KontenNr INTEGER not NULL GENERATED ALWAYS AS IDENTITY (START WITH 0 INCREMENT BY 1, NO CACHE), " +
		                   " Kontostand FLOAT, " + 
		                   " KundenNr INTEGER not NULL, " +
		                   " PRIMARY KEY ( KontenNr ), " +
		                   " FOREIGN KEY ( KundenNr ) REFERENCES KUNDEN( KundenNr ))"; 

		      stmt.executeUpdate(sql);
		      System.out.println("Created table for accounts in given database...");
			
		}
		
		/**
		 * @todo check if the tables have the correct form
		 */

		
		} catch(SQLException e) {
			System.out.println("Connection to DB or creation of tables failed.");
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		
		System.out.println(" ... connecting to Database DB2 successful.");
		return con;

	}
	
	public void deleteTables() {
		try {
		
	      Statement stmt = _con.createStatement();
	      
	      stmt.executeUpdate("DROP TABLE KONTEN");
	      stmt.executeUpdate("DROP TABLE KUNDEN");
	      

	      System.out.println("Droped our tables");
		
		
		} catch (SQLException e) {
			System.out.println("Droping tables failed.");
			System.out.println(e.getMessage());
		}
	}
	
	public void printTableEntries(String tableName) {
		/// TODO use getTableEntries() here!
		try {
			
			Statement stmt = _con.createStatement();
			
			String query = "SELECT * FROM " + tableName;
			  
			ResultSet rs = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			String header = "";
			for(int i = 1; i <= columnsNumber; ++i) {
				header += rsmd.getColumnName(i);
				if(i != columnsNumber) {
					header += " ";
				}
			}
			System.out.println(header);
			
			
			
			while(rs.next()) {
				String row = "";
				for(int i = 1; i <= columnsNumber; ++i) {
					row += rs.getString(i);
					if(i != columnsNumber) {
						row += "\t";
					}
				}
				System.out.println(row);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public ArrayList<String> getTableEntries(String tableName) {
		ArrayList<String> tableEntries = new ArrayList<String>();
		try {
			
			Statement stmt = _con.createStatement();
			
			String query = "SELECT * FROM " + tableName;
			  
			ResultSet rs = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			String header = "";
			for(int i = 1; i <= columnsNumber; ++i) {
				header += rsmd.getColumnName(i);
				if(i != columnsNumber) {
					header += " ";
				}
			}
			tableEntries.add(header);
			
			
			
			while(rs.next()) {
				String row = "";
				for(int i = 1; i <= columnsNumber; ++i) {
					row += rs.getString(i);
					if(i != columnsNumber) {
						row += "\t";
					}
				}
				tableEntries.add(row);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return tableEntries;
		
	}
	

}
