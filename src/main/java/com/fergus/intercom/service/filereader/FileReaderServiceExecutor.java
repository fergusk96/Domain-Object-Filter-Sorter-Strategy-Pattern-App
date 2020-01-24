package com.fergus.intercom.service.filereader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fergus.intercom.App;
import com.fergus.intercom.exception.ConfigNotFoundException;
import com.fergus.intercom.modules.BasicIntercomModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * Determines correct strategy to read file.
 * 
 * Strategy selected is based on value read from config - filereader.type
 * 
 * Once strategy to execute is found, executeRead method is called for
 * corresponding implementation of {@link IFileReaderService}
 * 
 * @author kavanaghf
 *
 */
public class FileReaderServiceExecutor {

	/*
	 * Map that lists configuration strings to concrete implemetation of
	 * IFileReaderService
	 */
	private final Map<String, Class<? extends IFileReaderService>> fileReaderServiceMapping = new HashMap<>();

	
	/*
	 * Value in application.properties used to specify reading strategy
	 */
	protected static final String FILE_TYPE_CONFIG_KEY = "filereader.type";

	protected String fileReaderTypeConfigValue = App.getProperties().getProperty(FILE_TYPE_CONFIG_KEY);

	/*
	 * fileReader implementation to be used for readFileAsStringList()
	 */
	protected IFileReaderService fileReaderServiceImplementation;


	/**
	 * Populate map with implementation of {@link IFileReaderService} and config
	 * string that corresponds to this implementation
	 */
	public FileReaderServiceExecutor() {

		fileReaderServiceMapping.put("local", LocalFileReaderService.class);

	}

	/**
	 * 
	 * Retrieves correct implemetation of {@link IFileReaderService} from map.
	 * 
	 * Calls readFileAsStringList() for that implementation
	 * 
	 * @return list of domainObjects read from .txt file as Strings - to mapped
	 *         to object by mapper class
	 * @throws IOException
	 * @throws ConfigNotFoundException
	 */
	public List<String> executeRead() throws IOException, ConfigNotFoundException {
		IFileReaderService fileReaderService = getFileReaderServiceInstance(getFileReaderTypeConfigValue());
		this.setFileReaderServiceImplementation(fileReaderService);
		return getFileReaderServiceImplementation().readFileAsStringList();
	}

	/**
	 * 
	 * Retrieves correct implemetation of {@link IFileReaderService} from map.
	 * 
	 * returns instance of this class through injection
	 * 
	 * @param fileReaderTypeConfigValue
	 * @return instance of class from map specified by config
	 * @throws ConfigNotFoundException
	 *             - relevant config not found in application.properties
	 */
	protected IFileReaderService getFileReaderServiceInstance(final String fileReaderTypeConfigValue)
			throws ConfigNotFoundException {

		// Find class specified from config in strategies map
		Class<? extends IFileReaderService> serviceClass = getMap().get(fileReaderTypeConfigValue);

		/*
		 * If no implementation class found, then value in
		 * application.properties has not been mapped to any implemetation
		 */

		if (serviceClass == null) {
			throw new ConfigNotFoundException(FILE_TYPE_CONFIG_KEY, fileReaderTypeConfigValue);
		}

		return injectServiceClass(serviceClass);
	}

	/**
	 * 
	 * Return instance of IFileReaderService from class object
	 * 
	 * @param serviceClass - class object for implementation class of IFileReaderService
	 * @return instance of serviceClass injected through Guice
	 */
	protected IFileReaderService injectServiceClass(Class<? extends IFileReaderService> serviceClass) {
		Injector injector = Guice.createInjector(new BasicIntercomModule());
		return injector.getInstance(serviceClass);
	}

	/**
	 * 
	 * @return map from Strings to implementations of IFileReaderService
	 */
	protected Map<String, Class<? extends IFileReaderService>> getMap() {
		return fileReaderServiceMapping;
	}
	
	/**
	 * @return value in application.properties corresponding to the FILE_TYPE_CONFIG_KEY
	 */
	protected String getFileReaderTypeConfigValue() {
		return fileReaderTypeConfigValue;
	}

	public IFileReaderService getFileReaderServiceImplementation() {
		return fileReaderServiceImplementation;
	}

	public void setFileReaderServiceImplementation(IFileReaderService fileReaderServiceImplementation) {
		this.fileReaderServiceImplementation = fileReaderServiceImplementation;
	}
}
