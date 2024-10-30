package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public final class VehicleNotRentedException extends MyException {
    public VehicleNotRentedException(){
        super("There has been mistake with the renting. You didn't rent the vehicle");
    }
}