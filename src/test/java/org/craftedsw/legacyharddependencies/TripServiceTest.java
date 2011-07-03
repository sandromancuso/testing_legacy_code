package org.craftedsw.legacyharddependencies;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TripServiceTest {
	
	private static final User UNUSED_USER = null;
	private static final User NOT_LOGGED_USER = null;
	protected User loggedUser = Mockito.mock(User.class);
	protected User targetUser = Mockito.mock(User.class);
	private TripService tripService;
	private List<Trip> targetUserTrips;

	@Before
	public void initialise() {
		tripService = createTripService();
	}
	
	@Test(expected=UserNotLoggedInException.class) public void 
	shouldThrowExceptionWhenUserIsNotLoggedIn() throws Exception {
		loggedUser = NOT_LOGGED_USER;
		 
		tripService.getTripsByUser(UNUSED_USER);
	}
	
	@Test public void 
	shouldNotReturnTripsWhenLoggedUserIsNotAFriend() throws Exception {		
		when(targetUser.isFriendsWith(loggedUser)).thenReturn(false);
		
		List<Trip> trips = tripService.getTripsByUser(targetUser);
		 
		assertThat(trips.size(), is(equalTo(0)));
	}
	
	@Test public void 
	shouldReturnTripsWhenLoggedUserIsAFriend() throws Exception {
		when(targetUser.isFriendsWith(loggedUser)).thenReturn(true);
		targetUserNumberOfTripsIs(2);
		 
		List<Trip> trips = tripService.getTripsByUser(targetUser);
		 
		assertThat(trips.size(), is(equalTo(2)));
	}
	
	private void targetUserNumberOfTripsIs(int numberOfTrips) {
		targetUserTrips = new ArrayList<Trip>();
		for (int cnt = 0; cnt < numberOfTrips; cnt++) {
			targetUserTrips.add(new Trip());
		}
	}

	private TripService createTripService() {
		return new TripService() {
			@Override protected User loggedUser() {
				return TripServiceTest.this.loggedUser;
			}
			@Override protected List<Trip> findTripsByUser(User user) {
				return TripServiceTest.this.targetUserTrips;
			}
		};
	}
	
}
