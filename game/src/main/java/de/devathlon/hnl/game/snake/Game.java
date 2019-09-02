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

    private boolean running;
    private boolean pause;

    public Game() {
        running = true;
        setup();
        updateHeadPosition();

        // Refactor
        GameEngine gameEngine = GameEngine.create();
        gameEngine.setModel(new MapModel() {
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
                return null;
            }
        });
        gameEngine.setInputListener(this);
        gameEngine.setUp(new DefaultEngineConfiguration());
        gameEngine.start();

        this.pause = false;
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake();
        this.currentDirection = Direction.LEFT;
    }

    private void updateHeadPosition() {
       new Thread(() -> {
            while (running) {
                if (!pause) {
                    if (currentDirection != null) {
                        Point headPoint = snake.getHeadPoint();

                        int oldX = headPoint.getX();
                        int oldY = headPoint.getY();
                        switch (currentDirection) {
                            case UP:
                                headPoint.update(0, 1);
                                break;
                            case DOWN:
                                headPoint.update(0, -1);
                                break;
                            case LEFT:
                                headPoint.update(-1, 0);
                                break;
                            case RIGHT:
                                headPoint.update(1, 0);
                                break;
                        }
                        // after updating -> check for collision
                        if (collisionWithSnakeBody() || collisionWithBorder()) {
                            endGame();
                        }
                        // updated head, now update body
                        updateBody(oldX, oldY);
                        // sleep
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void updateBody(int oldHeadX, int oldHeadY) {
        int x = oldHeadX;
        int y = oldHeadY;
        for (int i = 0; i < snake.getBodyPoints().size(); i++) {
            Point point = snake.getBodyPoints().get(i);
            int tempX = point.getX();
            int tempY = point.getY();
            point.set(x, y);
            x = tempX;
            y = tempY;
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
        System.out.println("Pause:  " + pause);
        if (pause) {
            pause = false;
        } else {
            pause = true;
        }
    }

    private void endGame() {
        System.out.println("ending game.");
        running = false;
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