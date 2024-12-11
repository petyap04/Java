package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.Vector;

public class ControlCenter implements ControlCenterApi {
    MapEntity[][] map;

    public ControlCenter(MapEntity[][] map) {
        this.map = map;
    }

    public ControlCenter(char[][] map) {
        MapEntity[][] mapEntities = new MapEntity[map.length][map[0].length];
        for (int i = 0; i <= map.length; i++) {
            for (int j = 0; j <= map[0].length; j++) {
                mapEntities[i][j] = new MapEntity(new Location(i, j), MapEntityType.fromChar(map[i][j]));
            }
        }
        this.map = mapEntities;
    }

    public int shortestRoadForThisDeviveryGuy(Location restaurantLocation, Location clientLocation,
                                              Location currDeliveryGuy) {
        int rows = map.length;
        int colls = map[0].length;
        return restaurantLocation.shortestWayBetweenLocations(currDeliveryGuy, map, rows, colls) +
                restaurantLocation.shortestWayBetweenLocations(clientLocation, map, rows, colls);
    }


    private Vector<MapEntity> getAllDeliveryGuys() {
        Vector<MapEntity> deliveryGuysLocations = new Vector<>();
        for (MapEntity[] row : map) {
            for (MapEntity entity : row) {
                if (entity.type() == MapEntityType.DELIVERY_GUY_CAR || entity.type() == MapEntityType.DELIVERY_GUY_BIKE) {
                    deliveryGuysLocations.add(entity);
                }
            }
        }
        return deliveryGuysLocations;
    }

    private DeliveryInfo findBestDeliveryGuy(Vector<MapEntity> deliveryGuys, Location restaurantLocation,
                                             Location clientLocation, double maxPrice, int maxTime,
                                             ShippingMethod shippingMethod) {
        double minPrice = Double.MAX_VALUE;
        int minMin = Integer.MAX_VALUE;
        MapEntity bestDeliveryGuy = null;
        for (MapEntity currDeliveryGuy : deliveryGuys) {
            int currShortestRoad = shortestRoadForThisDeviveryGuy(restaurantLocation, clientLocation, currDeliveryGuy.location());
            DeliveryType deliveryType = (currDeliveryGuy.type() == MapEntityType.DELIVERY_GUY_BIKE) ? DeliveryType.BIKE : DeliveryType.CAR;
            int price = currShortestRoad * deliveryType.pricePerKilometer();
            int min = currShortestRoad * deliveryType.timePerKilometer();
            if (shippingMethod == ShippingMethod.CHEAPEST) {
                if ((price < minPrice || (price == minPrice && min < minMin)) &&
                        (min <= maxTime && price <= maxPrice)) {
                    minMin = min;
                    minPrice = price;
                    bestDeliveryGuy = currDeliveryGuy;
                }
            } else if (shippingMethod == ShippingMethod.FASTEST) {
                if ((min < minMin || (min == minMin && price < minPrice)) && (min <= maxTime && price <= maxPrice)) {
                    minMin = min;
                    minPrice = price;
                    bestDeliveryGuy = currDeliveryGuy;
                }
            }
        }
        if (bestDeliveryGuy == null) {
            return new DeliveryInfo(null, minPrice, minMin, null);
        }
        return new DeliveryInfo(bestDeliveryGuy.location(), minPrice, minMin, bestDeliveryGuy.type().returnType());
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation, double maxPrice,
                                               int maxTime, ShippingMethod shippingMethod) {
        if (restaurantLocation == null || clientLocation == null || shippingMethod == null || maxPrice < 0 ||
                maxTime < 0) {
            return null;
        }

        Vector<MapEntity> deliveryGuysLocations = getAllDeliveryGuys();
        if (deliveryGuysLocations.isEmpty()) {
            return null;
        }

        return findBestDeliveryGuy(deliveryGuysLocations, restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);
    }

    @Override
    public MapEntity[][] getLayout() {
        return map;
    }

}