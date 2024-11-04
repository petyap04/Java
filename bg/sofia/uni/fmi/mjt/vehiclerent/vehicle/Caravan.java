package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Caravan extends Vehicle{
    FuelType fuelType;
    int numberOfSeats;
    int numberOfBeds;
    double pricePerWeek;
    double pricePerDay;
    double pricePerHour;
    static final int priceOfSeat;
    static final int priceOfBed;

    static{
        priceOfSeat = 5;
        priceOfBed = 10;
    }

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek,
                   double pricePerDay, double pricePerHour) {
        super(id,model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.numberOfBeds = numberOfBeds;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (Duration.between(startOfRent, endOfRent).toHours() < 24 || startOfRent.isAfter(endOfRent)
        || startOfRent == null || endOfRent == null) {
            throw new InvalidRentingPeriodException();
        }

        double sum = 0;

        final int minutesInHour = 60;
        final int hoursInDay = 24;
        final int daysInWeek = 7;
        long totalMinutes = Duration.between(startOfRent, endOfRent).toMinutes();
        long weeks = totalMinutes / (minutesInHour * hoursInDay * daysInWeek);
        long remainingMinutes = totalMinutes % (minutesInHour * hoursInDay * daysInWeek);
        long days = remainingMinutes / (minutesInHour * hoursInDay);
        remainingMinutes %= (minutesInHour * hoursInDay);
        long hours = remainingMinutes / minutesInHour;
        sum = weeks * pricePerWeek + days * pricePerDay + hours * pricePerHour;

        long fuelTax = fuelType.tax() * (weeks * daysInWeek + days + (hours > 0 ? 1:0 ));
        sum += fuelTax;

        sum += driver.tax();

        sum += priceOfSeat * numberOfSeats + priceOfBed * numberOfBeds;

        return sum;
    }
}