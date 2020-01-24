package com.fergus.intercom.service.sortfilter;

import java.util.List;

import com.fergus.intercom.domain.IDomainObject;


/**
 * 
 * Implementing class is responsible for filtering and sorting a list of domain objects
 * 
 * @author kavanaghf
 *
 */
public interface IDomainFilterAndSortService {


	List<IDomainObject> filterAndSort(List<IDomainObject> domainObjectList);

}
