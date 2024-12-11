package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record Delivery(Location client, Location restaurant, Location deliveryGuy,
                       String foodItem, double price, int estimatedTime) {  }