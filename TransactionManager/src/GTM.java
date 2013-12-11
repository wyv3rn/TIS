import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ibm.db2.jcc.DB2Driver;


public class GTM {
	
	public DBMS 	_dbms0;
	public DBMS 	_dbms1;
	
	public LTM 		_ltm0;
	public LTM 		_ltm1;
	
	public GTM() {
		try {
			DriverManager.registerDriver(new DB2Driver());
		} catch(SQLException e) {
			System.out.println("Failed registering DB2 Driver");
			System.exit(-1);
			
		}
		
		_dbms0 = new DBMS("vs12");
		_dbms1 = new DBMS("vs13");
		
		// DEBUG only: clean up
//		_dbms0.deleteTables();
//		_dbms1.deleteTables();
		
		Connection con0 = _dbms0.getConnection();
		Connection con1 = _dbms1.getConnection();
		
		if(con0 != null && con1 != null) {
			System.out.println("Connection is ok!");
		} else {
			System.out.println("Connection is not ok!");
			System.exit(-1);
		}
		
//		DBMS.deleteTables();
		
		_ltm0 = new LTM(con0);
		_ltm1 = new LTM(con1);
	}
	
	
	public boolean createNewCustomer(String firstName, String lastName, String bankCode) {
		LTM ltm = getLTMbyBankCode(bankCode);
		
		if(ltm == null) {
			return false;
		}
		
		return ltm.createNewCustomer(firstName, lastName);
	}
	

	public boolean createNewAccount(int customerId, String bankCode) {
		LTM ltm = getLTMbyBankCode(bankCode);
		
		if(ltm == null) {
			return false;
		}
		
		return ltm.createNewAccount(customerId);
	
	}
	
	//Deleting with cID
	public boolean deleteCustomer(int cID, String bankCode){
		LTM ltm = getLTMbyBankCode(bankCode);
		
		if(ltm == null) {
			return false;
		}
		
		return ltm.deleteCustomer(cID);
	}
	
	
	
	public boolean deposit(int accountId, int amount, String bankCode) {
		LTM ltm = getLTMbyBankCode(bankCode);
		
		if(ltm == null) {
			return false;
		}
		
		return ltm.deposit(accountId, amount);
		
	}
	
	public boolean cashout(int accountId, int amount, String bankCode) {
		LTM ltm = getLTMbyBankCode(bankCode);
		
		if(ltm == null) {
			return false;
		}
		
		return ltm.cashout(accountId, amount);
		
	}
	
	public boolean remittance(int destAccountId, String destBankCode, int srcAccountId, String srcBankCode, int amount) {
		LTM destLtm = getLTMbyBankCode(destBankCode);
		LTM srcLtm = getLTMbyBankCode(srcBankCode);
		
		if(destLtm == null || srcLtm == null) {
			return false;
		}
		
		boolean dest = destLtm.incrementAccountBalance(destAccountId, amount);
		
		/// TODO check if dest is true
		
		boolean src = srcLtm.incrementAccountBalance(srcAccountId, -amount);
		
		if(!dest || !src) {
			System.out.println("Something went wrong in a global remittance!");
			return false;
		}
		
		return true;
		
		
		
	}
	
	public boolean transfer(int destAccountId, int srcAccountId, String bankCode, int amount) {
		LTM ltm = getLTMbyBankCode(bankCode);
		
		if(ltm == null) {
			return false;
		}
		
		return ltm.transfer(destAccountId, srcAccountId, amount);
	
	}
	
	private LTM getLTMbyBankCode(String bankCode) {
		if(bankCode.equals("vs12")) {
			return _ltm0;
		} else if(bankCode.equals("vs13")) {
			return _ltm1;
		} else {
			System.out.println("Unknown bank code!");
			return null;
		}
	}
	
	
	
	public void printTableEntries(String tableName) {
		System.out.println("Table " + tableName + " from VS12");
		_dbms0.printTableEntries(tableName);
		
		System.out.println("");
		System.out.println("---------------");
		System.out.println("");
		
		System.out.println("Table " + tableName + " from VS13");
		_dbms1.printTableEntries(tableName);
	}
	
	public void dumpDBEntries(String filename) {
		ArrayList<String> vs12customers = _dbms0.getTableEntries("KUNDEN");
		ArrayList<String> vs12accounts = _dbms0.getTableEntries("KONTEN");
		ArrayList<String> vs13customers = _dbms1.getTableEntries("KUNDEN");
		ArrayList<String> vs13accounts = _dbms1.getTableEntries("KONTEN");
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(filename), "utf-8"));
		    
		    writer.write("------ VS 12 ------\n\n");
		    
		    for (String line : vs12customers) {
				writer.write(line);
				writer.write("\n");
			}
		    
		    writer.write("\n");
		    
		    for (String line : vs12accounts) {
				writer.write(line);
				writer.write("\n");
			}
		    
		    writer.write("\n\n");
		    
		    writer.write("------ VS 13 ------\n\n");
		    
		    for (String line : vs13customers) {
				writer.write(line);
				writer.write("\n");
			}
		    
		    writer.write("\n");
		    
		    for (String line : vs13accounts) {
				writer.write(line);
				writer.write("\n");
			}
			writer.close();

		} catch (IOException ex) {
			System.out.println("Dumping DB entries to file failed!");
		} 
	}

}
