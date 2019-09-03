package de.devathlon.hnl.core;

import de.devathlon.hnl.core.math.Point;

import java.awt.Color;
import java.util.Collection;

public interface EffectBarModel {

    Color getColor();

    Collection<Point> getBar();
}
