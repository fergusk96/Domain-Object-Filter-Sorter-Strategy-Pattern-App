package com.fergus.intercom.service.sortfilter;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fergus.intercom.App;
import com.fergus.intercom.domain.Customer;
import com.fergus.intercom.domain.IDomainObject;
import com.fergus.intercom.service.MathHelperService;
import com.google.inject.Inject;

/**
 * 
 * Concrete implementaton of IDomainSortAndFilterService
 * 
 * Given a list of customers, filter out customers based on max haversine distance 
 * between customer location and intercom office location
 * 
 * Order filtered list by userId ASC
 * 
 * @author kavanaghf
 *
 */
public class CustomerFilterAndSortService implements IDomainFilterAndSortService {

	@Inject
	MathHelperService mathHelperService;

	/**
	 * 
	 * Cast super type list down to sub type Customer list
	 * 
	 * then filter and sort this list
	 * 
	 * @param domainObjectList - List of Customers
	 * @return the passed in list once filtering and sorting has occured
	 */
	@Override
	public List<IDomainObject> filterAndSort(List<IDomainObject> domainObjectList) {

		List<Customer> customerList = createCustomerListFromDomainObjectList(domainObjectList);
		List<Customer> filteredCustomerList = applyFilterToCustomers(customerList);
		return orderCustomersByUserId(filteredCustomerList);
	}
	

	/**
	 * 
	 * @param domainObjectList
	 * @return List of customers generated from super type list
	 */
	protected List<Customer> createCustomerListFromDomainObjectList(List<IDomainObject> domainObjectList) {
		return domainObjectList.stream().map(e -> (Customer) e).collect(Collectors.toList());

	}

	/**
	 * 
	 * @param customerList
	 * @return customerList ordered by userId of customers in list
	 */
	protected List<IDomainObject> orderCustomersByUserId(List<Customer> customerList) {
		return customerList.stream().sorted(Comparator.comparing(Customer::getUserId)).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param customerList
	 * @return list of customers from customerList where haversine distance to intercom office is less than 100km
	 */
	protected List<Customer> applyFilterToCustomers(List<Customer> customerList) {
		Predicate<Customer> customerFilter = (e -> distanceToOfficeIsLessThanFilterValue(e));
		return customerList.stream().filter(customerFilter).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param customer
	 * @return returns true if customer location is less than 100km from intercom office, otherwise false
	 */
	protected boolean distanceToOfficeIsLessThanFilterValue(Customer customer) {
		Double intercomLatitude = Double.valueOf(App.getProperties().getProperty("intercom.latitude"));
		Double intercomLongitude = Double.valueOf(App.getProperties().getProperty("intercom.longitude"));

		Double distanceFromCustomerToOffice = getMathHelperService().haversineDistance(customer.getLatitude(),
				customer.getLongitude(), intercomLatitude, intercomLongitude);
		return distanceFromCustomerToOffice <= 100;
	}


	protected MathHelperService getMathHelperService() {
		return mathHelperService;
	}

}
