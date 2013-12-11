import java.rmi.Remote;
import java.rmi.RemoteException;


public interface BankInterface extends Remote {
	
	int test(int x, int y) throws RemoteException;
	
	public boolean createNewCustomer(String firstName, String lastName, String bankCode) throws RemoteException;
	
	public boolean createNewAccount(int customerId, String bankCode) throws RemoteException;
	
	public boolean deleteCustomer(int customerId, String bankCode) throws RemoteException;
	
	public boolean deposit(int accountId, float amount, String bankCode) throws RemoteException;
	
	public boolean cashout(int accountId, float amount, String bankCode) throws RemoteException;
	
	public boolean remittance(int destAccountId, String destBankCode, int srcAccountId, String srcBankCode, float amount) throws RemoteException;
	
	public boolean transfer(int destAccountId, int srcAccountId, String bankCode, float amount) throws RemoteException;
	
	public void dumpDBEntries(String filename) throws RemoteException;

}
