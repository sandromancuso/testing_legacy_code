package org.craftedsw.legacyharddependencies2;

import java.util.ArrayList;
import java.util.List;

public class User {

	private List<Card> cards = new ArrayList<Card>();
	
	public List<Card> getCards() {
		return cards;
	}

	public void addCard(Card card) {
		cards.add(card);
	}

}
