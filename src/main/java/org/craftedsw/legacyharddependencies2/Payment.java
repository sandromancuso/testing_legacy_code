package org.craftedsw.legacyharddependencies2;

import java.util.Date;

public class Payment {

	private Date date;
	private PaymentState state;

	public PaymentState getState() {
		return state;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setState(PaymentState state) {
		this.state = state;
	}

}
