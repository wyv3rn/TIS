package tm.server.model.operation;

import tm.server.model.Operation;
import tm.server.model.OperationTypes;
import tm.server.model.Transaction;

public class InsertCustumer<ResultType> extends Operation<ResultType> {

	public InsertCustumer(Transaction associatedTransaction, String firstName, String lastName) {
		super(associatedTransaction);
		_operationType = OperationTypes.INSERT_CUSTUMER;

		_query = "INSERT INTO KUNDEN (Vorname, Nachname) VALUES ('" + firstName + "', '" + lastName + "')"; 
	}
}
