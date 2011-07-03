package org.craftedsw.legacyharddependencies2;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {

	private static final Map<Item, Integer> EMPTY_BASKET = new HashMap<Item, Integer>();
	private static final User UNUSED_USER = null;
	
	private CheckoutService checkoutService;
	private List<Payment> paymentHistory;
	
	@Mock
	private PaymentGateway paymentGateway;
	@Mock
	private CategoryDiscountService categoryDiscountService;
	private Map<Item, Integer> basket;
	private User user;
	
	@Before
	public void initialise() {
		user = new User();
		basket = new HashMap<Item, Integer>();
		checkoutService = aCheckoutService();
		paymentHistory = new ArrayList<Payment>();
	}
	
	@Test public void 
	shouldNotProcessSaleIfNoItemsAreBeingBought() throws Exception {
		checkoutService.makePayment(EMPTY_BASKET, UNUSED_USER);
		salesShouldNeverBeProcessed();
	}
	
	@Test(expected=Exception.class) public void 
	shouldThrowAnExceptionIfUserHasNoCreditCards() throws Exception {
		checkoutService.makePayment(aBasketWithOneItem(), user);
		salesShouldNeverBeProcessed();
	}

	@Test(expected=Exception.class) public void 
	shouldThrowExceptionWhenUserHasNoCreditCardsSetToBeTheMainCardToBeUsed() throws Exception {
		 checkoutService.makePayment(aBasketWithOneItem(), aUserWithoutMainCard());
		 salesShouldNeverBeProcessed();
	}

	@Test(expected=Exception.class) public void 
	shouldThrowExceptionIfUserHasPendentPaymentsForOverAMonth() throws Exception {
		 aBasketWithOneItem();
		 Card mainCard = new Card();
		 mainCard.setMainCard(true);
		 user.addCard(mainCard);
		 Payment payment = new Payment();
		 Calendar c = new GregorianCalendar();
		 c.add(Calendar.MONTH, -2);
		 payment.setDate(c.getTime());
		 payment.setState(PaymentState.PENDING);
		 paymentHistory.add(payment);
		 
		 checkoutService.makePayment(basket, user);
		 
		 salesShouldNeverBeProcessed();		 
	}

	private User aUserWithoutMainCard() {
		user.addCard(new Card());
		return user;
	}

	private boolean salesShouldNeverBeProcessed() {
		return verify(paymentGateway, never()).processSale(
						any(User.class), any(Card.class), anyDouble());
	}	
	
	private Map<Item, Integer> aBasketWithOneItem() {
		basket.put(new Item(), 10);
		return basket;
	}
	
	protected CheckoutService aCheckoutService() {
		return new CheckoutService() {
			@Override
			protected PaymentGateway paymentGateway() {
				return CheckoutServiceTest.this.paymentGateway;
			}
			@Override
			protected List<Payment> paymentHistoryFor(User user) {
				return CheckoutServiceTest.this.paymentHistory;
			}
			@Override
			protected CategoryDiscountService categoryDiscountService() {
				return CheckoutServiceTest.this.categoryDiscountService;
			}
			
		};
	}
}
