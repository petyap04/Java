package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {

    ControlCenter controlCenter;

    public Glovo(char[][] map) {
        MapEntity[][] mapEntities = new MapEntity[map.length][map[0].length];
        for (int i = 0; i <= map.length; i++) {
            for (int j = 0; j <= map[0].length; j++) {
                mapEntities[i][j] = new MapEntity(new Location(i, j), MapEntityType.fromChar(map[i][j]));
            }
        }
        controlCenter = new ControlCenter(mapEntities);
    }

    public Glovo(MapEntity[][] map) {
        controlCenter = new ControlCenter(map);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {
        if (client == null) {
            throw new IllegalArgumentException("Client can't be null!");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant can't be null!");
        }
        if (foodItem == null || foodItem.isEmpty()) {
            throw new IllegalArgumentException("Food item has to be specified!");
        }

        DeliveryInfo info =
                controlCenter.findOptimalDeliveryGuy(restaurant.location(), client.location(), Double.MAX_VALUE,
                        Integer.MAX_VALUE, ShippingMethod.CHEAPEST);

        //this means there is no delivery guy on the map
        if (info == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy right now");
        }

        return new Delivery(client.location(), restaurant.location(), info.deliveryGuyLocation(), foodItem,
                info.price(), info.estimatedTime());
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {
        if (client == null) {
            throw new IllegalArgumentException("Client can't be null!");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant can't be null!");
        }
        if (foodItem == null || foodItem.isEmpty()) {
            throw new IllegalArgumentException("Food item has to be specified!");
        }

        DeliveryInfo info =
                controlCenter.findOptimalDeliveryGuy(restaurant.location(), client.location(), Double.MAX_VALUE,
                        Integer.MAX_VALUE, ShippingMethod.FASTEST);

        //this means there is no delivery guy on the map
        if (info == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy right now");
        }

        return new Delivery(client.location(), restaurant.location(), info.deliveryGuyLocation(), foodItem,
                info.price(), info.estimatedTime());
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem,
                                                 double maxPrice) throws NoAvailableDeliveryGuyException {
        if (client == null) {
            throw new IllegalArgumentException("Client can't be null!");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant can't be null!");
        }
        if (foodItem == null || foodItem.isEmpty()) {
            throw new IllegalArgumentException("Food item has to be specified!");
        }

        DeliveryInfo info =
                controlCenter.findOptimalDeliveryGuy(restaurant.location(), client.location(), maxPrice, Integer.MAX_VALUE,
                        ShippingMethod.FASTEST);

        //this means there is no delivery guy on the map
        if (info == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy right now");
        }

        //this means there is no delivery guy who can provide this delivery
        if (info.deliveryGuyLocation() == null) {
            throw new InvalidOrderException("This order is invalid");
        }

        return new Delivery(client.location(), restaurant.location(), info.deliveryGuyLocation(), foodItem,
                info.price(), info.estimatedTime());
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem,
                                                       int maxTime) throws NoAvailableDeliveryGuyException {
        if (client == null) {
            throw new IllegalArgumentException("Client can't be null!");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant can't be null!");
        }
        if (foodItem == null || foodItem.isEmpty()) {
            throw new IllegalArgumentException("Food item has to be specified!");
        }

        DeliveryInfo info =
                controlCenter.findOptimalDeliveryGuy(restaurant.location(), client.location(), Double.MAX_VALUE, maxTime,
                        ShippingMethod.FASTEST);

        //this means there is no delivery guy on the map
        if (info == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy right now");
        }

        //this means there is no delivery guy who can provide this delivery
        if (info.deliveryGuyLocation() == null) {
            throw new InvalidOrderException("This order is invalid");
        }

        return new Delivery(client.location(), restaurant.location(), info.deliveryGuyLocation(), foodItem,
                info.price(), info.estimatedTime());
    }
}