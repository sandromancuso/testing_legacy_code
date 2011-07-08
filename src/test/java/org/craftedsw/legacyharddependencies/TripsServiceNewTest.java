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
import org.mockito.stubbing.OngoingStubbing;

public class TripsServiceNewTest {
	
	private static final User UNUSED_USER = null;
	private static final User USER_NOT_LOGGED_IN = null;
	
	protected User loggedUser = Mockito.mock(User.class);
	protected User targetUser = Mockito.mock(User.class);
	private List<Trip> targetUserTrips;
	private TripService tripService;

	@Before
	public void initialise() {
		tripService = createTripService();
	}

	@Test(expected=UserNotLoggedInException.class) public void 
	shouldThrowExceptionWhenUserIsNotLoggedIn() throws Exception {
		loggedUser = USER_NOT_LOGGED_IN;

		tripService.getTripsByUser(UNUSED_USER);
	}
	
	@Test public void 
	shouldNotReturnTripsWhenLoggedUserIsNotAFriend() throws Exception {		
		targetUserIsNotFriendsWith(loggedUser);
		
		List<Trip> trips = tripService.getTripsByUser(targetUser);
		 
		assertThat(trips.size(), is(equalTo(0)));
	}
	
	@Test public void 
	shouldReturnTripsWhenLoggedUserIsAFriend() throws Exception {
		targetUserIsFriendsWith(loggedUser);
		targetUserNumberOfTripsIs(2);
		 
		List<Trip> trips = tripService.getTripsByUser(targetUser);
		 
		assertThat(trips.size(), is(equalTo(2)));
	}

	private void targetUserIsNotFriendsWith(User user) {
		whenAskedIfTargetUserIsFriendsWith(user).thenReturn(false);
	}

	private void targetUserIsFriendsWith(User user) {
		whenAskedIfTargetUserIsFriendsWith(user).thenReturn(true);
	}	
	
	private OngoingStubbing<Boolean> whenAskedIfTargetUserIsFriendsWith(User user) {
		return when(targetUser.isFriendsWith(user));
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
				return TripsServiceNewTest.this.loggedUser;
			}
			@Override protected List<Trip> findTripsByUser(User user) {
				return TripsServiceNewTest.this.targetUserTrips;
			}
		};
	}	

}
