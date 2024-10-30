package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public interface ItemFilter {
    boolean matches(StoreItem item);
}