package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public enum FuelType{
    DIESEL,
    PETROL,
    HYBRID,
    ELECTRICITY,
    HYDROGEN;

    int tax(){
        int result = switch (this){
            case DIESEL, PETROL -> 3;
            case HYBRID -> 1;
            case ELECTRICITY, HYDROGEN -> 0;
        };
        return result;
    }
}