package properties;

public class BookingProperties {
	public static String BOOKING_DOMAIN = "restful-booker.herokuapp.com";
	public static String BOOKING_BASE_ADDRESS = "https://restful-booker.herokuapp.com";
	public static String AUTH_PATH = "/auth";
	public static String BOOKING_PATH = "/booking";
	

	public static String AUTH_URL = BOOKING_BASE_ADDRESS + AUTH_PATH;
	public static String BOOKING_URL = BOOKING_BASE_ADDRESS + BOOKING_PATH;
	
	public static String AUTH_TYPE_COOKIE = "useCookie";
	public static String AUTH_TYPE_BASIC = "useBasic";
	public static String AUTH_TYPE_NOAUTH = null;
}
