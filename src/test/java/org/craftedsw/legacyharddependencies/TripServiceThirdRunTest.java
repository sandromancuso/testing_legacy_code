package org.craftedsw.legacyharddependencies;

import static org.craftedsw.legacyharddependencies.UserBuilder.anUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TripServiceThirdRunTest {

	private static final User UNUSED_FRIEND = new User();
	private static final User LOGGED_USER = new User();
	private static final User NON_LOGGED_USER = null;
	private TripService tripService;

	@Before
	public void intialise() {
		tripService = createTripService();
	}
	
	@Test(expected = UserNotLoggedInException.class) public void 
	shouldThrowExceptionWhenUserIsNotLoggedIn() throws Exception {
		tripService.getFriendTrips(NON_LOGGED_USER, UNUSED_FRIEND);
	}
	
	@Test public void 
	shouldNotReturnTripsWhenLoggedUserIsNotAFriend() throws Exception {
		List<Trip> friendTrips = tripService.getFriendTrips(LOGGED_USER, new User());
		
		assertThat(friendTrips.isEmpty(), is(equalTo(true)));
	}
	
	@Test public void 
	shouldReturnTripsWhenLoggedUserIsAFriend() throws Exception {
		User john = anUser().friendsWith(LOGGED_USER)
				            .withTrips(new Trip(), new Trip())
				            .build();
		
		List<Trip> friendTrips = tripService.getFriendTrips(LOGGED_USER, john);
		
		assertThat(friendTrips, is(equalTo(john.trips())));
	}

	private TripService createTripService() {
		return new TripService() {
			@Override protected List<Trip> findTripsForFriend(User user) {
				return user.trips();
			}
		};
	}
	
}
