package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.util.Collection;

/**
 * This class models the snake entity.
 *
 * @author Paul2708
 */
public interface SnakeModel {

    /**
     * Get the snakes head position.
     *
     * @return head point
     */
    Point getHeadPoint();

    /**
     * Get the collection of snake body points.
     *
     * @return collection of points
     */
    Collection<Point> getBodyPoints();
}
