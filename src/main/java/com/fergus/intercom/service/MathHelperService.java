package com.fergus.intercom.service;


/**
 * 
 * Service class to be used to implement mathematical formulae
 * 
 * @author kavanaghf
 *
 */
public class MathHelperService {

	/**
	 * 
	 * Calculate the haversine distance between two locations 
	 * based on their coordinates. Haversine distance is associative,
	 * haversineDistance(location1,location2) == haversineDistance(location2,location1)
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula" for reference formula
	 * 
	 * @param lat1 - latitude of location 1
	 * @param lon1 - longitude of location 1
	 * @param lat2 - latitude of location 2
	 * @param lon2 - longitude of location 2
	 * @return distance between two locations in KMs
	 */
	public Double haversineDistance(Double lat1, Double lon1, Double lat2, Double lon2) {

		Double latitudeDistance = Math.toRadians(lat2 - lat1);
		Double longitudeDistance = Math.toRadians(lon2 - lon1);

		Double latitude1InRadians = Math.toRadians(lat1);
		Double latitude2InRadians = Math.toRadians(lat2);

		Double haversineLatitudeDistance = Math.pow(Math.sin(latitudeDistance / 2), 2);
		Double haversineLonitudeDistance = Math.pow(Math.sin(longitudeDistance / 2), 2);

		Double c1 = haversineLatitudeDistance
				+ (Math.cos(latitude1InRadians) * Math.cos(latitude2InRadians) * haversineLonitudeDistance);

		Double rootc1 = Math.sqrt(c1);

		Double c2 = Math.asin(rootc1);
		
		Double earthRadius = 6371.00; // Radius of the earth in KMs
		
		return 2 * earthRadius * c2;
		
		
	}

}
