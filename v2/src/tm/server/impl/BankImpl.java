package tm.server.impl;
import tm.server.Bank;
import tm.server.model.Transaction;
import tm.server.model.operation.Read;
import tm.server.model.operation.Write;
import tm.server.util.ExecuteException;

/**
 * Our global transaction manager GTM
 */
public class BankImpl implements Bank {
	
	private TransactionManager _tm;
	
	public BankImpl() {
		_tm = new TransactionManager();
	}
	
	public boolean createNewCustomer(String firstName, String lastName, String bankCode) {
//		LTM ltm = getLTMbyBankCode(bankCode);
//		
//		if(ltm == null) {
//			return false;
//		}
//		
//		return ltm.createNewCustomer(firstName, lastName);
		return false;
	}
	
	public boolean createNewAccount(int customerId, String bankCode) {
//		LTM ltm = getLTMbyBankCode(bankCode);
//		
//		if(ltm == null) {
//			return false;
//		}
//		
//		return ltm.createNewAccount(customerId);
		return false;
	}
	
	public boolean deleteCustomer(int customerId, String bankCode) {
//		LTM ltm = getLTMbyBankCode(bankCode);
//		
//		if(ltm == null) {
//			return false;
//		}
//		
//		return ltm.deleteCustomer(customerId);
		return false;
	}
	
	public boolean deposit(int accountId, float amount, String bankCode) {
//		LTM ltm = getLTMbyBankCode(bankCode);
//		
//		if(ltm == null) {
//			return false;
//		}
//		
//		return ltm.deposit(accountId, (int)amount);
		return false;
	}
	
	public boolean cashout(int accountId, float amount, String bankCode) {
		Transaction t = _tm.createNewTransaction();
		t.begin();
		
		try {
			Read<Float> r = new Read<Float>(t, String.valueOf(accountId));
			Float currentAccountBalance = Float.valueOf(_tm.execute(r, bankCode).getResult());
			
			if (currentAccountBalance < amount) {
				t.abort();
				throw new ExecuteException();
			}
			
			float newAccountBalance = currentAccountBalance - amount;
			
			Write<Float> w = new Write<Float>(t, String.valueOf(accountId), newAccountBalance);
			_tm.execute(w, bankCode);
			
			t.commit();
			
			return true;
		} catch (ExecuteException e) {
			t.abort();
			return false;
		}
	}
	
	public boolean remittance(int destAccountId, String destBankCode, int srcAccountId, String srcBankCode, float amount) {
//		LTM destLtm = getLTMbyBankCode(destBankCode);
//		LTM srcLtm = getLTMbyBankCode(srcBankCode);
//		
//		if(destLtm == null || srcLtm == null) {
//			return false;
//		}
//		
//		boolean dest = destLtm.incrementAccountBalance(destAccountId, (int)amount);
//		
//		//TODO check if dest is true
//		
//		boolean src = srcLtm.incrementAccountBalance(srcAccountId, (int)-amount);
//		
//		if(!dest || !src) {
//			System.out.println("Something went wrong in a global remittance!");
//			return false;
//		}
//		
//		return true;
		return false;
	}
	
	public boolean transfer(int destAccountId, int srcAccountId, String bankCode, float amount) {
//		LTM ltm = getLTMbyBankCode(bankCode);
//		
//		if(ltm == null) {
//			return false;
//		}
//		
//		return ltm.transfer(destAccountId, srcAccountId, (int)amount);
		return false;
	}
}
