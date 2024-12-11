package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {
    CAR,
    BIKE;

    private static final int CAR_PRICE = 5;
    private static final int CAR_TIME = 3;

    private static final int BIKE_PRICE = 3;
    private static final int BIKE_TIME = 5;

    public int pricePerKilometer() {
        return switch (this) {
            case CAR -> CAR_PRICE;
            case BIKE -> BIKE_PRICE;
        };
    }

    public int timePerKilometer() {
        return switch (this) {
            case CAR -> CAR_TIME;
            case BIKE -> BIKE_TIME;
        };
    }
}