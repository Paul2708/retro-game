package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.configuration.DefaultEngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.util.Direction;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;

public class Game implements InputListener {

    private Snake snake;

    private Direction currentDirection;
    private Thread updateThread;

    public Game() {
        setup();
        updateHeadPosition();

        // Refactor
        GameEngine gameEngine = GameEngine.create();
        gameEngine.setUp(new DefaultEngineConfiguration(), new MapModel() {

            @Override
            public Collection<Point> getBorder() {
                return new HashSet<>();
            }

            @Override
            public SnakeModel getSnake() {
                return snake;
            }

            @Override
            public Collection<FoodModel> getFood() {
                return new HashSet<>();
            }
        });
        gameEngine.start();
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake();
        this.currentDirection = Direction.LEFT;
    }

    private void updateHeadPosition() {
        updateThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                switch (currentDirection) {
                    case UP:
                        snake.getHeadPoint().update(0, 1);
                        break;
                    case DOWN:
                        snake.getHeadPoint().update(0, -1);
                        break;
                    case LEFT:
                        snake.getHeadPoint().update(-1, 0);
                        break;
                    case RIGHT:
                        snake.getHeadPoint().update(1, 0);
                        break;
                }
                // updated head, now update body
                updateBody();
                // after updating -> check for collision
                if (collisionWithSnakeBody() || collisionWithBorder()) {
                    System.out.println("collision!");
                    endGame();
                }
                // sleep
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start();
    }

    private void updateBody() {
        int x = snake.getHeadPoint().getX();
        int y = snake.getHeadPoint().getY();
        for (int i = 0; i < snake.getBodyPoints().size(); i++) {
            System.out.println("Move to " + x);
            Point point = snake.getBodyPoints().get(i);
            int tempX = point.getX();
            int tempY = point.getY();
            point.set(x, y);
            x = tempX;
            y = tempY;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        if (updateThread.isAlive()) {
            updateThread.interrupt();
        } else {
            updateThread.start();
        }
    }

    private void endGame() {
        updateThread.interrupt();
    }

    @Override
    public void onInput(int keyCode) {
        System.out.println("INPUT");
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