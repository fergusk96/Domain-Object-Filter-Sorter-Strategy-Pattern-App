package com.fergus.intercom.service

import com.fergus.intercom.modules.BasicIntercomModule
import com.google.inject.Guice
import com.google.inject.Injector

import spock.lang.Specification


/**
 * Unit test class for com.fergus.intercom.service.MathHelperService
 * @author fergus
 *
 */
class MathHelperServiceSpec extends Specification {

	def mathHelperService;

	private static final Double LONDON_LATITUDE = 51.508530;
	private static final Double LONDON_LONGITUDE = -0.076132;

	private static final Double INTERCOM_DUBLIN_LATITUDE = 53.339428;
	private static final Double INTERCOM_DUBLIN_LONGITUDE = -6.257664;

	private static final Double INTERCOM_LONDON_ACTUAL_DISTANCE_IN_KMS = 463.00;

	private static final Double ALLOWED_ERROR_IN_KMS = 10.00;

	def "setup"() {
		//Inject mathHelperService into test class
		Injector basicIntercomModuleInjector = Guice.createInjector(new BasicIntercomModule());
		mathHelperService =  basicIntercomModuleInjector.getInstance(MathHelperService.class)
	}

	def "TEST haversineDistance - measures distance correctly"(){
		given: "mathHelperService instanciated, coordinates of two locations known and actual distance known"


		when: "haversineDistance is called on coordinates"
		def haversineDistance = mathHelperService.haversineDistance(INTERCOM_DUBLIN_LATITUDE,INTERCOM_DUBLIN_LONGITUDE,LONDON_LATITUDE,LONDON_LONGITUDE)

		then: "returned distance is correct within a margin of error"
		Math.abs(haversineDistance - INTERCOM_LONDON_ACTUAL_DISTANCE_IN_KMS) < ALLOWED_ERROR_IN_KMS;
	}

	def "TEST haversineDistance is associative - Distance from Dublin to London = Distance from London to Dublin"(){
		given: "mathHelperService instanciated, coordinates of two locations known and actual distance known"

		when: "haversineDistance is called twice, changing order in which arguments are passed"
		def haversineDistance1 = mathHelperService.haversineDistance(INTERCOM_DUBLIN_LATITUDE,INTERCOM_DUBLIN_LONGITUDE,LONDON_LATITUDE,LONDON_LONGITUDE)
		def haversineDistance2 = mathHelperService.haversineDistance(LONDON_LATITUDE,LONDON_LONGITUDE,INTERCOM_DUBLIN_LATITUDE,INTERCOM_DUBLIN_LONGITUDE)

		then: "haversineDistance method returns same value in both cases"
		haversineDistance1 == haversineDistance2
	}
}
