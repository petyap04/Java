package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.math.BigDecimal.valueOf;

public class Game implements StoreItem{
    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private String genre;
    private double rating;
    private int ratingCount;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.rating = 0.0;
        this.ratingCount = 0;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public int getRatingCount(){
        return ratingCount;
    }
}