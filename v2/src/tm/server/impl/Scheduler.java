package tm.server.impl;

import tm.server.model.Operation;
import tm.server.model.Result;
import tm.server.util.ExecuteException;


public class Scheduler {

	//TODO Verbindung über RMI anstelle von getObjectManagerbyBankCode(String bankCode)
	private ObjectManager _om1 = null;
	private ObjectManager _om2 = null;
	
	public Scheduler() {
		_om1 = new ObjectManager("vs12");
		_om2 = new ObjectManager("vs13");
	}
	
	public Result schedule(Operation<?> op, String bankCode) throws ExecuteException {
		ObjectManager om = getObjectManagerbyBankCode(bankCode);
		
		if (om != null) {
			return om.executeOperation(op);
		} else {
			System.out.println("Unknown bank code!");
			throw new ExecuteException();
		}
	}
	
	private ObjectManager getObjectManagerbyBankCode(String bankCode) {
		if (bankCode.equals("vs12")) {
			return _om1;
		} else if (bankCode.equals("vs13")) {
			return _om2;
		} else {
			return null;
		}
	}
}
