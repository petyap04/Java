package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public class Driver{

    AgeGroup group;

    public Driver(AgeGroup group){
        this.group = group;
    }

    public int tax(){
        return group.tax();
    }
}