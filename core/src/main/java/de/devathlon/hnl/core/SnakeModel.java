package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.util.Collection;

public interface SnakeModel {

    Point getHeadPoint();

    Collection<Point> getBodyPoints();
}
