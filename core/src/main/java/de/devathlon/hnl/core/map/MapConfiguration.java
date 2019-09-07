package de.devathlon.hnl.core.map;

import de.devathlon.hnl.core.math.Point;

import java.awt.Dimension;

/**
 * This class holds data about a map and its configuration.
 *
 * @author Paul2708
 */
public class MapConfiguration {

    private final String name;
    private final Dimension size;
    private final Point spawnPoint;

    /**
     * Create a new map configuration.
     *
     * @param name       name of the map
     * @param size       size in blocks
     * @param spawnPoint spawn point (relative coordinates)
     */
    public MapConfiguration(String name, Dimension size, Point spawnPoint) {
        this.name = name;
        this.size = size;
        this.spawnPoint = spawnPoint;
    }

    /**
     * Get the name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the map size in blocks.
     *
     * @return size
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Get the snakes spawn point for the map.
     *
     * @return spawn point
     */
    public Point getSpawnPoint() {
        return spawnPoint;
    }
}