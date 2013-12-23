package tm.server.model.operation;

import tm.server.model.Operation;
import tm.server.model.OperationTypes;
import tm.server.model.Transaction;

public class DeleteCustumer<ResultType> extends Operation<ResultType> {
	
	public DeleteCustumer(Transaction associatedTransaction, String custumerID) {
		super(associatedTransaction);
		_operationType = OperationTypes.DELETE_CUSTUMER;
		
		_query = "DELETE FROM Kunden WHERE KundenNr = '" + custumerID + "'";
	}
	
}

