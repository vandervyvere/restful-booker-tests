package factories;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import models.BookingModel;
import models.BookingPartialModel;
import models.Bookingdates;
import models.CredentialModel;

public class BookingsDataFactory {
	
	private Faker _bogus = new Faker();
	private DateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public CredentialModel getLoginDetails() {
		CredentialModel credentials = new CredentialModel();
		credentials.username = "admin";
		credentials.password = "password123";
		
		return credentials;
		
	}
	
	public BookingModel getRandomBookingDetails() {
		
		BookingPartialModel randomPersonIdentity = getRandomPersonIdentity();
		
		BookingModel booking = new BookingModel();
		booking.firstname = randomPersonIdentity.firstname;
		booking.lastname = randomPersonIdentity.lastname;
		booking.additionalneeds = _bogus.food().dish();
		booking.bookingdates = generateRandomBookingDates();
		booking.totalprice = 1045;
		
		return booking;
	}
	
	public BookingPartialModel getRandomPersonIdentity() {
		BookingPartialModel identity = new BookingPartialModel();
		identity.firstname = _bogus.name().firstName();
		identity.lastname = _bogus.name().lastName();
		
		return identity;
	}
	
	public BookingPartialModel getRandomPersonWithFirstnameOnly() {
		BookingPartialModel identity = new BookingPartialModel();
		identity.firstname = _bogus.name().firstName();
		
		return identity;
	}
	
	public BookingPartialModel getRandomPersonWithLastNameOnly() {
		BookingPartialModel identity = new BookingPartialModel();
		identity.lastname = _bogus.name().lastName();
		
		return identity;
	}
	
	public Bookingdates generateRandomBookingDates() {
		
		Bookingdates bookingDates = new Bookingdates();
		bookingDates.checkin = _dateFormat.format(_bogus.date().future(2, TimeUnit.DAYS));
		bookingDates.checkout = _dateFormat.format(_bogus.date().future(5, TimeUnit.DAYS));
		
		return bookingDates;		
	}
}
