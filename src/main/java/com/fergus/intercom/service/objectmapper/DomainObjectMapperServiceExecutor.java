package com.fergus.intercom.service.objectmapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import com.fergus.intercom.App;
import com.fergus.intercom.domain.Customer;
import com.fergus.intercom.domain.IDomainObject;
import com.fergus.intercom.exception.ConfigNotFoundException;

/**
 * Service to map JSON string to instance of {@link IDomainObject}
 * 
 * @author kavanaghf
 *
 */
public class DomainObjectMapperServiceExecutor {

	/*
	 * key in config to set domainObject to be mapped
	 */

	protected static final String DOMAIN_OBJECT_CONFIG_KEY = "domainobject.name";

	/*
	 * Value for this DOMAIN_OBJECT_CONFIG_KEY in application.properties
	 */
	protected String domainObjectConfigValue = App.getProperties().getProperty(DOMAIN_OBJECT_CONFIG_KEY);

	/*
	 * Map config value string to specific instance of IDomainObject e.g. map
	 * "customer" to Customer.class
	 */
	private final Map<String, Class<? extends IDomainObject>> domainObjectMapping = new HashMap<>();

	/**
	 * Populate map with known implementations of IDomainObject
	 */
	public DomainObjectMapperServiceExecutor() {
		domainObjectMapping.put(Customer.getDomainNameInConfig(), Customer.class);
	}

	/**
	 * 
	 * Select the correct object mapping strategy based on config in
	 * application.properties
	 * 
	 * then use this mapping strategy to generate list of DomainObject from list of
	 * String
	 * 
	 * @param domainObjectStringList - list of JSON strings representing domain
	 *                               object
	 * @return list of domain objects corresponding to each json string passed
	 * @throws IOException             - thrown when element of
	 *                                 domainObjectStringList can't be represented
	 *                                 as domainObject
	 * @throws ConfigNotFoundException - thrown when there is no mapping between
	 *                                 config value and domainObject
	 * 
	 */
	public List<IDomainObject> getDomainObjectList(List<String> domainObjectStringList) throws ConfigNotFoundException {

		ObjectMapper domainObjectMapper = new ObjectMapper();
		List<IDomainObject> domainObjectList = new ArrayList<IDomainObject>();
		Class<? extends IDomainObject> domainObjectClass = getMap().get(getDomainObjectConfigValue());
		if (domainObjectClass == null) {
			throw new ConfigNotFoundException(DOMAIN_OBJECT_CONFIG_KEY, getDomainObjectConfigValue());
		}

		domainObjectStringList.forEach(e -> {
			try {
				domainObjectList.add(domainObjectMapper.readValue(e, domainObjectClass));
			} catch (IOException exc) {
				Logger.getAnonymousLogger().log(Level.SEVERE,
						"Cannot map " + e + " to object of type " + domainObjectClass.getName(), exc);
			}
		});
		return domainObjectList;

	}

	protected Map<String, Class<? extends IDomainObject>> getMap() {
		return domainObjectMapping;
	}

	protected String getDomainObjectConfigValue() {
		return domainObjectConfigValue;
	}

}
