package com.fergus.intercom.domain;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * Simple Pojo representing a customer object
 * 
 * @author kavanaghf
 *
 */
public class Customer implements IDomainObject {
	
	/*
	 * String to domainobject.name to refer to this object
	 */
	private static final String DOMAIN_NAME_IN_CONFIG = "customers";
	
	@JsonProperty("name")
	private String name;

	@JsonProperty("longitude")
	private Double longitude;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("user_id")
	private Integer userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Specifies how Customer object will be written to .txt file
	 */
	@Override
	public String toString() {
		return this.name +", "+ this.userId.toString();

	}

	/**
	 * 
	 * @return value in application.properties that is used to map to this class
	 */
	public static String getDomainNameInConfig() {
		return DOMAIN_NAME_IN_CONFIG;
	}

}
