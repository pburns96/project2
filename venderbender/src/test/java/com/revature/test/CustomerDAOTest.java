package com.revature.test;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.revature.beans.Customer;
import com.revature.data.CustomerDAO;

public class CustomerDAOTest {
	private static final Logger log = Logger.getLogger(CustomerDAOTest.class);
	
	private CustomerDAO customerDao;
	private static ApplicationContext context;
	
	@BeforeClass
	public static void beforeClass(){
		context = new FileSystemXmlApplicationContext("/src/main/webapp/WEB-INF/bender.xml");
	}
	
	@Test
	public void testGetCustomerMethods(){
		log.debug("Testing the getCustomer(int id) method");
		customerDao = (CustomerDAO)context.getBean("customerDAO");
		
		Customer customer = (Customer)context.getBean("customer");
		customer.setUsername("pburns");
		customer.setHashedPassword("password");
		customer.setFirstname("Patrick");
		customer.setLastname("Burns");
		customer.setEmail("pburns@email.com");
		customer.setManager(false);
		
		customerDao.createCustomer(customer);
		
		Customer customerActual = customerDao.getCustomer(customer.getUsername());
		assertEquals(customer, customerActual);
		
		customerDao.deleteCustomer(customerActual);
	}
}
