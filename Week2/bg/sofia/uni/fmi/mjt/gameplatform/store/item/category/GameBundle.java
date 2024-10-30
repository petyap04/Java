package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.math.BigDecimal.valueOf;

public class GameBundle implements StoreItem{
    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private Game[] games;
    private double rating;
    int ratingCount;

    public GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games){
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.games = games;
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public double getRating() {
        return rating;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        this.rating = (this.rating * ratingCount + rating);
        ratingCount++;
        this.rating /= ratingCount;
    }

    @Override
    public void applyDiscount(int percent) {
        BigDecimal mult = valueOf((double)(100-percent)/100);
        price = price.multiply((mult));
    }

    @Override
    public int getRatingCount() {
        return ratingCount;
    }

}