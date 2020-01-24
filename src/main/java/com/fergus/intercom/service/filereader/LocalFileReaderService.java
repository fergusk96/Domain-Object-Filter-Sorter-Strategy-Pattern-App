package com.fergus.intercom.service.filereader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.fergus.intercom.App;

/**
 * 
 * Implentation of IFileReaderService for files that are stored locally within
 * the src/main/resources folder
 * 
 * @author kavanaghf
 *
 */
public class LocalFileReaderService implements IFileReaderService {

	protected Properties properties = App.getProperties();
	protected App applicaton;

	public LocalFileReaderService() {
		this.applicaton = new App();
	}

	/**
	 * 
	 * Reads .txt file from src/main/resources
	 * the .txt file read is dependant on domainobject.name in application.properties
	 * file should be a list json representing a domain object
	 * 
	 * @return list of json strings representing domain object
	 * 
	 */
	@Override
	public List<String> readFileAsStringList() throws IOException {
		InputStream fileAsInputStream = this.applicaton.getFileFromResources(getFileUri());
		List<String> domainObjectStringList = IOUtils.readLines(fileAsInputStream, "UTF-8");
		return domainObjectStringList;
	}

	/**
	 * 
	 * Files for lists of domainObjects be the name of the domainObject with
	 * .txt appended
	 * 
	 * @return relative path of file from src/main/resources
	 * 
	 */
	@Override
	public String getFileUri() {
		return getProperties().getProperty("domainobject.name") + ".txt";
	}

	/**
	 * Setter extracted to method for testing
	 * 
	 * @param applicaton
	 *            - source of truth for configurations read from
	 *            application.properties
	 */
	protected void setApplication(App applicaton) {
		this.applicaton = applicaton;
	}

	/**
	 * 
	 * Getter extracted to method for testing
	 * 
	 * @return list of properties from application.properties
	 */
	protected Properties getProperties() {
		return properties;
	}

	/**
	 * 
	 * Setter extracted to method for testing
	 * 
	 * @param properties
	 *            properties to be used by strategy executor classes
	 */
	protected void setProperties(Properties properties) {
		this.properties = properties;
	}

}
