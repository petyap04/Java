package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.math.BigDecimal;

public class PriceItemFilter implements ItemFilter{
    BigDecimal lowerBound;
    BigDecimal upperBound;
    public PriceItemFilter(BigDecimal lowerBound, BigDecimal upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    @Override
    public boolean matches(StoreItem item) {
        return item.getPrice().compareTo(lowerBound) >= 0 && item.getPrice().compareTo(upperBound) <= 0;
    }
}