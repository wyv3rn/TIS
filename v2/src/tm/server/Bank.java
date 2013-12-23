package tm.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
	
	public final String BINDNAME = "Bank";
	
	public boolean createNewCustomer(String firstName, String lastName, String bankCode) throws RemoteException;
	
	public boolean createNewAccount(int customerId, String bankCode) throws RemoteException;
	
	public boolean deleteCustomer(int customerId, String bankCode) throws RemoteException;
	
	public boolean deposit(int accountId, float amount, String bankCode) throws RemoteException;
	
	public boolean cashout(int accountId, float amount, String bankCode) throws RemoteException;
	
	public boolean remittance(int destAccountId, String destBankCode, int srcAccountId, String srcBankCode, float amount) throws RemoteException;
	
	public boolean transfer(int destAccountId, int srcAccountId, String bankCode, float amount) throws RemoteException;
}
