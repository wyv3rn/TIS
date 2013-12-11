import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
	
	private static BankInterface bank;

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry();
			
			bank = (BankInterface) registry.lookup("Bank");
			
			System.out.println(bank.test(2, 13));
			executeScript("src/test.bs");
			
		} catch(Exception e) {
			System.out.println("RMI on client side failed:");
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void executeScript(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while((line = reader.readLine()) != null) {
				if(line.length() != 0 && !line.substring(0, 1).equals("#")) {
					// skip comment lines!
					executeOperation(line);
				}

			}
			
		} catch (Exception e) {
			System.out.println("Executing script failed:");
			System.out.println(e.getMessage());
			
		}
	}
	
	public static void executeOperation(String line) {
		try {
			String[] strs = line.split(" ");
			String command = strs[0];
			if(command.equals("createNewCustomer")) {
				if(strs.length != 4) {
					System.out.println("Expected 3 parameters for a createNewCustomer, skipping this operation: " + line);
					return;
				}
				bank.createNewCustomer(strs[1], strs[2], strs[3]);
				return;
			}
			
			if(command.equals("createNewAccount")) {
				if(strs.length != 3) {
					System.out.println("Expected 2 parameters for a createNewAccount, skipping this operation: " + line);
					return;
				}
				Integer cid = Integer.valueOf(strs[1]);
				bank.createNewAccount(cid, strs[2]);
				return;
			}
			
			if(command.equals("deleteCustomer")) {
				if(strs.length != 3) {
					System.out.println("Expected 2 parameters for a deleteCustomer, skipping this operation: " + line);
					return;
				}
				Integer cid = Integer.valueOf(strs[1]);
				bank.deleteCustomer(cid, strs[2]);
				return;
			}
			
			if(command.equals("deposit")) {
				if(strs.length != 4) {
					System.out.println("Expected 3 parameters for a deposit, skipping this operation: " + line);
					return;
				}
				Integer aid = Integer.valueOf(strs[1]);
				Float amount = Float.valueOf(strs[2]);
				bank.deposit(aid, amount, strs[3]);
				return;
			}
			
			if(command.equals("cashout")) {
				if(strs.length != 4) {
					System.out.println("Expected 3 parameters for a cashout, skipping this operation: " + line);
					return;
				}
				Integer aid = Integer.valueOf(strs[1]);
				Float amount = Float.valueOf(strs[2]);
				bank.cashout(aid, amount, strs[3]);
				return;
			}
			
			if(command.equals("remittance")) {
				if(strs.length != 6) {
					System.out.println("Expected 5 parameters for a remittance, skipping this operation: " + line);
					return;
				}
				Integer destAID = Integer.valueOf(strs[1]);
				Integer srcAID = Integer.valueOf(strs[3]);
				Float amount = Float.valueOf(strs[5]);
				bank.remittance(destAID, strs[2], srcAID, strs[4], amount);
				return;
			}
			
			if(command.equals("transfer")) {
				if(strs.length != 5) {
					System.out.println("Expected 4 parameters for a transfer, skipping this operation: " + line);
					return;
				}
				Integer destAID = Integer.valueOf(strs[1]);
				Integer srcAID = Integer.valueOf(strs[2]);
				Float amount = Float.valueOf(strs[4]);
				bank.transfer(destAID, srcAID, strs[3], amount);
				return;
			}
			
			if(command.equals("dumpDBEntries")) {
				if(strs.length != 2) {
					System.out.println("Expected 1 parameters for dumping the DB entries (the filename), skipping this operation: " + line);
					return;
				}
				bank.dumpDBEntries(strs[1]);
				return;
			}
			
			System.out.println("Skipping unknown operation: " + line);
			
			
		} catch(RemoteException e) {
			System.out.println("Executing operation \"" + line + "\" failed:");
			System.out.println(e.getMessage());
		}
	}

}
