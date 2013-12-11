package Operations;

public class Delete extends Operation {

	String _db;
	String _query;
	

	/** 
	 * @param db (i.e. "vs12" or "vs13")
	 * @param objectId (i.e. customer ID or account ID)
	 */
	public Delete(int transactionId, String db, String tableName, int objectId) {
		
		super(transactionId);
		
		_db = db;
		
		String idString = "";
		
		if(tableName.equalsIgnoreCase("Konten")) {
			idString = "KontenNr";
		}
		
		if(tableName.equalsIgnoreCase("Kunden")) {
			idString = "KundenNr";
		}
		
		_query = "DELETE FROM " + tableName + " WHERE " + idString + " = '" + objectId + "'";
	}
	
}

