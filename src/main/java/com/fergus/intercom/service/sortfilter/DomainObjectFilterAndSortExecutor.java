package com.fergus.intercom.service.sortfilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fergus.intercom.App;
import com.fergus.intercom.domain.Customer;
import com.fergus.intercom.domain.IDomainObject;
import com.fergus.intercom.exception.ConfigNotFoundException;
import com.fergus.intercom.modules.BasicIntercomModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * Determines correct strategy to sort and filter domainObject list.
 * 
 * Strategy selected is based on value read from config - domainobject.name
 * 
 * Once strategy to execute is found, sortAndFilterDomainObjectList method is
 * called for corresponding implementation of
 * {@link IDomainFilterAndSortService}
 * 
 * @author kavanaghf
 *
 */

public class DomainObjectFilterAndSortExecutor {
	
	/*
	 * Map that lists configuration strings to concrete implemetation of
	 * IDomainSortAndFilterService
	 */
	private final Map<String, Class<? extends IDomainFilterAndSortService>> domainObjectSortAndFilterImplementationMapping = new HashMap<>();

	/*
	 * Value in application.properties used to specify reading strategy
	 */
	protected static final String DOMAIN_OBJECT_CONFIG_KEY = "domainobject.name";

	private String domainObjectConfigValue = App.getProperties().getProperty(DOMAIN_OBJECT_CONFIG_KEY);

	
	/**
	 * Populate map with implementation of {@link IDomainFilterAndSortService} and config
	 * string that corresponds to this implementation
	 */
	public DomainObjectFilterAndSortExecutor() {
		domainObjectSortAndFilterImplementationMapping.put(Customer.getDomainNameInConfig(),
				CustomerFilterAndSortService.class);
	}

	/**
	 * 
	 * Retrieves correct implemetation of {@link IDomainFilterAndSortService} from map.
	 * 
	 * Calls sortAndFilter(domainObjectList) for that implementation
	 * 
	 * @return list of filtered and sorted domainObjects
	 * @throws ConfigNotFoundException
	 */
	public List<IDomainObject> sortAndFilterDomainObjectList(List<IDomainObject> domainObjectList)
			throws ConfigNotFoundException {
		final IDomainFilterAndSortService domainSortAndFilterService = getDomainSortAndFilterServiceInstance(
				getDomainObjectConfigValue());
		return domainSortAndFilterService.filterAndSort(domainObjectList);
	}


	/**
	 * 
	 * Retrieves correct implemetation of {@link IDomainFilterAndSortService} from map.
	 * 
	 * returns instance of this class through injection
	 * 
	 * @param domainObjectConfigValue - value of domain object specified by DOMAIN_OBJECT_CONFIG_KEY
	 * @return instance of class from map specified by config
	 * @throws ConfigNotFoundException
	 *             - relevant config not found in application.properties
	 */
	public IDomainFilterAndSortService getDomainSortAndFilterServiceInstance(final String domainObjectConfigValue)
			throws ConfigNotFoundException {
		Class<? extends IDomainFilterAndSortService> serviceClass = getMap().get(domainObjectConfigValue);
		if (serviceClass == null) {
			throw new ConfigNotFoundException(DOMAIN_OBJECT_CONFIG_KEY, getDomainObjectConfigValue());
		}
		return injectServiceClass(serviceClass);
	}

	protected IDomainFilterAndSortService injectServiceClass(
			Class<? extends IDomainFilterAndSortService> serviceClass) {
		Injector injector = Guice.createInjector(new BasicIntercomModule());
		return injector.getInstance(serviceClass);
	}

	protected Map<String, Class<? extends IDomainFilterAndSortService>> getMap() {
		return this.domainObjectSortAndFilterImplementationMapping;
	}

	public String getDomainObjectConfigValue() {
		return domainObjectConfigValue;
	}
}
