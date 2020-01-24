package com.fergus.intercom.serivce.objectmapper

import org.codehaus.jackson.annotate.JsonProperty

import com.fergus.intercom.domain.IDomainObject
import com.fergus.intercom.exception.ConfigNotFoundException
import com.fergus.intercom.modules.BasicIntercomModule
import com.fergus.intercom.service.objectmapper.DomainObjectMapperServiceExecutor
import com.google.inject.Guice
import com.google.inject.Injector

import spock.lang.Specification

/**
 * 
 * Unit tests for com.fergus.intercom.serivce.objectmapper.ObjectMapperServiceExecutor
 * 
 * @author kavanaghf
 *
 */
class DomainObjectMapperServiceExecutorSpec extends Specification {

	def objectMapperServiceExecutor;


	def "setup"() {
		/*
		 * Inject objectMapperExecutorService into test class
		 */
		Injector basicIntercomModuleInjector = Guice.createInjector(new BasicIntercomModule());
		objectMapperServiceExecutor =  basicIntercomModuleInjector.getInstance(DomainObjectMapperServiceExecutor.class)
	}

	def "TEST map is populated with correct amount of elements - this test will be updated as more implementations of IDomainObject are added"(){
		given: "objectMapperServiceExecutor has been injected into class"

		when: "the number of elements in the map is class map is checked"
		def mapSize = objectMapperServiceExecutor.getMap().size()

		then: "the map size is equal to 1"
		mapSize == 1;
	}

	def "TEST getDomainObjectList - correct implementation chosen"(){
		given: "objectMapperServiceExecutor has been injected into class, value and corresponding class implemention exist in map"

		objectMapperServiceExecutor = new TestObjectMapperServiceExecutor();

		when: "The service is passed a list of domain objects represented as a JSON String"

		List<String> domainObjectListAsString = new ArrayList();
		domainObjectListAsString.add("{\"user_id\": 12}")
		def domainObjectList = objectMapperServiceExecutor.getDomainObjectList(domainObjectListAsString)

		then: "String is mapped to domain object as expected"
		domainObjectList.size() == 1;
		(domainObjectList.get(0) instanceof TestDomainObject) == true;
		domainObjectList.get(0).userId == 12;
	}


	def "NEG TEST when config returns string not in map error is thrown"(){
		given: "objectMapperExecutorService has been injected into class, domainObject in config is string that does not correspond to any mapper"
		objectMapperServiceExecutor = new TestObjectMapperServiceExecutor() {
					protected String getDomainObjectConfigValue() {

						return "doesn't exist in map";
					}
				}

		when: "executeRead method is called"
		def listOfDomainObjects = objectMapperServiceExecutor.getDomainObjectList(null)

		then: "Correct service class (local) is returned"
		thrown ConfigNotFoundException
	}


	/**
	 * 
	 * Create local TestDomainObject so our test doesn't rely on any one implementation
	 * @author kavanaghf
	 *
	 */
	static class TestDomainObject implements IDomainObject{
		@JsonProperty("user_id")
		private Integer userId;

		public TestDomainObject() {
		}

		public TestDomainObject(Integer userId) {
			this.userId = userId;
		}
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId
		}
	}

	/**
	 * 
	 * Override certain methods so tests don't depend on any one map configuration
	 * or value from application.properties
	 * 
	 * Test becomes more general
	 * 
	 * @author kavanaghf
	 *
	 */
	class TestObjectMapperServiceExecutor extends DomainObjectMapperServiceExecutor{
		private static final String TEST_DOMAIN_OBJECT = "testDomainObject"

		@Override
		protected Map<String,  Class<? extends IDomainObject>> getMap() {
			Map<String,  Class<? extends IDomainObject>> testMap = new HashMap();
			testMap.put(TEST_DOMAIN_OBJECT, TestDomainObject.class);
			return testMap;
		}

		@Override
		protected String getDomainObjectConfigValue() {
			return TEST_DOMAIN_OBJECT;
		}
	}
}
