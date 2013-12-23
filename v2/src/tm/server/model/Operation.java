package tm.server.model;


public class Operation<ResultType> {

	private Transaction _associatedTransaction;
	protected String _query;
	private ResultType _result;
	protected OperationTypes _operationType;
	
	public Operation(Transaction t) {
		_associatedTransaction = t;
		_query = "";
		_result = null;
		_operationType = OperationTypes.UNKNOWN;
	}
	
	public Transaction getAssociatedTransaction() {
		return _associatedTransaction;
	}
	
	public String getQuery() {
		return _query;
	}
	
	public void setResult(ResultType value) {
		_result = value;
	}
	
	public ResultType getReturnValue() {
		return _result;
	}
	
	public OperationTypes getType() {
		return _operationType;
	}
}
