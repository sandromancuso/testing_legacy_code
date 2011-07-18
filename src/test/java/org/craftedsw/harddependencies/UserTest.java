package org.craftedsw.harddependencies;

import static org.junit.Assert.assertTrue;

import org.craftedsw.harddependencies.user.User;
import org.junit.Test;

public class UserTest {

	@Test public void 
	shouldReturnTrueWhenUsersAreFriends() throws Exception {
		User John = new User();
		User Bob = new User();
		
		John.addFriend(Bob);
		
		assertTrue(John.isFriendsWith(Bob));
	}
	
}
