package tm.server.impl;

import tm.server.model.Operation;
import tm.server.model.Result;
import tm.server.model.Transaction;
import tm.server.util.ExecuteException;

public class TransactionManager {

	private int _id;
	private Scheduler _scheduler;
	
	public TransactionManager() {
		_id = 0;
		_scheduler = new Scheduler();
	}
	
	public Transaction createNewTransaction() {
		return new Transaction(_id++);
	}
	
	public Result execute(Operation<?> op, String bankCode) throws ExecuteException {
		return _scheduler.schedule(op, bankCode);
	}
	
}
