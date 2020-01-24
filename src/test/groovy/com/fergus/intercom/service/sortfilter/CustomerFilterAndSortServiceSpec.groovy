package com.fergus.intercom.service.sortfilter


import com.fergus.intercom.domain.Customer
import com.fergus.intercom.domain.IDomainObject
import com.fergus.intercom.service.MathHelperService

import spock.lang.Specification

/**
 * Unit tests for CustomerFilterAndSortService
 *
 * @author fergus
 *
 */
class CustomerFilterAndSortServiceSpec extends Specification {

	def mathHelperService
	def customerFilterAndSortService

	static final BigDecimal CUSTOMER_ONE_POSITION = 10.00

	static final BigDecimal CUSTOMER_TWO_POSITION = 20.00

	def "setup"(){
		mathHelperService = Mock(MathHelperService.class)
		customerFilterAndSortService = new CustomerFilterAndSortService()
	}

	def "TEST apply filter to customer list"(){

		given:"Two customers in map, one that should be filtered, one should not"

				customerFilterAndSortService = new CustomerFilterAndSortService(){
				@Override
				protected boolean distanceToOfficeIsLessThanFilterValue(Customer customer){
					return (customer.getUserId().equals(1));
				}
			}

			Customer customer1 = Stub(Customer.class);
			Customer customer2 = Stub(Customer.class);

			customer1.userId >> 1
			customer2.userId >> 2


			List<Customer> customerList = new ArrayList();
			customerList.add(customer1);
			customerList.add(customer2);
	
			
		when: "applyFilterToCustomers is called"
		def filteredList = customerFilterAndSortService.applyFilterToCustomers(customerList)

		then: "returned list is as expected"
		filteredList.size() == 1;
		filteredList.get(0) == customer1
	}

	def "TEST apply filter to single customer"(){

		given:"two customers, one has distance less than and one greater than specified value"
		Customer customer1 = Stub(Customer.class);
		Customer customer2 = Stub(Customer.class);
		def mathHelperService = Mock(MathHelperService.class)

		customerFilterAndSortService = new CustomerFilterAndSortService() {
					@Override
					protected MathHelperService getMathHelperService(){
						return new MathHelperService() {

									@Override
									public Double haversineDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
										if(lat1 == CUSTOMER_ONE_POSITION && lon1 == CUSTOMER_ONE_POSITION) {
											return 50.00;
										}
										else if(lat1 == CUSTOMER_TWO_POSITION && lon1 == CUSTOMER_TWO_POSITION) {
											return 150.00;
										}
									}
								}
					}
				}

		customer1.latitude >> CUSTOMER_ONE_POSITION;
		customer1.longitude >> CUSTOMER_ONE_POSITION;
		customer2.latitude >> CUSTOMER_TWO_POSITION;
		customer2.longitude >> CUSTOMER_TWO_POSITION;

		when: "applyFilterToCustomers is called"
		def customer1CloseToOffice = customerFilterAndSortService.distanceToOfficeIsLessThanFilterValue(customer1)
		def customer2CloseToOffice = customerFilterAndSortService.distanceToOfficeIsLessThanFilterValue(customer2)

		then: "returned list is as expected"
		customer1CloseToOffice == true;
		customer2CloseToOffice == false
	}


	def "TEST order customer list by userId"(){

		given:"customer list has been filtered"

		Customer customer1 = Stub(Customer.class);
		Customer customer2 = Stub(Customer.class);

		customer1.userId >> 2
		customer2.userId >> 1

		List<Customer> customerList = new ArrayList();
		customerList.add(customer1);
		customerList.add(customer2);

		when: "orderCustomersByUserId is called"
		def orderedCustomerList = customerFilterAndSortService.orderCustomersByUserId(customerList)

		then: "returned list is ordered correctly"
		orderedCustomerList.get(0) == customer2
		orderedCustomerList.get(1) == customer1
	}

	def "TEST create customer list from domain object list"(){

		given:"two customers"
		Customer customer1 = Stub(Customer.class);
		Customer customer2 = Stub(Customer.class);

		List<IDomainObject> domainObjectList = new ArrayList();
		domainObjectList.add(customer1);
		domainObjectList.add(customer2);

		when: "createCustomerListFromDomainObjectList"
		def customerList = customerFilterAndSortService.createCustomerListFromDomainObjectList(domainObjectList)

		then: "list is of correct type"
		(customerList instanceof List<Customer>) == true;
	}

	def "TEST filter and sort"(){
		given:"two customers in list, already filtered and sorted"

		customerFilterAndSortService = new CustomerFilterAndSortService(){
					@Override
					protected List<Customer> createCustomerListFromDomainObjectList(List<IDomainObject> domainObjectList) {
						return new ArrayList<Customer>();
					}
					@Override
					protected List<Customer> applyFilterToCustomers(List<Customer> customerList) {
						return new ArrayList<Customer>();
					}

					@Override
					protected List<IDomainObject> orderCustomersByUserId(List<Customer> customerList) {
						return new ArrayList<IDomainObject>();
					}
				}

		Customer customer1 = Stub(Customer.class);
		Customer customer2 = Stub(Customer.class);

		List<IDomainObject> domainObjectList = new ArrayList();
		domainObjectList.add(customer1);
		domainObjectList.add(customer2);

		when: "createCustomerListFromDomainObjectList"
		def returnedDomainObjectList = customerFilterAndSortService.filterAndSort(domainObjectList)

		then: "list is of correct type"
		(returnedDomainObjectList instanceof List<IDomainObject>) == true;
	}
}
