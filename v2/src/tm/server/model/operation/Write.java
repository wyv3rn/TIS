package tm.server.model.operation;

import tm.server.model.Operation;
import tm.server.model.OperationTypes;
import tm.server.model.Transaction;


public class Write<InOutputType> extends Operation<InOutputType> {

	/**
	 * @param db (i.e. "vs12" or "vs13")
	 * @param objectId (i.e. customer ID or account ID)
	 */
	public Write(Transaction associatedTransaction, String accountID, InOutputType value) {
		super(associatedTransaction);
		_operationType = OperationTypes.WRITE;
		
		_query = "UPDATE Konten SET Kontostand = '" + value + "' WHERE KontenNr = '" + accountID + "'";
	}
	
}
