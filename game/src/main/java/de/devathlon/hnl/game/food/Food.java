package de.devathlon.hnl.game.food;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.math.Point;

import java.awt.*;

/**
 * This class is used to create new basic food models.
 *
 * @author Leon
 */
public class Food implements FoodModel {

    private Point location;
    private Color color;

    /**
     * @param x location
     * @param y location
     * @param color new {@link Color} object to define the color
     */
    public Food(int x, int y, Color color) {
        location = Point.of(x, y);
        this.color = color;
    }

    /**
     * @return {@link Point} location of the object on the map
     */
    @Override
    public Point getLocation() {
        return location;
    }

    /**
     * @return @{@link Color} current color
     */
    @Override
    public Color getColor() {
        return color;
    }
}
