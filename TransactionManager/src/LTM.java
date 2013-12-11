import java.sql.*;

public class LTM {
	private Connection 		_con;
	
	public LTM(Connection con) {
		_con = con;
	}
	
	public boolean createNewCustomer(String firstName, String lastName) {
		try {
			Statement stmt = _con.createStatement();
	      
			String sql = "INSERT INTO KUNDEN (Vorname, Nachname) " + 
	    		  "VALUES ('" + firstName + "', '" + lastName + "')"; 
  
	     
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet result = stmt.getGeneratedKeys();
			
			result.next();
			int newId = result.getInt(1);
	      
			System.out.println("Created new customer. Customer ID: " + newId);
			
			createNewAccount(newId);
			
			return true;
			
		} catch (SQLException e) {
			System.out.println("Creation of customer failed.");
			System.out.println(e.getMessage());
			return false;
		}
	}
	

	public boolean createNewAccount(int customerId) {
		try {
			
			Statement stmt = _con.createStatement();
		      
			String sql = "INSERT INTO KONTEN (Kontostand, KundenNr) " + 
	    		  "VALUES ('0', '" + customerId + "')"; 
  
	     
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet result = stmt.getGeneratedKeys();
			
			result.next();
			int newId = result.getInt(1);
			
			System.out.println("Created new account. Account ID: " + newId);
			return true;
			
		} catch (SQLException e) {
			System.out.println("Creation of account failed.");
			System.out.println(e.getMessage());
			return false;
			
		}
	
	}
	
	//Deleting with cID
		public boolean deleteCustomer(int cID){
		  try{
			  //Statement stmt = _con.createStatement();
			 
			  //Deleting Accounts of Customer cID
			  PreparedStatement accounts = _con.prepareStatement("DELETE FROM Konten WHERE KundenNr = ?");
			  accounts.setInt(1, cID);
			  
			  int rowsDeleted = accounts.executeUpdate(); 
			  System.out.println(rowsDeleted + " rows deleted"); 
			  accounts.close();
			  
			  //Deleting Customer with cID
			  PreparedStatement customer = _con.prepareStatement("DELETE FROM Kunden WHERE KundenNr = ?");
			  customer.setInt(1, cID);
			  customer.executeUpdate();
			  customer.close();
			  
			  return true;
		  } catch (SQLException e) {
				System.out.println("Deleting failed.");
				System.out.println(e.getMessage());
				return false;
		  }
	  }
	
	/**
	 * @brief can be used for decrementing as well -> just use a negative amount
	 */
	public boolean incrementAccountBalance(int accountId, int amount) {
		try{
			  Statement stmt = _con.createStatement();
			  int currentAmount;
			  //getting current amount 
			  String query =
					  "SELECT Kontostand FROM KONTEN " +
					  "WHERE KontenNr = '" + accountId + "' ";
			  
			  ResultSet rs = stmt.executeQuery(query);
			  if(rs.next()) {
				  currentAmount = rs.getInt("Kontostand");
				  
				  //adding value
				  currentAmount += amount;
				  
				  //writing new amount
				  String update =
						  "UPDATE Konten "	+
						  "SET Kontostand = '" +currentAmount+ "' " +
						  "WHERE KontenNr = '" + accountId + "' ";
				  stmt.executeUpdate(update);
				  
				  return true;
			  } else {
				  System.out.println("Account (" + accountId + ") doesn't exist (increment balance).");
				  return false;
			  }
		  } catch (SQLException e) {
				System.out.println("Incrementing account balance failed.");
				System.out.println(e.getMessage());
				return false;
		  }
	}
	
	public boolean deposit(int accountId, int amount) {
		if(amount <= 0) {
			System.out.println("Deposit amount should be positive.");
			return false;
		}
		
		// increment balance
		return incrementAccountBalance(accountId, amount);
		
	}
	
	public boolean cashout(int accountId, int amount) {
		if(amount <= 0) {
			System.out.println("Cash out amount should be positive.");
			return false;
		}
		
		// decrement balance ( - amount !)
		return incrementAccountBalance(accountId, -amount);
		
	}
	
	public boolean remittance(int destAccountId, int srcAccountId, int amount) {
		if(amount <= 0) {
			System.out.println("Remittance amount should be positive.");
			return false;
		}
		
		boolean dest = incrementAccountBalance(destAccountId, amount);
		boolean src = incrementAccountBalance(srcAccountId, -amount);
		
		if(dest && src) {
			return true;
		} else {
			System.out.println("Remittance failed, account balances might be corrupted now.");
			return false;
		}
	}
	
	public boolean transfer(int destAccountId, int srcAccountId, int amount) {
		if(amount <= 0) {
			System.out.println("Transfer amount should be positive.");
			return false;
		}
		
		try {
			Statement stmt = _con.createStatement();
			int destCustomerId;
			int srcCustomerId;
			
			String query =
					  "SELECT KundenNr FROM KONTEN " +
					  "WHERE KontenNr = '" + destAccountId + "' ";
			  
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				destCustomerId = rs.getInt("KundenNr");
			} else {
				System.out.println("Destination account doesn't exist (transfer).");
				return false;
			}
			
			query =
					  "SELECT KundenNr FROM KONTEN " +
					  "WHERE KontenNr = '" + srcAccountId + "' ";
			  
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				srcCustomerId = rs.getInt("KundenNr");
			} else {
				System.out.println("Destination account doesn't exist (transfer).");
				return false;
			}
			
			System.out.println("Dest: " + destCustomerId + ", Src: " + srcCustomerId);
			
			if(srcCustomerId != destCustomerId) {
				System.out.println("Transfer between different customers isn't allowed.");
				return false;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		boolean dest = incrementAccountBalance(destAccountId, amount);
		boolean src = incrementAccountBalance(srcAccountId, -amount);
		
		if(dest && src) {
			return true;
		} else {
			System.out.println("Transfer failed, account balances might be corrupted now.");
			return false;
		}
		
		
		
		
	}
	

}
