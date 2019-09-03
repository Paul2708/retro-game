package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.animation.Effect;
import de.devathlon.hnl.game.entities.Food;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.util.Direction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

    // effect variables
    private long effectGiven;
    private int effectTime;
    private long speed;
    private boolean invincible;
    private boolean doublePoints;

    public Game() {
        // initialize effect variables
        this.speed = 100;
        this.effectGiven = 0;
        this.effectTime = 10;
        this.doublePoints = false;
        this.invincible = false;

        this.running = true;
        this.pause = new AtomicBoolean(true);
        setup();

        // Refactor
        this.gameEngine = GameEngine.create();
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
        this.gameEngine.setModel(mapModel);
        this.gameEngine.setInputListener(this);
        this.engineConfiguration = new EngineConfiguration(35, 35, 120);
        this.gameEngine.setUp(engineConfiguration);
        this.gameEngine.start();
        // setup border
        this.borderPoints.add(Point.of(0, 0));
        this.borderPoints.add(Point.of(0, engineConfiguration.getHeightInBlocks()));
        this.borderPoints.add(Point.of(engineConfiguration.getWidthInBlocks(), 0));
        this.borderPoints.add(Point.of(engineConfiguration.getWidthInBlocks(), engineConfiguration.getHeightInBlocks()));

        // map design
        this.borderPoints.add(Point.of(10, 15));
        this.borderPoints.add(Point.of(10, 20));
        this.borderPoints.add(Point.of(20, 20));
        this.borderPoints.add(Point.of(20, 15));

        // generate new food model
        generateNewFood();
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake();
        this.currentDirection = Direction.LEFT;
        updateHeadPosition();
        // initialize food
        this.foodList = new CopyOnWriteArrayList<>();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (effectGiven != 0) {
                    Effect.animateTimer(2);
                    if (((System.currentTimeMillis() - effectGiven) / 1000) >= effectTime) {
                        removeAllEffects();
                    }
                }
            }
        }, 1000, 1000);
    }

    private void generateNewFood() {
        Random random = new Random();

        int x = random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1;
        int y = random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1;

        Food food;
        Food special = null;
        // special food
        if (random.nextInt(4) == 1) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.GREEN); // green (speed)
        } else if (random.nextInt(4) == 2) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.BLUE); // blue (slowness)
        } else if (random.nextInt(4) == 0) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.RED); // blue (slowness)
        } else if (random.nextInt(4) == 3) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.GRAY); // gray (half snake disappears)
        } else if (random.nextInt(4) == 0) {
            special = new Food(
                    random.nextInt(engineConfiguration.getHeightInBlocks() - 2) + 1,
                    random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1,
                    Color.YELLOW); // yellow (double points)
        }
        for (int i = 0; i < borderPoints.size(); i++) {
            if (x == borderPoints.get(i).getX() && y == borderPoints.get(i).getY()) {
                generateNewFood();
                return;
            }
            if (special != null) {
                if (special.getLocation().getX() == borderPoints.get(i).getX() && special.getLocation().getY() == borderPoints.get(i).getY()) {
                    return;
                }
            }
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
            }, 1000 * 20);
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
                        if (collisionWithBorder() && invincible) {
                            if (headPoint.getX() == 0) {
                                headPoint.setX(engineConfiguration.getWidthInBlocks() - 1);
                            } else if (headPoint.getX() == engineConfiguration.getWidthInBlocks()) {
                                headPoint.setX(1);
                            } else if (headPoint.getY() == 0) {
                                headPoint.setY(engineConfiguration.getHeightInBlocks() - 1);
                            } else if (headPoint.getY() == engineConfiguration.getHeightInBlocks()) {
                                headPoint.setY(1);
                            }
                        } else {
                            endGame();
                        }
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
                            if (doublePoints)
                                snake.getBodyPoints().add(Point.of(oldX, oldY));

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
        removeAllEffects();
        if (foodModel.getColor() == Color.GREEN) {
            this.speed = 50;
        } else if (foodModel.getColor() == Color.BLUE) {
            this.speed = 300;
        } else if (foodModel.getColor() == Color.RED) {
            this.invincible = true;
        } else if (foodModel.getColor() == Color.GRAY) {
            for (int i = 0; i < snake.getBodyPoints().size() / 2; i++) {
                snake.getBodyPoints().remove(i);
            }
            return; // return if no timer is needed
        } else if (foodModel.getColor() == Color.YELLOW) {
            this.doublePoints = true;
        }
        this.effectGiven = System.currentTimeMillis();
    }

    private void removeAllEffects() {
        this.speed = 100;
        this.invincible = false;
        this.effectGiven = 0;
        this.doublePoints = false;
    }

    private boolean collisionWithSnakeBody() {
        if (invincible) return false;
        for (Point point : snake.getBodyPoints()) {
            if (point.getX() == snake.getHeadPoint().getX() && point.getY() == snake.getHeadPoint().getY()) return true;
        }
        return false;
    }

    private boolean collisionWithBorder() {
        for (int i = 0; i < borderPoints.size(); i++) {
            if (snake.getHeadPoint().getX() == borderPoints.get(i).getX() && snake.getHeadPoint().getY() == borderPoints.get(i).getY())
                return true;
        }
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

        this.foodList.clear();
        generateNewFood();

        this.running = true;

        removeAllEffects();
        this.effectGiven = 0;

        this.currentDirection = Direction.LEFT;
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