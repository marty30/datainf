package nl.utwente.di;

import org.junit.Assert;
import org.junit.Test;

public class TestQuoter {
	@Test 
	 public void testBook1() throws Exception { 
	 Quoter quoter = new Quoter(); 
	 double price = quoter.getBookPrice("1"); 
	 Assert.assertEquals("Price of book 1", 10.0, price, 0.0); 
	 } 
}
