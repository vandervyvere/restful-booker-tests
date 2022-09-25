package helpers;

public class AssertFailedMessageHelper {

	public String generateInfoForFailedSearch(String url, String queryParamters) {
		
		String combinedUrl = url + queryParamters;
		
		String message = 	"\n" +
							"Search failed: \n" +
							combinedUrl +
							"\n";
		return message;
	}
	
}
