package com.fergus.intercom

import com.fergus.intercom.domain.Customer
import com.fergus.intercom.domain.IDomainObject

import spock.lang.Specification;

class AppIntegrationSpec extends Specification{

	private static final String TEST_OUTPUT_TXT = "testOutput.txt"

	def application;

	def "setup"(){
		application = new App();
	}

	def "TEST write file - file of right structure created"(){
		given: "List of customers exists"

			List<IDomainObject> customerList = new ArrayList();
			Customer customer = new Customer();
			customer.setUserId(1);
			customer.setName("test")
			customerList.add(customer)


		when: "List is written to file"
		application.writeDomainObjectListToFile(customerList,TEST_OUTPUT_TXT);

		then: "File is created and contains elements"
			File file = new File(TEST_OUTPUT_TXT)
			file.exists() == true
			file.length() != 0
	
		cleanup:
			file.deleteOnExit()
	}
	
	def "TEST properties file is as expected - this test will be modified as more implementations are added"(){
		given: "Application is initialised"
		
		when: "Properties are loaded"
			def properties = application.getProperties();
		
		then: "Properties exist in application.properties file"
		
			properties.get("filereader.type").length() != 0;
			properties.get("domainobject.name").length() != 0;
			properties.get("intercom.latitude").length() != 0;
			properties.get("intercom.longitude").length() != 0;
			
	}
}
