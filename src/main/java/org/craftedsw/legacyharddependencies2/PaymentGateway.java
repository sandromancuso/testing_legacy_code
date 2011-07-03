package org.craftedsw.legacyharddependencies2;

public class PaymentGateway {

	public boolean processSale(User user, Card card, double total) {
		throw new RuntimeException("PaymentGateway can not be invoked in a unit test.");
	}

}
