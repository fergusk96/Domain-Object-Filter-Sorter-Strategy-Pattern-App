package com.fergus.intercom.service.filereader

import org.apache.commons.io.IOUtils
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.spockframework.runtime.Sputnik

import com.fergus.intercom.App

import spock.lang.Specification



/**
 *
 * Unit tests for com.fergus.intercom.serivce.filereader.LocalFileReader
 *
 * @author kavanaghf
 *
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Sputnik.class)
@PrepareForTest(IOUtils.class)
class LocalFileReaderServiceSpec extends Specification {

	private static final String TEST_DOMAIN_OBJECT_NAME = "testDomainObjectName"

	def localFileReaderService;


	def "setup"() {
		/*
		 * PowerMockito used to map static methods
		 */
		PowerMockito.mockStatic(IOUtils.class)
		Properties properties = Stub(Properties.class)
		properties.getProperty("domainobject.name") >> TEST_DOMAIN_OBJECT_NAME;
		localFileReaderService = new LocalFileReaderService();
		localFileReaderService.setProperties(properties);
	}

	def "TEST getFileUri"(){
		given: "Properties are defined for application"

		when: "getFileUri method is called"
			def testUri = localFileReaderService.getFileUri();

		then: "uri is equal to name read from properties with .txt added"
			testUri == TEST_DOMAIN_OBJECT_NAME+".txt"
	}

	def "TEST readFileAsList - happy path"(){
		given: "Properties are defined for application"
			def app = Mock(App.class)
			def inputStream = Stub(InputStream.class)
			localFileReaderService.setApplication(app)
		
		and: "Static method to read inputStream exists"
			Mockito.when(IOUtils.readLines(inputStream, "UTF-8")).thenReturn(new ArrayList<String>());
			
				
		when: "readFileAsList method is called"
			def testList = localFileReaderService.readFileAsStringList()
			
		then: "Input stream is converted to list"
			1 * app.getFileFromResources(_) >> inputStream;
			testList != null;
	}
}
