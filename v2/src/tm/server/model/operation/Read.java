package tm.server.model.operation;

import tm.server.model.Operation;
import tm.server.model.OperationTypes;
import tm.server.model.Transaction;

public class Read<ResultType> extends Operation<ResultType> {

	public Read(Transaction associatedTransaction, String accountID) {
		super(associatedTransaction);
		_operationType = OperationTypes.READ;

		_query = "SELECT Kontostand FROM Konten WHERE KontenNr = '" + accountID + "'";
	}
}

