package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

public class GameStore implements StoreAPI{
    private final StoreItem[] availableItems;
    boolean hasBeenApplyedDiscount;
    public GameStore(StoreItem[] availableItems){
        this.availableItems = availableItems;
    }
    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        StoreItem[] arr = new StoreItem[availableItems.length];
        int sizeOfArr=0;
        for(StoreItem item:availableItems){
            boolean matches = true;
            for(ItemFilter currFilter:itemFilters){
                if(!currFilter.matches(item)){
                    matches = false;
                    break;
                }
            }
            if(matches){
                arr[sizeOfArr] = item;
                sizeOfArr++;
            }
        }
        StoreItem[] result = new StoreItem[sizeOfArr];
        for(int i=0;i<sizeOfArr;i++){
            result[i]=arr[i];
        }
        return result;
    }

    @Override
    public void applyDiscount(String promoCode) {
        if(!hasBeenApplyedDiscount && promoCode.equals("VAN40")){
            for(StoreItem i:availableItems){
                i.applyDiscount(40);
                hasBeenApplyedDiscount = true;
            }
        }
        else if(promoCode.equals("100YO")){
            for(StoreItem i:availableItems){
                i.applyDiscount(100);
            }
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if(rating >= 1 && rating <= 5){
            item.rate(rating);
            return true;
        }
        return false;
    }
}

