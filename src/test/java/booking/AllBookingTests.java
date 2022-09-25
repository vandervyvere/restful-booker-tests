package booking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DeleteBookingTests.class, GetBookingIdsTests.class, PartialUpdateBookingTests.class })
public class AllBookingTests {

}
