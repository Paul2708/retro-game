package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

import java.util.Collection;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class EasyMap extends CustomMap {

    public EasyMap(Game game) {
        super(game);
    }

    @Override
    public void updateFood() {

    }

    /**
     * Get the name of the map.
     *
     * @return map name
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Get a collection of all border points.
     *
     * @return collection of points
     */
    @Override
    public Collection<Point> getBorder() {
        return null;
    }
}
