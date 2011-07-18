package org.craftedsw.harddependencies;

import static org.craftedsw.harddependencies.UserBuilder.anUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.craftedsw.harddependencies.exception.UserNotLoggedInException;
import org.craftedsw.harddependencies.trip.Trip;
import org.craftedsw.harddependencies.trip.TripService;
import org.craftedsw.harddependencies.user.User;
import org.junit.Before;
import org.junit.Test;

public class TripServiceTest {
	
	private User NON_LOGGED_USER = null;
	private User UNUSED_USER = new User();
	private User LOGGED_USER = new User();
	
	private TripService tripService;
	
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
		
		assertTrue(friendTrips.isEmpty());
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
