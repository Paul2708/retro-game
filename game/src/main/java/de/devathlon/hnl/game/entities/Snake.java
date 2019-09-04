package de.devathlon.hnl.game.entities;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;

import java.util.ArrayList;
import java.util.List;

public class Snake implements SnakeModel {

    private Point headPoint;
    private ArrayList<Point> bodyPoints;

    private long speed;
    private boolean invincible;

    public Snake() {
        this.speed = 100;
        this.invincible = false;

        bodyPoints = new ArrayList<>();
        // generate snake at 10;10 with 4 body points
        headPoint = Point.of(10, 10);

        for (int i = 1; i <= 4; i++) {
            bodyPoints.add(Point.of(10 + i, 10));
        }
    }

    @Override
    public Point getHeadPoint() {
        return headPoint;
    }

    @Override
    public ArrayList<Point> getBodyPoints() {
        return bodyPoints;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public long getSpeed() {
        return speed;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean collisionWithBody() {
        if (isInvincible()) return false;
        for (Point point : bodyPoints) {
            if (point.getX() == headPoint.getX() && point.getY() == headPoint.getY()) return true;
        }
        return false;
    }

    public boolean collisionWithBorder(List<Point> borderPoints) {
        for (Point borderPoint : borderPoints) {
            if (headPoint.getX() == borderPoint.getX() && headPoint.getY() == borderPoint.getY())
                return true;
        }
        return false;
    }

    public FoodModel contactWithFood(List<FoodModel> foodList) {
        for (FoodModel food : foodList) {
            if (food.getLocation().getX() == headPoint.getX() && food.getLocation().getY() == headPoint.getY())
                return food;
        }

        return null;
    }

    public void updateBody(int oldHeadX, int oldHeadY) {
        int x = oldHeadX;
        int y = oldHeadY;
        for (Point point : bodyPoints) {
            int tempX = point.getX();
            int tempY = point.getY();
            point.set(x, y);
            x = tempX;
            y = tempY;
        }


    }
}
