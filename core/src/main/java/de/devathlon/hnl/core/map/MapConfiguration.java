package de.devathlon.hnl.core.map;

import de.devathlon.hnl.core.math.Point;

import java.awt.*;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class MapConfiguration {

    private final String name;
    private final Dimension size;
    private final Point spawnPoint;

    public MapConfiguration(String name, Dimension size, Point spawnPoint) {
        this.name = name;
        this.size = size;
        this.spawnPoint = spawnPoint;
    }

    public String getName() {
        return name;
    }

    public Dimension getSize() {
        return size;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }
}
