package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

public interface StoreAPI {
    StoreItem[] findItemByFilters(ItemFilter[] itemFilters);
    void applyDiscount(String promoCode);
    boolean rateItem(StoreItem item, int rating);
}