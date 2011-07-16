package org.craftedsw.legacyharddependencies;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UserTest {

	@Test public void 
	shouldReturnTrueWhenUsersAreFriends() throws Exception {
		User John = new User();
		User Bob = new User();
		
		John.addFriend(Bob);
		
		assertThat(John.isFriendsWith(Bob), is(true));
	}
	
}
