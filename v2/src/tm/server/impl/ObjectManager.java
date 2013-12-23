package tm.server.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tm.server.model.Operation;
import tm.server.model.Result;

import com.ibm.db2.jcc.DB2Driver;

public class ObjectManager {

	private Connection _con = null;
	private String _dbName = null;
	
	public ObjectManager(String dbName) {
		try {
			DriverManager.registerDriver(new DB2Driver());
		} catch(SQLException e) {
			System.out.println("Failed registering DB2 Driver");
			System.exit(-1);
		}
		
		_dbName = dbName;

		_con = connectToDB(dbName);
		
		if(_con != null) {
			System.out.println("Connection is ok!");
		} else {
			System.out.println("Connection is not ok!");
			System.exit(-1);
		}
	}
	
	private Connection connectToDB(String hostname) {
		Connection con = null;
		try {
			System.out.println("Connecting to Database DB2 ...");
			con = DriverManager.getConnection("jdbc:db2://" + hostname + ".prakinf.tu-ilmenau.de:50000/Bank2", "tis2", "tis211#");

			DatabaseMetaData metadata = con.getMetaData();

			boolean customer = false;
			boolean account = false;

			ResultSet resultSet;
			resultSet = metadata.getTables(null, null, "%", null);
			while (resultSet.next()) {
				if (resultSet.getString("TABLE_NAME").equals("KUNDEN")) {
					System.out.println("Found existing table: "
							+ resultSet.getString("TABLE_NAME"));
					customer = true;
				}
				if (resultSet.getString("TABLE_NAME").equals("KONTEN")) {
					System.out.println("Found existing table: "
							+ resultSet.getString("TABLE_NAME"));
					account = true;
				}
			}

			System.out.println("customer = " + customer);
			System.out.println("account = " + account);

			if (!customer) {
				System.out.println("Creating table for customers in given database...");
				Statement stmt = con.createStatement();

				String sql = "CREATE TABLE KUNDEN "
						+ "(KundenNr INTEGER not NULL GENERATED ALWAYS AS IDENTITY (START WITH 0 INCREMENT BY 1, NO CACHE), "
						+ " Vorname VARCHAR(255), "
						+ " Nachname VARCHAR(255), "
						+ " PRIMARY KEY ( KundenNr ))";

				stmt.executeUpdate(sql);
				System.out.println("Created table for customers in given database...");
			}

			if (!account) {
				System.out.println("Creating table for accounts in given database...");
				Statement stmt = con.createStatement();

				String sql = "CREATE TABLE KONTEN ("
						+ " KontenNr INTEGER not NULL GENERATED ALWAYS AS IDENTITY (START WITH 0 INCREMENT BY 1, NO CACHE), "
						+ " Kontostand FLOAT, "
						+ " KundenNr INTEGER not NULL, "
						+ " PRIMARY KEY ( KontenNr ), "
						+ " FOREIGN KEY ( KundenNr ) REFERENCES KUNDEN( KundenNr )"
						+ ")";

				stmt.executeUpdate(sql);
				System.out.println("Created table for accounts in given database...");
			}
		} catch (SQLException e) {
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

	public Result executeOperation(Operation<?> op) {
		try {
			Statement stmt = _con.createStatement();
			stmt.executeUpdate(op.getQuery(), Statement.RETURN_GENERATED_KEYS);
			
			ResultSet result = stmt.getGeneratedKeys();
			result.next();
			int newId = result.getInt(1);
			
			Result r = new Result(String.valueOf(newId));
	      
			switch(op.getType()) {
				case DELETE_ACCOUNT:
					break;
				case DELETE_CUSTUMER:
					break;
				case INSERT_ACCOUNT:
					System.out.println("Created new account. Account ID: " + newId);
					break;
				case INSERT_CUSTUMER:
					System.out.println("Created new customer. Customer ID: " + newId);
					break;
				case READ:
					break;
				case UNKNOWN:
					break;
				case WRITE:
					break;
				default:
					break;
			}
			
			return r;
		} catch (SQLException e) {
			System.out.println("Operation failed.");
			System.out.println(e.getMessage());
			return null;
		}
	}
	
//	//Deleting with cID
//		public boolean deleteCustomer(int cID){
//		  try{
//			  //Statement stmt = _con.createStatement();
//			 
//			  //Deleting Accounts of Customer cID
//			  PreparedStatement accounts = _con.prepareStatement("DELETE FROM Konten WHERE KundenNr = ?");
//			  accounts.setInt(1, cID);
//			  
//			  int rowsDeleted = accounts.executeUpdate(); 
//			  System.out.println(rowsDeleted + " rows deleted"); 
//			  accounts.close();
//			  
//			  //Deleting Customer with cID
//			  PreparedStatement customer = _con.prepareStatement("DELETE FROM Kunden WHERE KundenNr = ?");
//			  customer.setInt(1, cID);
//			  customer.executeUpdate();
//			  customer.close();
//			  
//			  return true;
//		  } catch (SQLException e) {
//				System.out.println("Deleting failed.");
//				System.out.println(e.getMessage());
//				return false;
//		  }
//	  }
//	
//	/**
//	 * @brief can be used for decrementing as well -> just use a negative amount
//	 */
//	public boolean incrementAccountBalance(int accountId, int amount) {
//		try{
//			  Statement stmt = _con.createStatement();
//			  int currentAmount;
//			  //getting current amount 
//			  String query =
//					  "SELECT Kontostand FROM KONTEN " +
//					  "WHERE KontenNr = '" + accountId + "' ";
//			  
//			  ResultSet rs = stmt.executeQuery(query);
//			  if(rs.next()) {
//				  currentAmount = rs.getInt("Kontostand");
//				  
//				  //adding value
//				  currentAmount += amount;
//				  
//				  //writing new amount
//				  String update =
//						  "UPDATE Konten "	+
//						  "SET Kontostand = '" +currentAmount+ "' " +
//						  "WHERE KontenNr = '" + accountId + "' ";
//				  stmt.executeUpdate(update);
//				  
//				  return true;
//			  } else {
//				  System.out.println("Account (" + accountId + ") doesn't exist (increment balance).");
//				  return false;
//			  }
//		  } catch (SQLException e) {
//				System.out.println("Incrementing account balance failed.");
//				System.out.println(e.getMessage());
//				return false;
//		  }
//	}
//	
//	public boolean deposit(int accountId, int amount) {
//		if(amount <= 0) {
//			System.out.println("Deposit amount should be positive.");
//			return false;
//		}
//		
//		// increment balance
//		return incrementAccountBalance(accountId, amount);
//		
//	}
//	
//	public boolean cashout(int accountId, int amount) {
//		if(amount <= 0) {
//			System.out.println("Cash out amount should be positive.");
//			return false;
//		}
//		
//		// decrement balance ( - amount !)
//		return incrementAccountBalance(accountId, -amount);
//		
//	}
//	
//	public boolean remittance(int destAccountId, int srcAccountId, int amount) {
//		if(amount <= 0) {
//			System.out.println("Remittance amount should be positive.");
//			return false;
//		}
//		
//		boolean dest = incrementAccountBalance(destAccountId, amount);
//		boolean src = incrementAccountBalance(srcAccountId, -amount);
//		
//		if(dest && src) {
//			return true;
//		} else {
//			System.out.println("Remittance failed, account balances might be corrupted now.");
//			return false;
//		}
//	}
//	
//	public boolean transfer(int destAccountId, int srcAccountId, int amount) {
//		if(amount <= 0) {
//			System.out.println("Transfer amount should be positive.");
//			return false;
//		}
//		
//		try {
//			Statement stmt = _con.createStatement();
//			int destCustomerId;
//			int srcCustomerId;
//			
//			String query =
//					  "SELECT KundenNr FROM KONTEN " +
//					  "WHERE KontenNr = '" + destAccountId + "' ";
//			  
//			ResultSet rs = stmt.executeQuery(query);
//			if(rs.next()) {
//				destCustomerId = rs.getInt("KundenNr");
//			} else {
//				System.out.println("Destination account doesn't exist (transfer).");
//				return false;
//			}
//			
//			query =
//					  "SELECT KundenNr FROM KONTEN " +
//					  "WHERE KontenNr = '" + srcAccountId + "' ";
//			  
//			rs = stmt.executeQuery(query);
//			if(rs.next()) {
//				srcCustomerId = rs.getInt("KundenNr");
//			} else {
//				System.out.println("Destination account doesn't exist (transfer).");
//				return false;
//			}
//			
//			System.out.println("Dest: " + destCustomerId + ", Src: " + srcCustomerId);
//			
//			if(srcCustomerId != destCustomerId) {
//				System.out.println("Transfer between different customers isn't allowed.");
//				return false;
//			}
//			
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//			return false;
//		}
//		
//		boolean dest = incrementAccountBalance(destAccountId, amount);
//		boolean src = incrementAccountBalance(srcAccountId, -amount);
//		
//		if(dest && src) {
//			return true;
//		} else {
//			System.out.println("Transfer failed, account balances might be corrupted now.");
//			return false;
//		}
//	}
	
	public void printTableEntries(String tableName) {
		System.out.println("Table " + tableName + " from " + _dbName);
		printTE(tableName);
		System.out.println("");
		System.out.println("");
	}
	
	private void printTE(String tableName) {
		// TODO use getTableEntries() here!
		try {
			Statement stmt = _con.createStatement();

			String query = "SELECT * FROM " + tableName;

			ResultSet rs = stmt.executeQuery(query);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			String header = "";
			for (int i = 1; i <= columnsNumber; ++i) {
				header += rsmd.getColumnName(i);
				if (i != columnsNumber) {
					header += " ";
				}
			}
			System.out.println(header);

			while (rs.next()) {
				String row = "";
				for (int i = 1; i <= columnsNumber; ++i) {
					row += rs.getString(i);
					if (i != columnsNumber) {
						row += "\t";
					}
				}
				System.out.println(row);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void dumpDBEntries(String filename) {
		ArrayList<String> customers = getTableEntries("KUNDEN");
		ArrayList<String> accounts = getTableEntries("KONTEN");
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(
		    		new OutputStreamWriter(
		    				new FileOutputStream(filename),
		    				"utf-8"
		    		)
		    );
		    
		    writer.write("------ " + _dbName + " ------\n\n");
		    
		    for (String line : customers) {
				writer.write(line);
				writer.write("\n");
			}
		    
		    writer.write("\n");
		    
		    for (String line : accounts) {
				writer.write(line);
				writer.write("\n");
			}
		    
		    writer.write("\n\n");
		    
			writer.close();
		} catch (IOException ex) {
			System.out.println("Dumping DB entries to file failed!");
		} 
	}
	
	private ArrayList<String> getTableEntries(String tableName) {
		ArrayList<String> tableEntries = new ArrayList<String>();
		try {

			Statement stmt = _con.createStatement();

			String query = "SELECT * FROM " + tableName;

			ResultSet rs = stmt.executeQuery(query);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			String header = "";
			for (int i = 1; i <= columnsNumber; ++i) {
				header += rsmd.getColumnName(i);
				if (i != columnsNumber) {
					header += " ";
				}
			}
			tableEntries.add(header);

			while (rs.next()) {
				String row = "";
				for (int i = 1; i <= columnsNumber; ++i) {
					row += rs.getString(i);
					if (i != columnsNumber) {
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
