package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.util.Collection;

/**
 * This class models the whole map and all its entities.
 *
 * @author Paul2708
 */
public interface MapModel {

    /**
     * Get the name of the map.
     *
     * @return map name
     */
    String getName();

    /**
     * Get a collection of all border points.
     *
     * @return collection of points
     */
    Collection<Point> getBorder();

    /**
     * Get the snake entity model.
     *
     * @return snake model
     */
    SnakeModel getSnake();

    /**
     * Get a collection of all current food entity models.
     *
     * @return food model
     */
    Collection<FoodModel> getFood();

    /**
     * Get the effect bar model.
     *
     * @return effect bar model
     */
    EffectBarModel getEffectBar();
}
