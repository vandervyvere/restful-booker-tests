package helpers;

public class AssertFailedMessageHelper {

	public String generateInfoForFailedSearch(String url, String queryParamters, Integer existingBookingId) {
		
		String combinedUrl = url + queryParamters;
		
		String message = 	"\n" +
							"BookingId used to use filter paramters of: " + existingBookingId + "\n" +
							"Search failed: \n" +
							combinedUrl +
							"\n";
		return message;
	}
	
}
