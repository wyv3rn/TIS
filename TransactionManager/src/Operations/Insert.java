package Operations;

public class Insert extends Operation {

	String _db;
	String _query;
	

	/**
	 * Constructor for inserting a new customer
	 * 
	 * @param db (i.e. "vs12" or "vs13")
	 */
	public Insert(int transactionId, String db, String firstName, String lastName) {
		
		super(transactionId);
		
		_db = db;
		_query = "INSERT INTO KUNDEN (Vorname, Nachname) " + 
	    		  "VALUES ('" + firstName + "', '" + lastName + "')"; 
	}
	
	/**
	 * Constructor for inserting a new account
	 * 
	 * @param db (i.e. "vs12" or "vs13")
	 */
	public Insert(int transactionId, String db, int customerId) {
		
		super(transactionId);
		
		_db = db;
		_query = "INSERT INTO KONTEN (Kontostand, KundenNr) " + 
	    		  "VALUES ('0', '" + customerId + "')";  
	}
	
}
