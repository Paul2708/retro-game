package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.awt.Color;
import java.util.Collection;

/**
 * This class models the effect bar.
 * The effect bar indicates how long and which effect is active.
 *
 * @author Paul2708
 */
public interface EffectBarModel {

    /**
     * Check if the bar is active and should be drawn to the game.
     *
     * @return true if the bar is active, otherwise false
     */
    boolean isActive();

    /**
     * Get the bar color.
     *
     * @return color
     */
    Color getColor();

    /**
     * Get a collection of all points that will be used to display the effect bar.
     *
     * @return collection of points
     */
    Collection<Point> getBar();
}