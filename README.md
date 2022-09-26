## Restful Booking API test regression pack
This repository contains some basic automated tests that are excersising the restful-booking API.

### Scope
The scope of this test suite is limited the following methods:

-  [Booking](https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking)
    * [GetBookingIds
](https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-GetBookings) Test that the following conditions are functional 
        + A list of bookingids are retrieved with no filter.
        + A list of bookingids are retrieved with a filter of one or two parameters.
    * [DeleteBooking
](https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-DeleteBooking)
    * [PartialUpdateBooking](https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-PartialUpdateBooking)

### 🐞Bugs🐞
The following potential bugs were found:
1. [GetBookingIds: No records returned when filtered with checkin and firstname parameters](https://github.com/vandervyvere/restful-booker-tests/issues/3)
2. [GetBookingIds: No records returned when filtered with all parameters](https://github.com/vandervyvere/restful-booker-tests/issues/2)
   - All though the requirement states to use a filter with 1 or 2 parameters, something to look at potentially.

### ToDo's
Things that can be improved upon this repository.
- Reduce repetitive code in the test classes
- Add additional missing tests
  - Bad requests with invalid packets
  - Test XML results(only JSON coverage, if that would make a difference)
  - Possibly duplicate tests to use both cookie and basic auth for update and delete methods.

