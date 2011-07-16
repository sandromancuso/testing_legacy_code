package org.craftedsw.legacyharddependencies;

import static org.craftedsw.legacyharddependencies.UserBuilder.anUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TripServiceFourthRunTest {
	
	private User NON_LOGGED_USER = null;
	private User UNUSED_USER = new User();
	private User LOGGED_USER = new User();
	
	private TripServiceThridRun tripService;
	
	@Before
	public void initialise() {
		tripService = createTripService();
	}
	
	@Test(expected = UserNotLoggedInException.class) public void 
	shouldThrowExceptionIfUserIsNotLoggedIn() throws Exception {
		tripService.getFriendTrips(NON_LOGGED_USER, UNUSED_USER);
	}
	
	@Test public void 
	shouldNotReturnTripsWhenLoggedUserIsNotAFriend() throws Exception {
		List<Trip> friendTrips = tripService.getFriendTrips(LOGGED_USER, anUser().build());
		
		assertThat(friendTrips.size(), is(equalTo(0)));
	}
	
	@Test public void 
	shouldReturnTripsWhenLoggedUserIsAFriend() throws Exception {
		User john = anUser().friendsWith(LOGGED_USER)
				            .withTrips(new Trip(), new Trip())
				            .build();

		List<Trip> friendTrips = tripService.getFriendTrips(LOGGED_USER, john);
		
		assertThat(friendTrips, is(equalTo(john.trips())));
	}

	protected TripServiceThridRun createTripService() {
		return new TripServiceThridRun() {
			@Override protected List<Trip> findTripsByUser(User user) {
				return user.trips();
			}
		};
	}

}
