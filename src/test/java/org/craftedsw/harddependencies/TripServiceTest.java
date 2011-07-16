package org.craftedsw.harddependencies;

import static org.craftedsw.harddependencies.UserBuilder.anUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.craftedsw.harddependencies.exception.UserNotLoggedInException;
import org.craftedsw.harddependencies.trip.Trip;
import org.craftedsw.harddependencies.user.User;
import org.junit.Before;
import org.junit.Test;

public class TripServiceTest {
	
	private static final User UNUSED_USER = null;
	private static final User NO_LOGGED_USER = null;
	private User loggedUser = new User();
	private User targetUser = new User();
	private TripService tripService;

	@Before
	public void initialise() {
		tripService = createTripService();
	}
	
	@Test(expected=UserNotLoggedInException.class) public void 
	shouldThrowExceptionWhenUserIsNotLoggedIn() throws Exception {
		loggedUser = NO_LOGGED_USER;
		 
		tripService.getTripsByUser(UNUSED_USER);
	}
	
	@Test public void 
	shouldNotReturnTripsWhenLoggedUserIsNotAFriend() throws Exception {		
		List<Trip> trips = tripService.getTripsByUser(targetUser);
		 
		assertThat(trips.size(), is(equalTo(0)));
	}
	
	@Test public void 
	shouldReturnTripsWhenLoggedUserIsAFriend() throws Exception {
		User john = anUser().friendsWith(loggedUser)
					        .withTrips(new Trip(), new Trip())
					        .build();
		 
		List<Trip> trips = tripService.getTripsByUser(john);
		 
		assertThat(trips, is(equalTo(john.trips())));
	}

	private TripService createTripService() {
		return new TripService() {
			@Override protected User loggedUser() {
				return loggedUser;
			}
			@Override protected List<Trip> findTripsByUser(User user) {
				return user.trips();
			}
		};
	}
	
}
