package bg.sofia.uni.fmi.mjt.vehiclerent;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;

import java.time.LocalDateTime;

public class RentalService {

    LocalDateTime startOfRent;

    public void rentVehicle(Driver driver, Vehicle vehicle, LocalDateTime startOfRent) {
        if(driver == null || vehicle == null || startOfRent == null){
            throw new IllegalArgumentException();
        }
        this.startOfRent = startOfRent;
        vehicle.rent(driver,startOfRent);
    }

    public double returnVehicle(Vehicle vehicle, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if(vehicle == null && endOfRent == null){
            throw new IllegalArgumentException();
        }
        assert vehicle != null;
        vehicle.returnBack(endOfRent);
        return vehicle.calculateRentalPrice(startOfRent, endOfRent);
    }
}
