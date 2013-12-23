package tm.server.impl.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tm.client.Client;
import tm.server.Bank;
import tm.server.Server;

public class BankImplTest {

	Server s;
	Client c;
	
	@Before
	public void setUp() throws Exception {
		// nothing to do, open files or something else
	}

	@After
	public void tearDown() throws Exception {
		//s.stopThread();
		//c.stopThread();
	}
	
	@Test
	public void testRMI() {
		try {
			s = new Server();
			s.start();
		} catch(Exception e) {
			System.out.println("Could not set up RMI on server side with reason:");
			System.out.println(e.getMessage());
			fail();
		}
		
		try {
			c = new Client();
			c.start();
		} catch(Exception e) {
			System.out.println("Could not set up RMI on client side with reason:");
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void testCreateNewCustomer() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			
			Bank bank = (Bank) registry.lookup("Bank");

			bank.createNewCustomer("John", "Doe", "vs12");
			bank.createNewCustomer("Jane", "Doe", "vs12");
			bank.createNewCustomer("Max", "Mustermann", "vs12");
			bank.createNewCustomer("John", "Wayne", "vs12");
			
			bank.createNewCustomer("Erika", "Musterfrau", "vs13");
			bank.createNewCustomer("Erik", "Mustermann", "vs13");
			bank.createNewCustomer("Max", "Musterfrau", "vs13");
			bank.createNewCustomer("Maria", "Mustermann", "vs13");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test createNewCustomer failed. " + e.getMessage());
		}
	}
	
	@Test
	public void testOtherOperations() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			
			Bank bank = (Bank) registry.lookup("Bank");
			
			bank.deposit(0, (float)1024, "vs12");
			
			bank.createNewAccount(0, "vs12");
			bank.createNewAccount(0, "vs12");
			
			bank.remittance(1, "vs12", 0, "vs12", (float)512);
			bank.remittance(2, "vs12", 1, "vs12", (float)256);
			bank.remittance(3, "vs12", 2, "vs12", (float)128);
			
			bank.remittance(0, "vs13", 3, "vs12", (float)64);
			
			bank.remittance(1, "vs13", 0, "vs13", (float)32);
			bank.remittance(2, "vs13", 1, "vs13", (float)16);
			bank.remittance(3, "vs13", 2, "vs13", (float)8);
			
			bank.deleteCustomer(0, "vs12");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test otherOperations failed. " + e.getMessage());
		}
	}
	
	/*
	@Test
	public void otherOperations() {
		bank.cashout(aid, amount, strs[3]);
		bank.transfer(destAID, srcAID, strs[3], amount);
	}
	*/
}
