package com.fergus.intercom.service

import com.fergus.intercom.service.filereader.LocalFileReaderService

import spock.lang.Specification

class LocalFileReaderServiceIntegrationSpec extends Specification {
	
	def localFileReaderService;
	
	def "setup"(){
		
		/*
		 * Set uri to be test file for integration test
		 */
		localFileReaderService = new LocalFileReaderService() {
			@Override
			public String getFileUri() {
				return "testFile.txt";
			}
		}
		
	}
	
	def "TEST - read testFile from src/main/resources"(){
		given: "localFileReaderService injected into class" 
		
		when: "readFileAsStringList called"
			def stringList = localFileReaderService.readFileAsStringList()
		then: "file read is as expected"
			stringList.size() == 2;
		
		
		
	}
	
}
