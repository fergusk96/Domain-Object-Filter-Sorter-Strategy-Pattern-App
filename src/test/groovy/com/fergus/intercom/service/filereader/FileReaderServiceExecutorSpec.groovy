package com.fergus.intercom.service.filereader

import com.fergus.intercom.exception.ConfigNotFoundException
import com.fergus.intercom.modules.BasicIntercomModule
import com.google.inject.Guice
import com.google.inject.Injector

import spock.lang.Specification


/**
 *
 * Unit tests for com.fergus.intercom.serivce.filereader.FileReaderServiceExecutor
 *
 * @author kavanaghf
 *
 */
class FileReaderServiceExecutorSpec extends Specification {

	private static final String TEST_ARRAYlIST_ELEMENT = "testString";

	def fileReaderServiceExecutor;


	def "setup"() {
		//Inject fileReaderServiceExecutor into test class
		Injector basicIntercomModuleInjector = Guice.createInjector(new BasicIntercomModule());
		fileReaderServiceExecutor =  basicIntercomModuleInjector.getInstance(FileReaderServiceExecutor.class)
	}

	def "TEST map is populated with correct amount of elements  - this test will be updated as more implementations of IFileReaderService are added"(){
		given: "fileReaderServiceExecutor has been injected into class"
				
		when: "the number of elements in the map is class map is checked"
		def mapSize = fileReaderServiceExecutor.getMap().size()
		
		then: "the map size is equal to 1"
		mapSize == 1;
	}

	def "TEST when config value is set to string contained in map, corresponding readerService is executed"(){
		given: "fileReaderServiceExecutor has been injected into class, file type in config is local"

		fileReaderServiceExecutor = new TestFileReaderServiceExecutor();

		when: "executeRead method is called"
		def listOfDomainObjects = fileReaderServiceExecutor.executeRead()

		then: "Correct implementation of executeRead is called"
		fileReaderServiceExecutor.getFileReaderServiceImplementation().getClass() == TestFileReaderService.class
		listOfDomainObjects.get(0) == TEST_ARRAYlIST_ELEMENT;
	}

	def "NEG TEST when config returns string not in map error is thrown"(){
		given: "fileReaderServiceExecutor has been injected into class, file type in config is string that does not correspond to any implementation of fileReaderService"
		fileReaderServiceExecutor = new TestFileReaderServiceExecutor() {
					@Override
					protected String getFileReaderTypeConfigValue() {
						return "doesn't exist in map";
					}
				}

		when: "executeRead method is called"
		def listOfDomainObjects = fileReaderServiceExecutor.executeRead()

		then: "Correct service class (local) is returned"
		thrown ConfigNotFoundException
	}

	
	/**
	 *
	 * Create local TestFileReaderService so our test doesn't rely on any one implementation
	 * @author kavanaghf
	 *
	 */
	class TestFileReaderService implements IFileReaderService{
		@Override
		public List<String> readFileAsStringList() throws IOException {
			List<String> testList = new ArrayList();
			testList.add(TEST_ARRAYlIST_ELEMENT);
			return testList;
		}

		@Override
		public String getFileUri() {
			return "";
		}
	}

	
	
	/**
	 *
	 * Override certain methods so tests don't depend on any one map configuration
	 * or value from application.properties
	 *
	 * Test becomes more general
	 *
	 * @author kavanaghf
	 *
	 */
	class TestFileReaderServiceExecutor extends FileReaderServiceExecutor{

		private static final String TEST_FILE_TYPE = "testFileReaderType"

		@Override
		protected Map<String, Class<? extends IFileReaderService>> getMap() {
			Map<String, Class<? extends IFileReaderService>> testMap = new HashMap();
			testMap.put(TEST_FILE_TYPE, TestFileReaderService.class);
			return testMap;
		}
		@Override
		protected IFileReaderService injectServiceClass(Class<? extends IFileReaderService> serviceClass) {
			return new TestFileReaderService();
		}
		@Override
		protected String getFileReaderTypeConfigValue() {
			return TEST_FILE_TYPE;
		}
	}
}
