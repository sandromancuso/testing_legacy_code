package org.craftedsw.legacyharddependencies;

import java.util.ArrayList;
import java.util.List;

public class TripServiceThridRun {
 
	public List<Trip> getFriendTrips(User loggedUser, User friend) throws UserNotLoggedInException {
		validate(loggedUser);
		return (friend.isFriendsWith(loggedUser)) 
					? findTripsByUser(friend)
				    : new ArrayList<Trip>();
	}

	private void validate(User loggedUser) throws UserNotLoggedInException {
		if (loggedUser == null) {
			throw new UserNotLoggedInException();
		}
	}

	protected List<Trip> findTripsByUser(User user) {
		return TripDAO.findTripsByUser(user);
	}
	
}
