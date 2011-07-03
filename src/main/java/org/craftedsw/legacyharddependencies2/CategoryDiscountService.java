package org.craftedsw.legacyharddependencies2;

import java.util.List;

public class CategoryDiscountService {

	public List<CategoryDiscount> getCategoryDiscounts() {
	    throw new RuntimeException("CategoryDiscountService can not be invoked in a unit test.");
	}

}
