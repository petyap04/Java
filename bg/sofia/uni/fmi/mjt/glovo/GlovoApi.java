package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public interface GlovoApi {

    Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException;

    Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException;

    Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem, double maxPrice)
            throws NoAvailableDeliveryGuyException;

    Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem, int maxTime)
            throws NoAvailableDeliveryGuyException;

}