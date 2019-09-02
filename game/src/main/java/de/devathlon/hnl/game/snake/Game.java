package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.configuration.DefaultEngineConfiguration;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.Launcher;
import de.devathlon.hnl.game.entities.Food;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game implements InputListener {

    private Snake snake;

    private Direction currentDirection;

    private boolean running;
    private AtomicBoolean pause;

    private GameEngine gameEngine;
    private EngineConfiguration engineConfiguration;
    private MapModel mapModel;
    private List<Point> borderPoints = new ArrayList<>();

    private List<FoodModel> foodList;

    private long speed;

    private Thread effectTimer;
    private boolean newEffect;

    public Game() {
        this.speed = 100;
        this.effectTimer = new Thread();
        running = true;
        pause = new AtomicBoolean(true);
        setup();
        updateHeadPosition();

        foodList = new CopyOnWriteArrayList<>();

        // Refactor
        gameEngine = GameEngine.create();
        this.mapModel = new MapModel() {
            @Override
            public List<Point> getBorder() {
                return borderPoints;
            }

            @Override
            public SnakeModel getSnake() {
                return snake;
            }

            @Override
            public Collection<FoodModel> getFood() {
                return foodList;
            }
        };
        gameEngine.setModel(mapModel);
        gameEngine.setInputListener(this);
        engineConfiguration = new EngineConfiguration(20, 20, 120);
        gameEngine.setUp(engineConfiguration);
        gameEngine.start();

        // generate food AFTER game engine is setup
        generateNewFood();
        // setup border
        borderPoints.add(Point.of(0, 0));
        borderPoints.add(Point.of(0, engineConfiguration.getHeightInBlocks()));
        borderPoints.add(Point.of(engineConfiguration.getWidthInBlocks(), 0));
        borderPoints.add(Point.of(engineConfiguration.getWidthInBlocks(), engineConfiguration.getHeightInBlocks()));
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake();
        this.currentDirection = Direction.LEFT;
    }

    private void generateNewFood() {
        Random random = new Random();

        int x = random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1;
        int y = random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1;

        Food food;
        Food special = null;
        if (random.nextInt(3) == 1) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.GREEN);
        } else if (random.nextInt(3) == 2) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.BLUE);
        }
        food = new Food(x, y, Color.ORANGE);
        foodList.add(food);
        if (special != null) {
            final Food specialFood = special;
            foodList.add(specialFood);
            // remove food after 20 seconds
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    if (foodList.contains(specialFood))
                        foodList.remove(specialFood);
                }
            },1000 * 20);
        }
    }

    private void updateHeadPosition() {
        new Thread(() -> {
            while (true) {
                if (!pause.get() && currentDirection != null) {
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
                    // check if
                    if (contactWithFood() != null) {
                        FoodModel food = contactWithFood();
                        foodList.remove(food);
                        if (food.getColor() != Color.ORANGE) {
                            proccessFoodEffect(food);
                        } else {
                            snake.getBodyPoints().add(Point.of(oldX, oldY));
                        }
                        generateNewFood();
                    }
                    // sleep
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

    private FoodModel contactWithFood() {
        for (FoodModel food : foodList) {
            if (food.getLocation().getX() == snake.getHeadPoint().getX() && food.getLocation().getY() == snake.getHeadPoint().getY())
                return food;
        }

        return null;
    }

    private void proccessFoodEffect(FoodModel foodModel) {
        if(effectTimer != null && effectTimer.isAlive()) {
            effectTimer.interrupt();
            effectTimer = null;
            newEffect = true;
        }
        if (foodModel.getColor() == Color.GREEN) {
            this.speed = 50;
        } else if (foodModel.getColor() == Color.BLUE) {
            this.speed = 300;
        }
        effectTimer = new Thread(() -> {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
            }
            if(!newEffect) this.speed = 100;
            System.out.println("Debug:  " + 100);
        });
        newEffect = false;
        effectTimer.start();
    }

    private void removeAllEffects() {
        speed = 100;
    }

    private boolean collisionWithSnakeBody() {
        for (Point point : snake.getBodyPoints()) {
            if (point.getX() == snake.getHeadPoint().getX() && point.getY() == snake.getHeadPoint().getY()) return true;
        }
        return false;
    }

    private boolean collisionWithBorder() {
        if (snake.getHeadPoint().getX() >= engineConfiguration.getWidthInBlocks()
                || snake.getHeadPoint().getX() <= 0
                || snake.getHeadPoint().getY() >= engineConfiguration.getHeightInBlocks()
                || snake.getHeadPoint().getY() <= 0) return true;
        return false;
    }

    private void pauseGame() {
        pause.set(!pause.get());
    }

    private void endGame() {
        running = false;
        pause = new AtomicBoolean(true);
    }

    private void reset() {
        this.snake = new Snake();

        foodList.clear();
        generateNewFood();

        running = true;

        removeAllEffects();
        if(effectTimer != null & effectTimer.isAlive()) {
            effectTimer.interrupt();
            effectTimer = null;
        }

        currentDirection = Direction.LEFT;
    }

    @Override
    public void onInput(int keyCode) {
        if (keyCode != KeyEvent.VK_SPACE && keyCode != KeyEvent.VK_ESCAPE) {
            Direction direction = Direction.getDirectionByKey(keyCode);
            if (direction != null) {
                if (currentDirection == Direction.UP && direction == Direction.DOWN) return;
                if (currentDirection == Direction.DOWN && direction == Direction.UP) return;
                if (currentDirection == Direction.LEFT && direction == Direction.RIGHT) return;
                if (currentDirection == Direction.RIGHT && direction == Direction.LEFT) return;
                currentDirection = direction;
            }
        } else {
            if (!running) {
                reset();
                return;
            }
            pauseGame();
        }
    }

    public EngineConfiguration getEngineConfiguration() {
        return engineConfiguration;
    }
}