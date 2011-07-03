package org.craftedsw.legacyharddependencies2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CheckoutService_Original {

	public void makePayment(List<Item> items, User user) throws Exception {
		if (items.size() == 0) {
			return;
		}
		Card card = null;
		for (Card c : user.getCards()) {
			if (c.isMainCard() == true) {
				card = c;
				break;
			}
		}
		if (card == null) {
			throw new Exception("User has no card.");
		}
		List<Payment> payments = PaymentService.getUserPaymentHistory(user);
		for (Payment payment : payments) {
			if (payment.getState() == PaymentState.PENDING) {
				Date paymentDate = payment.getDate();
				Calendar c = new GregorianCalendar();
				c.setTime(paymentDate);
				c.add(Calendar.DAY_OF_MONTH, -30);
				Date date = c.getTime();
				if (paymentDate.before(date)) {
					throw new Exception("User has pending payment.");
				}
			}
		}
		double total = 0;
		double discount = 0;
		for (Item item : items) {
			total += item.getPrice();
			CategoryDiscountService categoryDiscountService = new CategoryDiscountService();
			List<CategoryDiscount> categoryDiscounts = categoryDiscountService.getCategoryDiscounts();
			for (CategoryDiscount categoryDiscount : categoryDiscounts) {
				if (categoryDiscount.getCategory().equals(item.getCategory())) {
					discount += (categoryDiscount.getDiscount() * item.getPrice()) / 100;
				}
			}
		}
		PaymentGateway paymentGateway = new PaymentGateway();
		if (!paymentGateway.processSale(user, card, total)) {
			throw new Exception("Payment could not be processed");
		}
		System.out.println("****************************************");
		System.out.println("* You payment has been processed.");
		System.out.println("* Items purchased: ");
		for (Item item : items) {
			System.out.println("* - " + item.getName());
		}
		System.out.println("* ");
		System.out.println("* Total: " + total);
		System.out.println("* Discount: " + discount);
		System.out.println("* ");
		System.out.println("* You paid: " + (total - discount));
	}
	
}
