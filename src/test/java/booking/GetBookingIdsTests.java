package booking;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import factories.BookingsHttpFactory;
import helpers.AssertFailedMessageHelper;
import models.BookingIdModel;
import models.BookingModel;
import models.GenericHttpResponseModel;
import properties.BookingProperties;

public class GetBookingIdsTests {

	private AssertFailedMessageHelper _failedMessageHelper = new AssertFailedMessageHelper();
	private BookingsHttpFactory _bookingsHttpFactory = new BookingsHttpFactory(); 
	private ObjectMapper _objectMapper = new ObjectMapper();
	private BookingIdModel[] _bookingIdList = null;
	
	@Before
	public void Setup() throws ClientProtocolException, IOException {
		_bookingIdList = _bookingsHttpFactory.getExistingBookingIds();
	}
	
	@Test
	public void WhenGetBookingIdsIsCalled_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, null, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue("BookingIds do contain records", bookingIds.length > 0);
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByName_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?firstname=" + existingBooking.firstname.replace("\"", "") + 
								"&lastname=" + existingBooking.lastname.replace("\"", "");
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do not contain records", bookingIds.length > 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByFirstNameOnly_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?firstname=" + existingBooking.firstname.replace("\"", "");
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do not contain records", bookingIds.length > 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByNoneExistingFirstName_ThenEmptyListIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		String noneExistingFirstName = "sdsdsdfgdf";
		String qureyParamters = "?firstname=" +noneExistingFirstName;
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, 0) + "BookingIds do contain records", bookingIds.length == 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByLasNameOnly_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?lastname=" + existingBooking.lastname.replace("\"", "");
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do not contain records", bookingIds.length > 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByDates_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?checkin=" + existingBooking.bookingdates.checkin +
								"&checkout=" + existingBooking.bookingdates.checkout;
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do not contain records", bookingIds.length > 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByCheckInDateOnly_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?checkin=" + existingBooking.bookingdates.checkin;
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do not contain records", bookingIds.length > 0);		
		
	}

	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByCheckOutDateOnly_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?checkout=" + existingBooking.bookingdates.checkout;
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) +  "BookingIds do not contain records", bookingIds.length > 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByInvalidCheckinDate_ThenStatusInternalServerErrorIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		String invalidCheckinDate = "23-02-2013";
		String qureyParamters = "?checkin=" + invalidCheckinDate;
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
				
		// Then
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, getBookingIdsResponse.statusCode);
		assertEquals("Internal Server Error", getBookingIdsResponse.reasonPhrase);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByAllParameters_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?firstname=" + existingBooking.firstname.replace("\"", "") +
								"&lastname=" + existingBooking.lastname.replace("\"", "") +
								"&checkin=" + existingBooking.bookingdates.checkin +
								"&checkout=" + existingBooking.bookingdates.checkout;
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);
		
		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do contain records", bookingIds.length > 0);		
		
	}
	
	@Test
	public void GivenExistingBooking_WhenGetBookingIdsIsFilteredByCheckInAndFirstName_ThenListOfBookingIdsIsReturned() throws ClientProtocolException, IOException {
		
		// Given
		Integer randomIdIndex = ThreadLocalRandom.current().nextInt(0, _bookingIdList.length);
		GenericHttpResponseModel existingBookingResponse = _bookingsHttpFactory.getRequest(
				BookingProperties.BOOKING_URL + "/" + _bookingIdList[randomIdIndex].bookingid,
				null,
				BookingProperties.AUTH_TYPE_NOAUTH
				);
		BookingModel existingBooking = _objectMapper.readValue(
				existingBookingResponse.httpResponseBody,
				BookingModel.class);
		String qureyParamters = "?checkin=" + existingBooking.bookingdates.checkin +
								"&firstname=" + existingBooking.firstname.replace("\"", "");
				
		// When
		GenericHttpResponseModel getBookingIdsResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL, qureyParamters, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIds = _objectMapper.readValue(getBookingIdsResponse.httpResponseBody, BookingIdModel[].class);

		// Then
		assertEquals(HttpStatus.SC_OK, getBookingIdsResponse.statusCode);
		assertTrue(_failedMessageHelper.generateInfoForFailedSearch(BookingProperties.BOOKING_URL, qureyParamters, _bookingIdList[randomIdIndex].bookingid) + "BookingIds do not contain records", bookingIds.length > 0);		
		
	}
}
