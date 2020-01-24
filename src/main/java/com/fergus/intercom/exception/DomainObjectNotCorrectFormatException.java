package com.fergus.intercom.exception;

public class DomainObjectNotCorrectFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2954816059829059376L;

	public DomainObjectNotCorrectFormatException(String domainObject, String classname) {
		super(domainObject + "in file does can't be mapped to class"+classname);
	}

}
