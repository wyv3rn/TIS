import java.rmi.RemoteException;


public class BankImplementation implements BankInterface {
	
	public GTM _gtm;
	
	public BankImplementation(GTM gtm) {
		_gtm = gtm;
	}
	
	public int test(int x, int y) {
		return (int) Math.pow(x, y);
	}
	
	/// TODO implement with transactions, i.e. 
	///		T <- beginTransaction() 
	///		T.op_1() -> creates "operation object" OP_1 -> can be send to object managers (i.e. DB2) or to a scheduler (later)
	///		T.op_2()
	///		...
	///		T.op_n()
	///		T.commit()
	
	public boolean createNewCustomer(String firstName, String lastName, String bankCode) {
		return _gtm.createNewCustomer(firstName, lastName, bankCode);
	}
	
	public boolean createNewAccount(int customerId, String bankCode) {
		return _gtm.createNewAccount(customerId, bankCode);
	}
	
	public boolean deleteCustomer(int customerId, String bankCode) {
		return _gtm.deleteCustomer(customerId, bankCode);	
	}
	
	public boolean deposit(int accountId, float amount, String bankCode) {
		return _gtm.deposit(accountId, (int) amount, bankCode);
	}
	
	public boolean cashout(int accountId, float amount, String bankCode) {
		return _gtm.cashout(accountId, (int) amount, bankCode);
	}
	
	public boolean remittance(int destAccountId, String destBankCode, int srcAccountId, String srcBankCode, float amount) {
		return _gtm.remittance(destAccountId, destBankCode, srcAccountId, srcBankCode, (int) amount);
	}
	
	public boolean transfer(int destAccountId, int srcAccountId, String bankCode, float amount) {
		return _gtm.transfer(destAccountId, srcAccountId, bankCode, (int) amount); 
	}
	
	public void dumpDBEntries(String filename) throws RemoteException {
		_gtm.dumpDBEntries(filename);
	}
}
