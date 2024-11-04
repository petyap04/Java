package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;

public abstract sealed class Vehicle permits Bicycle, Car, Caravan{
    Driver driver;
    String id;
    String model;
    boolean hasBeenRented;
    LocalDateTime startRentTime;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
    }

    public void rent(Driver driver, LocalDateTime startRentTime) {
        if(hasBeenRented){
            throw new VehicleAlreadyRentedException();
        }
        if(driver == null){
            throw new IllegalArgumentException();
        }
        this.driver = driver;
        hasBeenRented = true;
        this.startRentTime = startRentTime;
    }

    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if(!hasBeenRented){
            throw new VehicleNotRentedException();
        }
        if(rentalEnd == null){
            throw new IllegalArgumentException();
        }
        hasBeenRented = false;
    }

    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent)
            throws InvalidRentingPeriodException;

}
