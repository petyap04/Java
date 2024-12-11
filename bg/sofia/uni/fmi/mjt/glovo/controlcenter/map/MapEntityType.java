package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;

public enum MapEntityType {
    ROAD('.'), WALL('#'), RESTAURANT('R'), CLIENT('C'), DELIVERY_GUY_CAR('A'), DELIVERY_GUY_BIKE('B');

    private final char symbol;

    MapEntityType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static MapEntityType fromChar(char c) {
        for (MapEntityType type : MapEntityType.values()) {
            if (type.symbol == c) {
                return type;
            }
        }
        throw new IllegalArgumentException("No MapEntityType for character: " + c);
    }

    public DeliveryType returnType() {
        if (this == DELIVERY_GUY_CAR) {
            return DeliveryType.CAR;
        } else if (this == DELIVERY_GUY_BIKE) {
            return DeliveryType.BIKE;
        }
        return null;
    }
}