package com.fergus.intercom.service.sortfilter

import com.fergus.intercom.domain.IDomainObject
import com.fergus.intercom.exception.ConfigNotFoundException

import spock.lang.Specification

/**
 * Unit tests for CustomerFilterAndSortService
 *
 * @author fergus
 *
 */
class DomainObjectFilterAndSortExecutorSpec extends Specification {

	private static final String TEST_DOMAIN_OBJECT = "testDomainObject"

	def domainObjectFilterAndSortExecutor;

	def "setup"() {
		domainObjectFilterAndSortExecutor = new DomainObjectFilterAndSortExecutor();
	}

	def "TEST map is populated with correct amount of elements  - this test will be updated as more implementations of IDomainSortAndFilterService are added"() {
		given: "domainObjectSortAndFilterExecutor has been injected into class"

		when: "the number of elements in the map is class map is checked"
		def mapSize = domainObjectFilterAndSortExecutor.getMap().size()

		then: "the map size is equal to 1"
		mapSize == 1;
	}

	def "TEST when config is set to string contained in map, corresponding DomainObjectSortAndFilterService is executed"(){
		given: "DomainObjectSortAndFilterService has been injected into class"

		domainObjectFilterAndSortExecutor = new TestDomainObjectSortAndFilterExecutor();

		when: "getDomainSortAndFilterServiceInstance method is called"
		def domainSortAndFilterImplementation = domainObjectFilterAndSortExecutor.getDomainSortAndFilterServiceInstance(TEST_DOMAIN_OBJECT)

		then: "Correct implementation DomainObjectSortAndFilterService is returned"
		domainSortAndFilterImplementation.getClass() == TestDomainFilterAndSortService.class
	}



	def "TEST sortAndFilterObjectList"(){
		given: "An implementation of IDomainFilterAndSortService has been returned - this implemtation returns empty list for sortAndFilter"

		domainObjectFilterAndSortExecutor = new TestDomainObjectSortAndFilterExecutor() {
					public IDomainFilterAndSortService getDomainSortAndFilterServiceInstance(final String domainObject) {
						return new TestDomainFilterAndSortService();
					}
				};

		when: "getDomainSortAndFilterServiceInstance method is called"
		List testList1 = new ArrayList<String>();
		def domainObjectList = domainObjectFilterAndSortExecutor.sortAndFilterDomainObjectList(testList1)

		then: "the sortAndFilter method is executed on the correct instance"
		domainObjectList == testList1;
	}

	def "NEG TEST config returns string not in map,  error is thrown"(){
		given: "domainObjectSortAndFilterExecutor has been injected into class, domainObject in config is string that does not correspond to any implementation of domainObjectSortAndFilterService"
		domainObjectFilterAndSortExecutor = new TestDomainObjectSortAndFilterExecutor() {
					@Override
					public String getDomainObjectConfigValue() {
						return "doesn't exist in map";
					}
				}

		when: "executeRead method is called"
		List<IDomainObject> testArray = new ArrayList();
		def listOfDomainObjects = domainObjectFilterAndSortExecutor.sortAndFilterDomainObjectList(testArray);

		then: "Correct service class (local) is returned"
		thrown ConfigNotFoundException
	}



	/**
	 * 
	 * Local IDomainFilterAndSortService to generalise tests
	 * @author kavanaghf
	 *
	 */
	class TestDomainFilterAndSortService implements IDomainFilterAndSortService{
		@Override
		public List<IDomainObject> filterAndSort(List<IDomainObject> domainObjectList){
			return domainObjectList;
		}
	}

	/**
	 * 
	 * Override certain methods in test class to generalise test and not tie it 
	 * to any particular configuration
	 * 
	 *
	 */
	class TestDomainObjectSortAndFilterExecutor extends DomainObjectFilterAndSortExecutor{

		private static final String TEST_FILE_TYPE = TEST_DOMAIN_OBJECT

		@Override
		protected Map<String, Class<? extends IDomainFilterAndSortService>> getMap(){

			Map<String, Class<? extends IDomainFilterAndSortService>> testMap = new HashMap();
			testMap.put(TEST_DOMAIN_OBJECT, TestDomainFilterAndSortService.class);
			return testMap;
		}

		protected IDomainFilterAndSortService injectServiceClass(Class<? extends IDomainFilterAndSortService> serviceClass) {
			return new TestDomainFilterAndSortService();
		}
	}
}
