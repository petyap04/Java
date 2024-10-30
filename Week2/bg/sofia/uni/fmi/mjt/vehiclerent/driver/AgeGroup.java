package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup{
    JUNIOR,
    EXPERIENCED,
    SENIOR;

    int tax(){
        int result;
        result = switch (this){
            case JUNIOR -> 10;
            case EXPERIENCED -> 0;
            case SENIOR -> 15;
        };
        return result;
    }
}