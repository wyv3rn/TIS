package Operations;

public class Write extends Operation {

	String _db;
	String _query;
	

	/**
	 * @param db (i.e. "vs12" or "vs13")
	 * @param objectId (i.e. customer ID or account ID)
	 */
	public Write(int transactionId, String db, String tableName, String setStatement, String whereStatement, int changedValue, int Id) {
		
		super(transactionId);
		
		_db = db;
		
		/// TODO if this doesn't work -> try removing some "'" ;)
		_query =
				  "UPDATE '" + tableName + "' "	+
				  "SET '" + setStatement + "' = '" + changedValue + "' " +
				  "WHERE '" + whereStatement + "'  = '" + Id + "' ";
	}
	
}
