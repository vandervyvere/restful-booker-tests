/**
 * 
 */
package booking;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import factories.BookingsDataFactory;
import factories.BookingsHttpFactory;
import models.BookingModel;
import models.BookingPartialModel;
import models.GenericHttpResponseModel;
import models.RegisteredBookingModel;
import properties.BookingProperties;

import org.junit.After;
import org.junit.Before;

/**
 * @author Edwardv
 *
 */
public class PartialUpdateBookingTests {

	private BookingsDataFactory _bookingsDataFactory = new BookingsDataFactory(); 
	private BookingsHttpFactory _bookingsHttpFactory = new BookingsHttpFactory(); 
	private ObjectMapper _objectMapper = new ObjectMapper();
	
	private RegisteredBookingModel _registeredBooking;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		_registeredBooking = _objectMapper.readValue(
				_bookingsHttpFactory.createBooking(
						_bookingsDataFactory.getRandomBookingDetails()
						)
				.httpResponseBody, 
				RegisteredBookingModel.class);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		_bookingsHttpFactory.deleteRequest(
				BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, 
				BookingProperties.AUTH_TYPE_COOKIE
				);
	}

	@Test
	public void GivenValidBooking_WhenPartialUpdateBookingIsCalled_ThenBookingShouldBeUpdated() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
				
		// Given		
		BookingPartialModel partialDetails = _bookingsDataFactory.getRandomPersonIdentity();
				
		// When
		GenericHttpResponseModel partialUpdate = _bookingsHttpFactory.patchRequest(
				partialDetails, 
				BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, 
				BookingProperties.AUTH_TYPE_COOKIE
				);
			
		BookingModel updatedBooking = _objectMapper.readValue(partialUpdate.httpResponseBody, BookingModel.class);
			
		// Then
		Assert.assertEquals(HttpStatus.SC_OK, partialUpdate.statusCode);	
		Assert.assertEquals(partialDetails.firstname, updatedBooking.firstname);
		Assert.assertEquals(partialDetails.lastname, updatedBooking.lastname);
			
	}
	
	@Test
	public void GivenValidBooking_WhenPartialUpdateBookingIsCalledWithNewFirstNameOnly_ThenBookingShouldBeUpdated() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
				
		// Given		
		BookingPartialModel partialDetails = _bookingsDataFactory.getRandomPersonWithFirstnameOnly();
				
		// When
		GenericHttpResponseModel partialUpdate = _bookingsHttpFactory.patchRequest(
				partialDetails, 
				BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, 
				BookingProperties.AUTH_TYPE_COOKIE
				);
			
		BookingModel updatedBooking = _objectMapper.readValue(partialUpdate.httpResponseBody, BookingModel.class);
			
		// Then
		Assert.assertEquals(HttpStatus.SC_OK, partialUpdate.statusCode);
		Assert.assertEquals(partialDetails.firstname, updatedBooking.firstname);		
			
	}
	
	@Test
	public void GivenValidBooking_WhenPartialUpdateBookingIsCalledWithNewLastNameOnly_ThenBookingShouldBeUpdated() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
				
		// Given		
		BookingPartialModel partialDetails = _bookingsDataFactory.getRandomPersonWithLastNameOnly();
				
		// When
		GenericHttpResponseModel partialUpdate = _bookingsHttpFactory.patchRequest(
				partialDetails, 
				BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, 
				BookingProperties.AUTH_TYPE_COOKIE
				);
			
		BookingModel updatedBooking = _objectMapper.readValue(partialUpdate.httpResponseBody, BookingModel.class);
			
		// Then
		Assert.assertEquals(HttpStatus.SC_OK, partialUpdate.statusCode);		
		Assert.assertEquals(partialDetails.lastname, updatedBooking.lastname);
			
	}
	
	@Test
	public void GivenInValidBookingId_WhenPartialUpdateBookingIsCalled_ThenStatusMethodNotAllowedIsreturned() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
				
		// Given		
		BookingPartialModel partialDetails = _bookingsDataFactory.getRandomPersonWithLastNameOnly();
		Integer invalidBookingId = 1125687;
				
		// When
		GenericHttpResponseModel partialUpdate = _bookingsHttpFactory.patchRequest(
				partialDetails, 
				BookingProperties.BOOKING_URL + "/" + invalidBookingId, 
				BookingProperties.AUTH_TYPE_COOKIE
				);		
					
		// Then
		Assert.assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, partialUpdate.statusCode);
		Assert.assertEquals("Method Not Allowed", partialUpdate.reasonPhrase);

	}
	
	@Test
	public void GivenValidBookingId_WhenPartialUpdateBookingIsCalledWithBasicAuth_ThenBookingShouldBeUpdated() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
				
		// Given		
		BookingPartialModel partialDetails = _bookingsDataFactory.getRandomPersonIdentity();
				
		// When
		GenericHttpResponseModel partialUpdate = _bookingsHttpFactory.patchRequest(
				partialDetails, 
				BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, 
				BookingProperties.AUTH_TYPE_BASIC
				);		
		
		BookingModel updatedBooking = _objectMapper.readValue(partialUpdate.httpResponseBody, BookingModel.class);
			
		// Then
		Assert.assertEquals(HttpStatus.SC_OK, partialUpdate.statusCode);	
		Assert.assertEquals(partialDetails.firstname, updatedBooking.firstname);
		Assert.assertEquals(partialDetails.lastname, updatedBooking.lastname);
		
			
	}
	
	@Test
	public void GivenValidBookingId_WhenPartialUpdateBookingIsCalledWithNoAuth_ThenStatusForbiddenIsReturned() throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
				
		// Given		
		BookingPartialModel partialDetails = _bookingsDataFactory.getRandomPersonIdentity();
				
		// When
		GenericHttpResponseModel partialUpdate = _bookingsHttpFactory.patchRequest(
				partialDetails, 
				BookingProperties.BOOKING_URL + "/" + _registeredBooking.bookingid, 
				BookingProperties.AUTH_TYPE_NOAUTH
				);		
		
		// Then
		Assert.assertEquals(HttpStatus.SC_FORBIDDEN, partialUpdate.statusCode);	
		Assert.assertEquals("Forbidden", partialUpdate.reasonPhrase);
			
	}

}
