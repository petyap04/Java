package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public final class VehicleAlreadyRentedException extends MyException {
    public VehicleAlreadyRentedException(){
        super("The vehicle has been already rented");
    }
}