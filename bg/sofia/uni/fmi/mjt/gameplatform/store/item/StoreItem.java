package bg.sofia.uni.fmi.mjt.gameplatform.store.item;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StoreItem {
    String getTitle();
    BigDecimal getPrice();
    double getRating();
    LocalDateTime getReleaseDate();
    void setTitle(String title);
    void setPrice(BigDecimal price);
    void setReleaseDate(LocalDateTime releaseDate);
    void rate(double rating);
    void applyDiscount(int percent);
    int getRatingCount();
}