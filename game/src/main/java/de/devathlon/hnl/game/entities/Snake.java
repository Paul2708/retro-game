package de.devathlon.hnl.game.entities;

import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;

import java.util.Collection;
import java.util.HashSet;

public class Snake implements SnakeModel {

    private Point headPoint;
    private HashSet<Point> bodyPoints;

    public Snake() {
        bodyPoints = new HashSet<>();
        // generate snake at 10;10 with 4 body points
        headPoint = Point.of(10, 10);

        for (int i = 0; i < 4; i++) {
            bodyPoints.add(Point.of(10 + 1, 10));
        }
    }

    @Override
    public Point getHeadPoint() {
        return headPoint;
    }

    @Override
    public Collection<Point> getBodyPoints() {
        return bodyPoints;
    }
}
