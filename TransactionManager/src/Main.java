import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GTM gtm = new GTM();
		
		
		// setting up RMI
		BankImplementation bank;
		BankInterface skeleton;
		
		try {
			
			System.out.println("Setting up RMI.");
			
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			
			bank = new BankImplementation(gtm);
			skeleton = (BankInterface) UnicastRemoteObject.exportObject(bank, Registry.REGISTRY_PORT);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Bank", skeleton);
			
		} catch(RemoteException e) {
			System.out.println("Could not set up RMI on server side:");
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		
	
		gtm.printTableEntries("KUNDEN");
		System.out.println("\n\n ----- \n\n");
		gtm.printTableEntries("KONTEN");
		
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
