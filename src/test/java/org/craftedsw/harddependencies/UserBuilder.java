package org.craftedsw.harddependencies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.craftedsw.harddependencies.trip.Trip;
import org.craftedsw.harddependencies.user.User;

public class UserBuilder {
	
	List<User> friends = new ArrayList<User>();
	List<Trip> trips = new ArrayList<Trip>();

	public static UserBuilder anUser() {
		return new UserBuilder();
	}
	
	public UserBuilder friendsWith(User friend) {
		friends.add(friend);
		return this;
	}
	
	public UserBuilder withTrips(Trip... trips) {
		this.trips.addAll(Arrays.asList(trips));
		return this;
	}
	
	public User build() {
		User user = new User();
		addFriends(user);
		addTrips(user);
		return user;
	}

	private void addTrips(User user) {
		for (Trip trip : trips) {
			user.addTrip(trip);
		}
	}

	private void addFriends(User user) {
		for (User friend : friends) {
			user.addFriend(friend);
		}
	}
}
