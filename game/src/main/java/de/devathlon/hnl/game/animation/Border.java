package de.devathlon.hnl.game.animation;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Border {

    public static void animateMovingBorder(int delay, int time, Game game, int yStart, int yEnd) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
