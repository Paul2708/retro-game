package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.animation.Border;
import de.devathlon.hnl.game.animation.Effect;
import de.devathlon.hnl.game.food.Food;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
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
    private List<Point> borderPoints = new CopyOnWriteArrayList<>();

    private List<FoodModel> foodList;

    // effect variables
    private long effectGiven;
    private int effectTime;
    private boolean doublePoints;
    private long pauseEffectTimePassed;

    // effectbar
    private List<Point> effectBar = new CopyOnWriteArrayList<>();
    private Color effectBarColor;
    private boolean effectBarActive;


    public Game() {
        // initialize effect variables
        this.effectGiven = 0;
        this.effectTime = 10;
        this.doublePoints = false;
        this.pauseEffectTimePassed = 0;

        // map end

        this.running = true;
        this.pause = new AtomicBoolean(true);
        setup();

        // Refactor
        gameEngine = GameEngine.create();
        MapModel mapModel = new MapModel() {
            @Override
            public Collection<Point> getBorder() {
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

            // TODO: Implement me
            @Override
            public EffectBarModel getEffectBar() {
                return new EffectBarModel() {
                    @Override
                    public boolean isActive() {
                        return effectBarActive;
                    }

                    @Override
                    public Color getColor() {
                        return effectBarColor;
                    }

                    @Override
                    public Collection<Point> getBar() {
                        return effectBar;
                    }
                };
            }
        };
        gameEngine.setModel(mapModel);
        gameEngine.setInputListener(this);
        this.engineConfiguration = new EngineConfiguration(35, 35, 120);
        gameEngine.setUp(engineConfiguration);
        gameEngine.start();
        // setup border
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < engineConfiguration.getWidthInBlocks(); x++) {
                this.borderPoints.add(Point.of(x, 0));
                this.borderPoints.add(Point.of(x, engineConfiguration.getHeightInBlocks() - 4));
            }
            for (int y = 0; y < engineConfiguration.getHeightInBlocks() - 4; y++) {
                this.borderPoints.add(Point.of(0, y));
                this.borderPoints.add(Point.of(engineConfiguration.getWidthInBlocks() - 1, y));
            }
        }

        // map
        for (int x = 5; x < 20; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 15; x < 20; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 25; x < 30; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }

        Border.animateMovingBorder(0, 1000, this, 25, 30);
        Border.animateMovingBorder(5500, 1500, this, 25, 30);

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
                if (!pause.get()) {
                    if (effectGiven != 0) {
                        long secondsLeft = ((System.currentTimeMillis() - effectGiven) / 1000);
                        Effect.animateTimer(Game.this, (int) secondsLeft);
                        if (((System.currentTimeMillis() - effectGiven) / 1000) >= effectTime) {
                            removeAllEffects();
                        }
                    }

                    for (FoodModel food : foodList) {
                        if (food instanceof SpecialFood) {
                            SpecialFood specialFood = (SpecialFood) food;
                            if (specialFood.getTimeAlive() == 0) {
                                foodList.remove(food);
                            } else {
                                specialFood.setTimeAlive(specialFood.getTimeAlive() - 1);
                            }
                        }
                    }
                }
            }
        }, 1000, 1000);
    }

    private void generateNewFood() {
        Random random = new Random();

        int x = random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1;
        int y = random.nextInt(engineConfiguration.getHeightInBlocks() - 5) + 1;

        int specialX = random.nextInt(engineConfiguration.getWidthInBlocks() - 2) + 1;
        int specialY = random.nextInt(engineConfiguration.getHeightInBlocks() - 5) + 1;
        Food food;
        SpecialFood special = null;
        // special food

        switch (random.nextInt(6)) {
            case 1:
                special = new SpecialFood(specialX, specialY, Color.GREEN); // green (speed)
                break;
            case 2:
                special = new SpecialFood(specialX, specialY, Color.BLUE); // blue (slowness)
                break;
            case 3:
                special = new SpecialFood(specialX, specialY, Color.RED); // blue (slowness)
                break;
            case 4:
                special = new SpecialFood(specialX, specialY, Color.GRAY); // gray (half snake disappears)
                break;
            case 5:
                special = new SpecialFood(specialX, specialY, Color.MAGENTA); // magenta (double points)
                break;
        }
        for (Point borderPoint : borderPoints) {
            if (x == borderPoint.getX() && y == borderPoint.getY()) {
                generateNewFood();
                return;
            }
            if (special != null) {
                if (special.getLocation().getX() == borderPoint.getX() && special.getLocation().getY() == borderPoint.getY()) {
                    special = null;
                }
            }
        }
        food = new Food(x, y, Color.ORANGE);
        foodList.add(food);
        if (special != null) {
            foodList.add(special);
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
                    if (snake.collisionWithBody() || snake.collisionWithBorder(borderPoints)) {
                        if (snake.collisionWithBorder(borderPoints) && snake.isInvincible()) {
                            if (headPoint.getX() == 0) {
                                headPoint.setX(engineConfiguration.getWidthInBlocks() - 1);
                            } else if (headPoint.getX() == engineConfiguration.getWidthInBlocks() - 1) {
                                headPoint.setX(1);
                            } else if (headPoint.getY() == 0) {
                                headPoint.setY(engineConfiguration.getHeightInBlocks() - 4);
                            } else if (headPoint.getY() == engineConfiguration.getHeightInBlocks() - 4) {
                                headPoint.setY(1);
                            }
                        } else {
                            endGame();
                        }
                    }
                    // updated head, now update body
                    snake.updateBody(oldX, oldY);
                    // check if
                    if (snake.contactWithFood(foodList) != null) {
                        FoodModel food = snake.contactWithFood(foodList);
                        foodList.remove(food);
                        if (food instanceof SpecialFood) {
                            SpecialFood specialFood = (SpecialFood) food;
                            specialFood.activateEffect(this, snake);
                        } else {
                            if (doublePoints)
                                snake.getBodyPoints().add(Point.of(oldX, oldY));

                            snake.getBodyPoints().add(Point.of(oldX, oldY));
                        }
                        generateNewFood();
                    }

                    // update score
                    gameEngine.update(EngineUpdate.SCORE_UPDATE, "Dein neuer Score", snake.getBodyPoints().size() + 1);
                }
                // sleep
                try {
                    Thread.sleep(snake.getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void removeAllEffects() {
        this.snake.setSpeed(100);
        this.snake.setInvincible(false);

        this.effectGiven = 0;
        this.doublePoints = false;

        effectBar.clear();
    }

    private void pauseGame() {
        if (!pause.get()) {
            if (effectGiven != 0) {
                pauseEffectTimePassed = ((System.currentTimeMillis() - effectGiven) / 1000);
                this.effectGiven = 0;
            }
        } else {
            if (pauseEffectTimePassed != 0) {
                effectGiven = System.currentTimeMillis() - (pauseEffectTimePassed * 1000);
                pauseEffectTimePassed = 0;
            }
        }
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
        this.pauseEffectTimePassed = 0;

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

    public void setEffectGiven(long effectGiven) {
        this.effectGiven = effectGiven;
    }

    public void setDoublePoints(boolean doublePoints) {
        this.doublePoints = doublePoints;
    }

    public EngineConfiguration getEngineConfiguration() {
        return engineConfiguration;
    }

    public List<Point> getBorderPoints() {
        return borderPoints;
    }

    public AtomicBoolean getPause() {
        return pause;
    }

    public int getEffectTime() {
        return effectTime;
    }


    public void setEffectBarActive(boolean effectBarActive) {
        this.effectBarActive = effectBarActive;
    }

    public void setEffectBarColor(Color effectBarColor) {
        this.effectBarColor = effectBarColor;
    }

    public List<Point> getEffectBar() {
        return effectBar;
    }
}