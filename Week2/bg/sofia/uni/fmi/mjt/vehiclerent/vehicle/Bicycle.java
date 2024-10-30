package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Bicycle extends Vehicle{
    double pricePerDay;
    double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id,model);
        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if(startOfRent.isAfter(endOfRent)){
            throw new InvalidRentingPeriodException();
        }

        if(driver == null){
            throw new IllegalArgumentException();
        }
        double sum = 0;

        final int minutesInHour = 60;
        final int hoursInDay = 24;
        final int daysInWeek = 7;
        long totalMinutes = Duration.between(startOfRent, endOfRent).toMinutes();
        long days = totalMinutes / (minutesInHour * hoursInDay);
        long remainingMinutesAfterDays = totalMinutes % (minutesInHour * hoursInDay);
        long hours = remainingMinutesAfterDays / minutesInHour;
        sum = days * pricePerDay + hours * pricePerHour;

        return sum;
    }
}