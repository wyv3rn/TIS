package tm.server.model;


public class Transaction {

	private int _transactionId;
	private TransactionStates _state;
	
	public Transaction(int id) {
		_transactionId = id;
		_state = TransactionStates.READY;
	}
	
	public void begin() {
		_state = TransactionStates.WORKING;
	}
	
	public void commit() {
		_state = TransactionStates.FINISH;
	}
	
	public void abort() {
		_state = TransactionStates.ABBORTED;
	}
	
	public TransactionStates getTransactionState() {
		return _state;
	}
	
	public int getTransactionId() {
		return _transactionId;
	}
}
