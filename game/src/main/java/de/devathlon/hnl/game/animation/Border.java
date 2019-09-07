package de.devathlon.hnl.game.animation;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * This class is used to animate things related to border points.
 *
 * @author Leon
 */
public class Border {

    /**
     * Moves a border periodically based on the given parameters.
     * Each border is one block wide.
     * The border will move infinitely between yStart and yEnd.
     *
     * @param delay how long it takes to first see the border
     * @param time how long it takes to move one block forwards
     * @param game current game object
     * @param yStart y start point
     * @param yEnd y end point
     */
    public static void animateMovingBorder(int delay, int time, Game game, int yStart, int yEnd) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignored) {
            }
            int currentX = 2;
            boolean moveForward = true;
            while (true) {
                if (!game.getPause().get()) {
                    List<Point> movingBorder = new CopyOnWriteArrayList<>();

                    if (currentX >= game.getEngineConfiguration().getWidthInBlocks() - 3) {
                        moveForward = false;
                        for (int y = yStart; y < yEnd; y++) {
                            game.getMapModel().getBorderPoints().remove(Point.of((currentX - 1), y));
                        }
                    }
                    if (currentX == 2 && !moveForward) {
                        moveForward = true;
                        for (int y = yStart; y < yEnd; y++) {
                            game.getMapModel().getBorderPoints().remove(Point.of((currentX + 1), y));
                        }
                    }

                    for (int y = yStart; y < yEnd; y++) {
                        if (!moveForward)
                            game.getMapModel().getBorderPoints().remove(Point.of((currentX + 1), y));
                        else
                            game.getMapModel().getBorderPoints().remove(Point.of((currentX - 1), y));
                        movingBorder.add(Point.of(currentX, y));
                    }
                    game.getMapModel().getBorderPoints().addAll(movingBorder);
                    if (!moveForward) {
                        currentX--;
                    }
                    if (moveForward) currentX++;
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ignored) {

                }
            }
        });

        thread.start();

        game.getAnimatedBorders().add(thread);
    }
}
