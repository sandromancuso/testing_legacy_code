package org.craftedsw.legacyharddependencies2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CheckoutService {

	public void makePayment(Map<Item, Integer> basket, User user) throws Exception {
		if (basket.size() == 0) {
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
		List<Payment> payments = paymentHistoryFor(user);
		for (Payment payment : payments) {
			if (payment.getState() == PaymentState.PENDING) {
				Date paymentDate = payment.getDate();
				Calendar c = new GregorianCalendar();
				c.add(Calendar.MONTH, -1);
				Date date = c.getTime();
				if (paymentDate.before(date)) {
					throw new Exception("User has pending payment.");
				}
			}
		}
		double total = 0;
		double discount = 0;
		for (Entry<Item, Integer> item : basket.entrySet()) {
			total += item.getKey().getPrice() * item.getValue();
			CategoryDiscountService categoryDiscountService = categoryDiscountService();
			List<CategoryDiscount> categoryDiscounts = categoryDiscountService.getCategoryDiscounts();
			for (CategoryDiscount categoryDiscount : categoryDiscounts) {
				if (categoryDiscount.getCategory().equals(item.getKey().getCategory())) {
					discount += ((categoryDiscount.getDiscount() * item.getKey().getPrice()) / 100) * item.getValue();
				}
			}
		}
		PaymentGateway paymentGateway = paymentGateway();
		if (!paymentGateway.processSale(user, card, total)) {
			throw new Exception("Payment could not be processed");
		}
		System.out.println("****************************************");
		System.out.println("* You payment has been processed.");
		System.out.println("* Items purchased: ");
		for (Entry<Item, Integer> item : basket.entrySet()) {
			System.out.println("* - " + item.getKey().getName());
		}
		System.out.println("* ");
		System.out.println("* Total: " + total);
		System.out.println("* Discount: " + discount);
		System.out.println("* ");
		System.out.println("* You paid: " + (total - discount));
	}

	protected CategoryDiscountService categoryDiscountService() {
		return new CategoryDiscountService();
	}

	protected List<Payment> paymentHistoryFor(User user) {
		return PaymentService.getUserPaymentHistory(user);
	}

	protected PaymentGateway paymentGateway() {
		return new PaymentGateway();
	}
	
}
