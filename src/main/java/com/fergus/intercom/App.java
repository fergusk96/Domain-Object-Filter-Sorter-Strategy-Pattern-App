package com.fergus.intercom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fergus.intercom.domain.IDomainObject;
import com.fergus.intercom.exception.ConfigNotFoundException;
import com.fergus.intercom.modules.BasicIntercomModule;
import com.fergus.intercom.service.filereader.FileReaderServiceExecutor;
import com.fergus.intercom.service.filereader.IFileReaderService;
import com.fergus.intercom.service.objectmapper.DomainObjectMapperServiceExecutor;
import com.fergus.intercom.service.sortfilter.DomainObjectFilterAndSortExecutor;
import com.fergus.intercom.service.sortfilter.IDomainFilterAndSortService;
import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * Entry point to application
 * 
 * The application generalises the reading, mapping, sorting and filtering of an
 * object represented by JSON through the strategy pattern
 * 
 * For any list of objects represented through JSON, the object list will first
 * be read from a file to a list of JSON strings through a class that implements
 * {@link IFileReaderService}.
 * 
 * The list of JSON strings then mapped to objects that implement the interface
 * {@link IDomainObject}.
 * 
 * This list is then filtered and sorted through a service class that implements
 * {@link IDomainFilterAndSortService}
 * 
 * The strategy for the readFile, mapToObject and sortAndFilter operations
 * specified through configuration in application.properties file
 * 
 * {@link FileReaderServiceExecutor}, {@link DomainObjectMapperServiceExecutor}
 * and {@link DomainObjectFilterAndSortExecutor} handle the selection of the
 * correct concrete implementation of each of these strategies as specified in
 * application.properties
 * 
 * Right now strategies for customer is the only implementation available.
 * 
 * To add a new strategy, simply add a new domain object (e.g. Customer), add to
 * the maps in each Executor class that map between configuration value and
 * class implementations, then finally add a concrete implementation of
 * {@link IDomainFilterAndSortService} for the newly added object
 * 
 * @author kavanaghf
 *
 */
public class App {

	/*
	 * File to be written to project base folder containing filtered and sorted
	 * domain objects
	 */
	protected static final String OUTPUT_TXT = "output.txt";

	/*
	 * Decides strategy to use when reading file
	 * then performs read
	 */
	static FileReaderServiceExecutor fileReaderServiceExecutor;

	/*
	 * Decides strategy to use when mapping JSON string list domain object list
	 * then performs object mapping
	 */
	static DomainObjectMapperServiceExecutor domainObjectMapperServiceExecutor;

	/*
	 * Decides strategy to use when filtering and sorting domain object list
	 * then performs execution
	 */
	static DomainObjectFilterAndSortExecutor filterAndSortExecutor;

	/*
	 * Single source of truth for properties read from application.properties.
	 * 
	 * These properties will be read by strategy executor classes
	 */
	protected static final Properties PROPS;

	
	static {
		PROPS = new Properties();
		try {
			InputStream propertyFileInputStream = new App().getFileFromResources("application.properties");
			PROPS.load(propertyFileInputStream);
		} catch (IOException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Property file could not be read", e);
		}
	}

	/**
	 * Instances of our three strategy executor classes are injected into main
	 * class.
	 * 
	 * These are responsible for performing reading, mapping to object and
	 * sorting/filtering respectively
	 * 
	 * The returned list of objects that implement {@link IDomainObject} are
	 * written to a .txt file based on the toString() method of each element of
	 * the list.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ConfigNotFoundException
	 */
	public static void main(String[] args) throws IOException, ConfigNotFoundException {

		Injector basicIntercomModuleInjector = Guice.createInjector(new BasicIntercomModule());

		// Read file as list of string objects
		fileReaderServiceExecutor = basicIntercomModuleInjector.getInstance(FileReaderServiceExecutor.class);
		List<String> domainObjectStringList = fileReaderServiceExecutor.executeRead();

		// Map list of string objects to domain object
		domainObjectMapperServiceExecutor = basicIntercomModuleInjector
				.getInstance(DomainObjectMapperServiceExecutor.class);
		List<IDomainObject> domainObjectList = domainObjectMapperServiceExecutor
				.getDomainObjectList(domainObjectStringList);

		// Sort and filter domain object list with correct service for object
		// type
		filterAndSortExecutor = basicIntercomModuleInjector.getInstance(DomainObjectFilterAndSortExecutor.class);
		List<IDomainObject> sortedAndFilteredDomainObjectList = filterAndSortExecutor
				.sortAndFilterDomainObjectList(domainObjectList);

		// Write to .txt file
		writeDomainObjectListToFile(sortedAndFilteredDomainObjectList, OUTPUT_TXT);

	}

	/**
	 * 
	 * After the correct strategies have been applied, write the sorted and
	 * filtered objects to a .txt file.
	 * 
	 * @param sortedAndFilteredDomainObjectList
	 *            - result of sorting and filtering our list of domain objects
	 * @throws IOException 
	 */
	protected static void writeDomainObjectListToFile(List<IDomainObject> sortedAndFilteredDomainObjectList, String fileName) throws IOException {
		BufferedWriter fileWritter = Files.newWriter(new File(fileName), Charset.defaultCharset());
		
		sortedAndFilteredDomainObjectList.forEach(e -> {
			try {
				fileWritter.write(e.toString());
				fileWritter.newLine();
			} catch (IOException e1) {
				Logger.getAnonymousLogger(e1.getMessage());
			}
		});
		fileWritter.close();

	}

	/**
	 * Get file from classpath, resources folder
	 * 
	 * @param fileName
	 *            - name of file to search for in src/main/resources
	 * @return inputStream representing file that has been read
	 */
	public InputStream getFileFromResources(String fileName) {

		ClassLoader classLoader = App.class.getClassLoader();
		return classLoader.getResourceAsStream(fileName);

	}

	/**
	 * 
	 * @return properties configured in Application.properties
	 */
	public static Properties getProperties(){
		return PROPS;
	}


}
