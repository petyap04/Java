package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

public interface ControlCenterApi {

    DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation,
                                        double maxPrice, int maxTime, ShippingMethod shippingMethod);

    MapEntity[][] getLayout();

}