package tm.server.model.operation;

import tm.server.model.Operation;
import tm.server.model.OperationTypes;
import tm.server.model.Transaction;

public class InsertAccount<ResultType> extends Operation<ResultType> {

	public InsertAccount(Transaction associatedTransaction, String custumerID) {
		super(associatedTransaction);
		_operationType = OperationTypes.INSERT_ACCOUNT;
		
		_query = "INSERT INTO KONTEN (Kontostand, KundenNr) VALUES ('0', '" + custumerID + "')";  
	}
	
}
