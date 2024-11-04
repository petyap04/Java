package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class TitleItemFilter implements ItemFilter{
    String title;
    boolean caseSensitive;

    public TitleItemFilter(String title, boolean caseSensitive){
        this.title = title;
        this.caseSensitive = caseSensitive;
    }
    @Override
    public boolean matches(StoreItem item) {
        if(caseSensitive){
            return item.getTitle().equals(title);
        }
        else{
            return item.getTitle().equalsIgnoreCase(title);
        }
    }
}