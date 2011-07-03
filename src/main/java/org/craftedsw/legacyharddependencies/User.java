package org.craftedsw.legacyharddependencies;

import java.util.List;

public class User {

	public List<User> getFriends() {
		return null;
	}
	
	public boolean isFriendsWith(User user) {
		boolean isFriend = false;
		for (User friend : user.getFriends()) {
			if (friend.equals(user)) {
				isFriend = true;
				break;
			}
		}
		return isFriend;
	}

}
