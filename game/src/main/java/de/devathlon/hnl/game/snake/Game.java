package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.util.Direction;

import java.awt.event.KeyEvent;

public class Game implements InputListener {

    private Snake snake;

    private Direction currentDirection;
    private Thread updateThread;

    public Game() {
        setup();
        updateSnakePosition();
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake();
    }

    private void updateSnakePosition() {
        updateThread = new Thread(() -> {
            int x;
            int y;
            switch (currentDirection) {
                case UP:
                    snake.getHeadPoint().update(0, 1);
                    x = snake.getHeadPoint().getX();
                    y = snake.getHeadPoint().getY();

                    for (Point point : snake.getBodyPoints()) {
                        int tempX = point.getX();
                        int tempY = point.getY();
                        point.update(x, y);
                        x = tempX;
                        y = tempY;
                    }
                    break;
                case DOWN:
                    snake.getHeadPoint().update(0, -1);
                    x = snake.getHeadPoint().getX();
                    y = snake.getHeadPoint().getY();

                    for (Point point : snake.getBodyPoints()) {
                        int tempX = point.getX();
                        int tempY = point.getY();
                        point.update(x, y);
                        x = tempX;
                        y = tempY;
                    }
                    break;
                case LEFT:
                    snake.getHeadPoint().update(-1, 0);
                    x = snake.getHeadPoint().getX();
                    y = snake.getHeadPoint().getY();

                    for (Point point : snake.getBodyPoints()) {
                        int tempX = point.getX();
                        int tempY = point.getY();
                        point.update(x, y);
                        x = tempX;
                        y = tempY;
                    }
                    break;
                case RIGHT:
                    snake.getHeadPoint().update(1, 0);
                    x = snake.getHeadPoint().getX();
                    y = snake.getHeadPoint().getY();

                    for (Point point : snake.getBodyPoints()) {
                        int tempX = point.getX();
                        int tempY = point.getY();
                        point.update(x, y);
                        x = tempX;
                        y = tempY;
                    }
                    break;
            }

            // after updating -> check for collision
            if (collisionWithSnakeBody() || collisionWithBorder()) {
                endGame();
            }

            // sleep
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean collisionWithSnakeBody() {
        for (Point point : snake.getBodyPoints()) {
            if (point.getX() == snake.getHeadPoint().getX() && point.getY() == snake.getHeadPoint().getY()) return true;
        }
        return false;
    }

    private boolean collisionWithBorder() {
        return false;
    }

    private void pauseGame() {

    }

    private void endGame() {
        updateThread.interrupt();
    }

    @Override
    public void onInput(int keyCode) {
        if (keyCode != KeyEvent.VK_SPACE && keyCode != KeyEvent.VK_ESCAPE) {
            Direction direction = Direction.getDirectionByKey(keyCode);
            if (direction != null) {
                currentDirection = direction;
            }
        } else {
            pauseGame();
        }
    }
}