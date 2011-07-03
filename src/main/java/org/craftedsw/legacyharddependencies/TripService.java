package org.craftedsw.legacyharddependencies;

import java.util.ArrayList;
import java.util.List;

public class TripService {

	public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
		if (loggedUser() == null) {
			throw new UserNotLoggedInException();
		} 
		return getAllTripsForUser(user);
	}

	protected List<Trip> getAllTripsForUser(User user) {
		return user.isFriendsWith(loggedUser()) 
							? findTripsByUser(user)
		 					: new ArrayList<Trip>();
	}

	protected List<Trip> findTripsByUser(User user) {
		return TripDAO.findTripsByUser(user);
	}

	protected User loggedUser() {
		return UserSession.getInstance().getLoggedUser();
	}
	
}
