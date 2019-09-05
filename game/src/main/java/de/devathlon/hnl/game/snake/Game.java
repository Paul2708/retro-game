package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.map.MapConfiguration;
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
import de.devathlon.hnl.game.food.foodtypes.*;
import de.devathlon.hnl.game.map.*;
import de.devathlon.hnl.game.pause.ContinueGameItem;
import de.devathlon.hnl.game.pause.EndGameItem;
import de.devathlon.hnl.game.pause.MapPauseItem;
import de.devathlon.hnl.game.util.Direction;
import de.devathlon.hnl.game.util.Messages;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game implements InputListener {

    private Snake snake;

    private Direction currentDirection;

    private boolean running;
    private AtomicBoolean pause;

    private GameEngine gameEngine;
    private EngineConfiguration engineConfiguration;

    // effect variables
    private long effectGiven;
    private int effectTime;
    private boolean doublePoints;
    private long pauseEffectTimePassed;

    // effectbar
    private List<Point> effectBar;
    private Color effectBarColor;

    private CustomMap mapModel;

    private boolean inputBlocked;

    private Set<Thread> animatedBorders;

    public Game() {
        // initialize effect variables
        this.effectBar = new CopyOnWriteArrayList<>();
        this.effectGiven = 0;
        this.effectTime = 10;
        this.doublePoints = false;
        this.pauseEffectTimePassed = 0;
        this.animatedBorders = new HashSet<>();

        this.inputBlocked = false;
        this.running = true;
        this.pause = new AtomicBoolean(true);

        gameEngine = GameEngine.create();

        this.mapModel = new EmptyMap(this);
        gameEngine.setModel(mapModel);
        gameEngine.setMaps(Arrays.asList(new EmptyMap(this), new EasyMap(this), new NormalMap(this), new DifficultMap(this)));
        gameEngine.setPauseItems(new ContinueGameItem(this), new MapPauseItem(this), new EndGameItem(this));
        gameEngine.setInputListener(this);
        this.engineConfiguration = new EngineConfiguration(35, 35, 120);
        gameEngine.setUp(engineConfiguration);
        gameEngine.start();

        mapModel.setup();
        setup();
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake(this);
        this.currentDirection = Direction.LEFT;
        updateHeadPosition();

        // deactivate death screen
        gameEngine.update(EngineUpdate.DEATH_SCREEN, false);
        // update score
        gameEngine.update(EngineUpdate.SCORE_UPDATE, Messages.SCORE_UPDATE, 0);
    }

    private void generateNewFood() {
        mapModel.updateFood();
    }

    private void updateHeadPosition() {
        new Thread(() -> {
            while (true) {
                if (!pause.get() && currentDirection != null) {
                    this.inputBlocked = false;
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
                    if (snake.collisionWithBody() || snake.collisionWithBorder(mapModel.getBorderPoints())) {
                        if (snake.collisionWithBorder(mapModel.getBorderPoints()) && snake.isInvincible()) {
                            if (headPoint.getX() == 0) {
                                headPoint.setX(engineConfiguration.getWidthInBlocks() - 2);
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
                    if (snake.contactWithFood(mapModel.getFood()) != null) {
                        FoodModel food = snake.contactWithFood(mapModel.getFood());
                        mapModel.getFood().remove(food);
                        if (food instanceof SpecialFood) {
                            SpecialFood specialFood = (SpecialFood) food;
                            specialFood.apply();
                        } else {
                            if (doublePoints)
                                snake.getBodyPoints().add(Point.of(oldX, oldY));

                            snake.getBodyPoints().add(Point.of(oldX, oldY));
                        }
                        generateNewFood();
                    }

                    snake.updateScore(gameEngine);
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

        gameEngine.update(EngineUpdate.EFFECT_UPDATE, "Effekt:", "-/-");
    }

    public void pauseGame() {
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

        if (pause.get()) {
            gameEngine.pause();
        } else {
            gameEngine.unpause();
        }
    }

    private void endGame() {
        running = false;
        pause = new AtomicBoolean(true);

        // activate death screen
        gameEngine.update(EngineUpdate.DEATH_SCREEN, true);
    }

    public void reset() {
        this.snake = new Snake(this);

        mapModel.getFood().clear();
        generateNewFood();

        this.running = true;

        removeAllEffects();
        this.effectGiven = 0;
        this.pauseEffectTimePassed = 0;

        this.currentDirection = Direction.LEFT;

        // update score
        gameEngine.update(EngineUpdate.SCORE_UPDATE, Messages.SCORE_UPDATE, 0);
        // deactivate death screen
        gameEngine.update(EngineUpdate.DEATH_SCREEN, false);
    }

    @Override
    public void onInput(int keyCode) {
        if (keyCode != KeyEvent.VK_SPACE && keyCode != KeyEvent.VK_ESCAPE) {
            if (inputBlocked) return;
            Direction direction = Direction.getDirectionByKey(keyCode);
            if (direction != null) {
                this.inputBlocked = true;
                if (currentDirection == Direction.UP && direction == Direction.DOWN) return;
                if (currentDirection == Direction.DOWN && direction == Direction.UP) return;
                if (currentDirection == Direction.LEFT && direction == Direction.RIGHT) return;
                if (currentDirection == Direction.RIGHT && direction == Direction.LEFT) return;
                this.currentDirection = direction;
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

    public AtomicBoolean getPause() {
        return pause;
    }

    public int getEffectTime() {
        return effectTime;
    }

    public long getEffectGiven() {
        return effectGiven;
    }

    public void setEffectBarColor(Color effectBarColor) {
        this.effectBarColor = effectBarColor;
    }

    public List<Point> getEffectBar() {
        return effectBar;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public Snake getSnake() {
        return snake;
    }

    public Color getEffectBarColor() {
        return effectBarColor;
    }

    public CustomMap getMapModel() {
        return mapModel;
    }

    public void setMapModel(CustomMap mapModel) {
        this.mapModel = mapModel;
    }

    public Set<Thread> getAnimatedBorders() {
        return animatedBorders;
    }
}