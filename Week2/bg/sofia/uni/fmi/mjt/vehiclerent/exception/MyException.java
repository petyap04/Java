package bg.sofia.uni.fmi.mjt.vehiclerent.exception;
import java.lang.RuntimeException;

public abstract sealed class MyException extends RuntimeException
        permits InvalidRentingPeriodException, VehicleAlreadyRentedException, VehicleNotRentedException, IllegalArgumentException {
    MyException(String message){
        super(message);
    }
}