package Operations;

public class Read extends Operation {

	String _db;
	String _query;
	

	/**
	 * @param db (i.e. "vs12" or "vs13")
	 * @param objectId (i.e. customer ID or account ID)
	 */
	public Read(int transactionId, String db, String tableName, String selectStatement,
			String whereStatement, int objectId) {
		
		super(transactionId);
		
		_db = db;
		_query = 
				"SELECT '" + selectStatement + "' FROM '" + tableName + "' " +
				"WHERE '" + whereStatement + "' = '" + objectId + "' ";
	}
	
}

