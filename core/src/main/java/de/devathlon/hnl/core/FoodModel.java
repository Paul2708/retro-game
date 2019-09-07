package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.awt.Color;

/**
 * This class models an appearing food item, that can be collected.
 *
 * @author Paul2708
 */
public interface FoodModel {

    /**
     * Get the food color that will be drawn.
     *
     * @return color
     */
    Color getColor();

    /**
     * Get the point location of the food item.
     *
     * @return location as point
     */
    Point getLocation();
}
