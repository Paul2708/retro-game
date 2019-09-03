package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.util.Collection;
import java.util.List;

public interface MapModel {

    List<Point> getBorder();

    SnakeModel getSnake();

    Collection<FoodModel> getFood();

    EffectBarModel getEffectBar();
}
