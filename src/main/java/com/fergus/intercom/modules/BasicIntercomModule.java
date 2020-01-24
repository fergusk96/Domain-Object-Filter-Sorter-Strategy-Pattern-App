package com.fergus.intercom.modules;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import com.fergus.intercom.service.MathHelperService;
import com.fergus.intercom.service.filereader.FileReaderServiceExecutor;
import com.fergus.intercom.service.objectmapper.DomainObjectMapperServiceExecutor;
import com.fergus.intercom.service.sortfilter.CustomerFilterAndSortService;
import com.google.inject.AbstractModule;

/**
 * 
 * Module to specify bindings for Guice to inject service classes
 * 
 * @author kavanaghf
 *
 */
public class BasicIntercomModule extends AbstractModule {

	@Override
	protected void configure() {

		Constructor<FileReaderServiceExecutor> fileReaderServiceExecutorConstructor;
		Constructor<CustomerFilterAndSortService> customerSortAndFilterServiceConstructor;
		Constructor<DomainObjectMapperServiceExecutor> domainObjectMapperServiceExecutorConstructor;
		Constructor<MathHelperService> mathHelperServiceConstructor;
		
		try {
			fileReaderServiceExecutorConstructor = FileReaderServiceExecutor.class.getConstructor();
			bind(FileReaderServiceExecutor.class).toConstructor(fileReaderServiceExecutorConstructor);
			
			customerSortAndFilterServiceConstructor = CustomerFilterAndSortService.class.getConstructor();
			bind(CustomerFilterAndSortService.class).toConstructor(customerSortAndFilterServiceConstructor);
			
			domainObjectMapperServiceExecutorConstructor = DomainObjectMapperServiceExecutor.class.getConstructor();
			bind(DomainObjectMapperServiceExecutor.class).toConstructor(domainObjectMapperServiceExecutorConstructor);
			
			mathHelperServiceConstructor = MathHelperService.class.getConstructor();
			bind(MathHelperService.class).toConstructor(mathHelperServiceConstructor);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getAnonymousLogger(e.getMessage());
		}

	}

}
