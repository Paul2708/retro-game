package de.devathlon.hnl.game.entities;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.game.Launcher;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a new snake.
 *
 * @author Leon
 */
public class Snake implements SnakeModel {

    private Point headPoint;
    private ArrayList<Point> bodyPoints;

    private long speed;
    private boolean invincible;

    private int defaultLength;

    private int winSize;

    /**
     * Constructor to set the default values and in order to generate the {@link #headPoint}
     * and to fill the {@link #bodyPoints} list with the {@link #defaultLength}
     *
     * @param game current game object
     */
    public Snake(Game game) {
        this.speed = 100;
        this.invincible = false;
        this.defaultLength = 2;
        this.winSize = 0;

        bodyPoints = new ArrayList<>();
        // generate snake at 10;10 with 4 body points
        headPoint = game.getMapModel().getConfiguration().getSpawnPoint().clone();

        for (int i = 1; i <= defaultLength; i++) {
            bodyPoints.add(Point.of(headPoint.getX() + i, headPoint.getY()));
        }

        // calculate snake's win size
        for (int x = 0; x < game.getEngineConfiguration().getWidthInBlocks(); x++) {
            for (int y = 0; y < game.getEngineConfiguration().getHeightInBlocks() - 3; y++) {
                if (!game.getMapModel().getBorderPoints().contains(Point.of(x, y))) {
                    winSize++;
                }
            }
        }
    }

    /**
     * @return the head {@link Point}
     */
    @Override
    public Point getHeadPoint() {
        return headPoint;
    }

    /**
     * @return an {@link ArrayList} with {@link Point}s to show the snake body
     */
    @Override
    public ArrayList<Point> getBodyPoints() {
        return bodyPoints;
    }

    /**
     * Sets the speed.
     * Lower values result in a faster gameplay.
     *
     * @param speed integer
     */
    public void setSpeed(long speed) {
        this.speed = speed;
    }

    /**
     * @return speed integer
     */
    public long getSpeed() {
        return speed;
    }

    /**
     * Sets whether the snake is invincible
     *
     * @param invincible boolean
     */
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    /**
     * @return if the snake is invincible
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * @return boolean if the head collisions with a body point
     */
    public boolean collisionWithBody() {
        if (isInvincible()) return false;
        for (Point point : bodyPoints) {
            if (point.getX() == headPoint.getX() && point.getY() == headPoint.getY()) return true;
        }
        return false;
    }

    /**
     * @param borderPoints list of all border points
     * @return boolean if the head collisions with a border point
     */
    public boolean collisionWithBorder(List<Point> borderPoints) {
        for (Point borderPoint : borderPoints) {
            if (headPoint.getX() == borderPoint.getX() && headPoint.getY() == borderPoint.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a food object if the snake's {@link #headPoint} has
     * the same coordinates as the food model.
     *
     * @param foodList list of all food models
     * @return FoodModel if the head contacts a food object
     */
    public FoodModel contactWithFood(List<FoodModel> foodList) {
        for (FoodModel food : foodList) {
            if (food.getLocation().getX() == headPoint.getX() && food.getLocation().getY() == headPoint.getY())
                return food;
        }

        return null;
    }

    /**
     * Moves all body points to match their locations with their predecessor.
     *
     * @param oldHeadX location x
     * @param oldHeadY location y
     */
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

    /**
     * Updates score based on the body length.
     *
     * @param gameEngine game engine
     */
    public void updateScore(GameEngine gameEngine) {
        // update score
        if ((getBodyPoints().size() + 1) >= winSize) {
            gameEngine.update(EngineUpdate.EFFECT_UPDATE, "Du hast das", "Spiel besiegt!! :o");
            gameEngine.update(EngineUpdate.SCORE_UPDATE, Messages.SCORE_UPDATE, 999999999);
            Launcher.getGame().endGame(true);
        } else {
            gameEngine.update(EngineUpdate.SCORE_UPDATE, Messages.SCORE_UPDATE, calculateScore());
        }
    }


    /**
     * Calculates score based on all body points - default length
     *
     * @return current score
     */
    public int calculateScore() {
        return getBodyPoints().size() - this.defaultLength;
    }

    /**
     * @return default body length as an int
     */
    public int getDefaultLength() {
        return defaultLength;
    }
}
