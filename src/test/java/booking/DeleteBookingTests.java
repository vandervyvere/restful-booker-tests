package booking;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import factories.BookingsDataFactory;
import factories.BookingsHttpFactory;
import models.GenericHttpResponseModel;
import models.RegisteredBookingModel;
import properties.BookingProperties;

public class DeleteBookingTests {
	
	private BookingsDataFactory _bookingsDataFactory = new BookingsDataFactory(); 
	private BookingsHttpFactory _bookingsHttpFactory = new BookingsHttpFactory(); 
	private ObjectMapper _objectMapper = new ObjectMapper();
	
	private RegisteredBookingModel _registeredBooking;

	@Before
	public void setUp() throws Exception {
		_registeredBooking = _objectMapper.readValue(
				_bookingsHttpFactory.createBooking(
						_bookingsDataFactory.getRandomBookingDetails()
						)
				.httpResponseBody, 
				RegisteredBookingModel.class);
	}

	@After
	public void tearDown() throws Exception {
		
		GenericHttpResponseModel getBookingResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, null, BookingProperties.AUTH_TYPE_COOKIE);
		
		if(getBookingResponse.statusCode == 200) {
			_bookingsHttpFactory.deleteRequest(BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, BookingProperties.AUTH_TYPE_COOKIE);
		}
	}

	@Test
	public void GivenValidBooking_WhenDeleteBookingIsCalled_ThenBookingShouldBeDeleted() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException {
		
		// Given
		Integer validBookingId = _registeredBooking.bookingid;
		
		// When
		GenericHttpResponseModel deleteResponse = _bookingsHttpFactory.deleteRequest(BookingProperties.BOOKING_URL + "/" + validBookingId, BookingProperties.AUTH_TYPE_COOKIE);
		GenericHttpResponseModel getBookingResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL + "/" + validBookingId, null, BookingProperties.AUTH_TYPE_NOAUTH);
		
		// Then
		assertEquals(HttpStatus.SC_CREATED, deleteResponse.statusCode);
		assertEquals("Created", deleteResponse.reasonPhrase);
		assertEquals(HttpStatus.SC_NOT_FOUND, getBookingResponse.statusCode);
		assertEquals("Not Found", getBookingResponse.reasonPhrase);
	}
	
	@Test
	public void GivenValidBooking_WhenDeleteBookingIsCalledWithBasicAuth_ThenBookingShouldBeDeleted() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException {
		
		// Given
		Integer validBookingId = _registeredBooking.bookingid;
		
		// When
		GenericHttpResponseModel deleteResponse = _bookingsHttpFactory.deleteRequest(BookingProperties.BOOKING_URL + "/" + validBookingId, BookingProperties.AUTH_TYPE_BASIC);
		GenericHttpResponseModel getBookingResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL + "/" + validBookingId, null, BookingProperties.AUTH_TYPE_NOAUTH);
		
		// Then
		assertEquals(HttpStatus.SC_CREATED, deleteResponse.statusCode);
		assertEquals("Created", deleteResponse.reasonPhrase);
		assertEquals(HttpStatus.SC_NOT_FOUND, getBookingResponse.statusCode);
		assertEquals("Not Found", getBookingResponse.reasonPhrase);
	}
	
	@Test
	public void GivenInValidBookingId_WhenDeleteBookingIsCalled_ThenStatusMethodNotAllowedIsReturned() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException {
		
		// Given
		Integer invalidBookingId = 75216456;
		
		// When
		GenericHttpResponseModel deleteResponse = _bookingsHttpFactory.deleteRequest(BookingProperties.BOOKING_URL + "/" + invalidBookingId, BookingProperties.AUTH_TYPE_COOKIE);
		GenericHttpResponseModel getBookingResponse = _bookingsHttpFactory.getRequest(BookingProperties.BOOKING_URL + "/" + invalidBookingId, null, BookingProperties.AUTH_TYPE_COOKIE);
		
		// Then
		assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, deleteResponse.statusCode);
		assertEquals("Method Not Allowed", deleteResponse.reasonPhrase);
		assertEquals(HttpStatus.SC_NOT_FOUND, getBookingResponse.statusCode);
		assertEquals("Not Found", getBookingResponse.reasonPhrase);
	}
	
	@Test
	public void GivenValidBooking_WhenDeleteBookingIsCalledWithNoAuth_ThenStatusForbiddenIsReturned() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException {
		
		// Given
		Integer validBookingId = _registeredBooking.bookingid;
		
		// When
		GenericHttpResponseModel deleteResponse = _bookingsHttpFactory.deleteRequest(BookingProperties.BOOKING_URL + "/" + validBookingId, BookingProperties.AUTH_TYPE_NOAUTH);
				
		// Then
		Assert.assertEquals(HttpStatus.SC_FORBIDDEN, deleteResponse.statusCode);	
		Assert.assertEquals("Forbidden", deleteResponse.reasonPhrase);
	}

}
