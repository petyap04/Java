package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public record Location(int x, int y) {

    private boolean processNeighbors(Location currLocation, Location target, Queue<Location> queue,
                                     Map<Location, Boolean> isVisited, MapEntity[][] map, int rows, int cols) {
        int currX = currLocation.x;
        int currY = currLocation.y;

        for (int i = currX - 1; i <= currX + 1; i++) {
            if (i >= 0 && i < rows) {
                for (int j = currY - 1; j <= currY + 1; j++) {
                    if (j >= 0 && j < cols && (currX + currY + 1 == i + j || currX + currY - 1 == i + j)) {
                        Location newLocation = map[i][j].location();
                        if (newLocation.x == target.x && newLocation.y == target.y) {
                            return true;
                        }
                        if (isVisited.getOrDefault(newLocation, false)) {
                            continue;
                        }
                        isVisited.put(newLocation, true);
                        if (map[i][j].type() == MapEntityType.WALL) {
                            continue;
                        }
                        queue.add(newLocation);
                    }
                }
            }
        }
        return false;
    }

    public int shortestWayBetweenLocations(Location other, MapEntity[][] map, int rows, int cols) {
        if (other.equals(this)) {
            return 0;
        }

        int currLevel = 1;
        Queue<Location> queue = new ArrayDeque<>();
        Map<Location, Boolean> isVisited = new LinkedHashMap<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            int currSize = queue.size();
            for (int q = 0; q < currSize; q++) {
                Location currLocation = queue.poll();
                if (processNeighbors(currLocation, other, queue, isVisited, map, rows, cols)) {
                    return currLevel;
                }
            }
            currLevel++;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}