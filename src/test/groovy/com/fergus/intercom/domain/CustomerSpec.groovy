package com.fergus.intercom.domain

import spock.lang.Specification

/**
 * Unit tests for Customer POJO
 * 
 * @author kavanaghf
 *
 */
class CustomerSpec extends Specification{

	private static final String TEST_NAME = "TESTNAME"
	private static final Double TEST_LATITUDE = 10.0;
	private static final Double TEST_LONGITUDE = 11.0;
	private static final Integer TEST_USERID = 6;
	
	
	def "Test getters and setters for POJO"(){
		given: "Customer Object initialisated and fields set"
		def Customer customer = new Customer();
		customer.setName(TEST_NAME)
		customer.setLongitude(TEST_LONGITUDE)
		customer.setLatitude(TEST_LATITUDE)
		customer.setUserId(TEST_USERID) 

		when: "Getters are called on object"
		def customerName = customer.getName()
		def customerLatitude = customer.getLatitude()
		def customerLongitude = customer.getLongitude()
		def customerId = customer.getUserId();

		then: "Getters return the expected result"
		customerName == TEST_NAME 
		customerLatitude == TEST_LATITUDE
		customerLongitude == TEST_LONGITUDE
		customerId == TEST_USERID
		
	}
	
	def "TEST toString() - returns userid and name separated by comma"(){
		given: "A customer object with id and name"
		def customer = new Customer()
		customer.setName(TEST_NAME);
		customer.setUserId(TEST_USERID) 
		
		when: "toString method is called on Customer"
		def customerString = customer.toString();
		
		then: "returns userid and name separated by comma"
		customerString == TEST_NAME +", "+TEST_USERID
	}
}
