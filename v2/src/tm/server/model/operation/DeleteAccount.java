package tm.server.model.operation;

import tm.server.model.Operation;
import tm.server.model.OperationTypes;
import tm.server.model.Transaction;

public class DeleteAccount<ResultType> extends Operation<ResultType> {
	
	public DeleteAccount(Transaction associatedTransaction, String accountID) {
		super(associatedTransaction);
		_operationType = OperationTypes.DELETE_ACCOUNT;
		
		_query = "DELETE FROM Konten WHERE KontenNr = '" + accountID + "'";
	}
	
}

