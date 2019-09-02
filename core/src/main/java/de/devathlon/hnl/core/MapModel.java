package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.util.Collection;

public interface MapModel {

    Collection<Point> getBorder();

    SnakeModel getSnake();

    Collection<FoodModel> getFood();
}
