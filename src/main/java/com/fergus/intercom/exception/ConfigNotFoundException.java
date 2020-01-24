package com.fergus.intercom.exception;



/**
 * Customer exception found when the value specified in application.properties
 * has not been mapped to any class implementation
 * 
 * @author kavanaghf
 */
public class ConfigNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ConfigNotFoundException(String configName) {
		super("No config found for "+configName);
		
	}

	public ConfigNotFoundException(String configKey, String configValue) {
		super("No match in map for config name:"+configKey+" value: "+configValue);
	}
	
}
