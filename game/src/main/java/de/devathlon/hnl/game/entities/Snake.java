package de.devathlon.hnl.game.entities;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.util.ArrayList;
import java.util.List;

public class Snake implements SnakeModel {

    private Point headPoint;
    private ArrayList<Point> bodyPoints;

    private long speed;
    private boolean invincible;

    private int defaultLength;

    public Snake(Game game) {
        this.speed = 100;
        this.invincible = false;
        this.defaultLength = 2;

        bodyPoints = new ArrayList<>();
        // generate snake at 10;10 with 4 body points
        // headPoint = game.getMapModel().getConfiguration().getSpawnPoint();
        headPoint = Point.of(10, 2);

        for (int i = 1; i <= defaultLength; i++) {
            bodyPoints.add(Point.of(headPoint.getX() + i, headPoint.getY()));
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
            if (headPoint.getX() == borderPoint.getX() && headPoint.getY() == borderPoint.getY()) {
                System.out.println("collision border XD");
                return true;
            }
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

    public void updateScore(GameEngine gameEngine) {
        // update score
        gameEngine.update(EngineUpdate.SCORE_UPDATE, Messages.SCORE_UPDATE, getBodyPoints().size() - this.defaultLength);
    }

    public int getDefaultLength() {
        return defaultLength;
    }
}
