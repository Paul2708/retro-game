package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.map.*;
import de.devathlon.hnl.game.pause.ContinueGameItem;
import de.devathlon.hnl.game.pause.EndGameItem;
import de.devathlon.hnl.game.pause.MapChangeItem;
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


/**
 * This class manages the whole game. It creates e.g. the view, moves the snake
 * and processes input and map related changes.
 *
 * @author Leon
 */
public class Game implements InputListener {

    // snake model related variables
    private Snake snake;
    private Direction currentDirection;

    // game related variables
    private boolean running;
    private AtomicBoolean pause;
    private boolean inputBlocked;

    // gameEngine
    private GameEngine gameEngine;
    private EngineConfiguration engineConfiguration;

    // mapModel
    private CustomMap mapModel;
    private Set<Thread> animatedBorders;

    // effect variables
    private long effectGiven;
    private int effectTime;
    private boolean doublePoints;
    private long pauseEffectTimePassed;

    // effectbar
    private List<Point> effectBar;
    private Color effectBarColor;

    /**
     * This constructor initialises the game engine, sets the default
     * map model and calls the setup method in order to setup the snake object.
     */
    public Game() {
        // initialize effect variables
        this.effectBar = new CopyOnWriteArrayList<>();
        this.effectGiven = 0;
        this.effectTime = 10;
        this.doublePoints = false;
        this.pauseEffectTimePassed = 0;

        this.inputBlocked = false;
        this.running = true;
        this.pause = new AtomicBoolean(true);

        this.animatedBorders = new HashSet<>();

        gameEngine = GameEngine.create();

        this.mapModel = new NormalMap(this);
        gameEngine.setModel(mapModel);
        gameEngine.setMaps(Arrays.asList(new EmptyMap(this), new EasyMap(this), new NormalMap(this), new DifficultMap(this)));
        gameEngine.setPauseItems(new ContinueGameItem(this), new MapChangeItem(this), new EndGameItem(this));
        gameEngine.setInputListener(this);
        this.engineConfiguration = new EngineConfiguration(35, 35, 120);
        gameEngine.setUp(engineConfiguration);

        mapModel.setup();
        setup();

        gameEngine.start();
    }

    /**
     * This method setups the snake. After that it starts the repeating update task
     * and updates the DEATH_SCREEN and SCORE_UPDATE information to the engine.
     */
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

    /**
     * Calls a method from class {@link CustomMap} called updateFood
     * in order to spawn new food items.
     */
    private void generateNewFood() {
        mapModel.updateFood();
    }

    /**
     * Contains a thread which processes the movement of the snake.
     * The while-loop is delayed by the variable speed in the {@link Snake} class.
     * Furthermore, this method handles information like a contact with the border or
     * the own body.
     * After each successful run the score is updated.
     */
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
                            endGame(false);
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

    /**
     * Resets the snake object to the default values.
     */
    public void removeAllEffects() {
        this.snake.setSpeed(100);
        this.snake.setInvincible(false);

        this.effectGiven = 0;
        this.doublePoints = false;

        effectBar.clear();

        gameEngine.update(EngineUpdate.EFFECT_UPDATE, "Effekt:", "-/-");
    }

    /**
     * Changes the {@link #pause} attribute to the opposite value.
     * In addition to that, the time passed during an effect is changed so that the
     * timer can continue later successful.
     */
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

    /**
     * Changes {@link #running} attribute to false and {@link #pause} attribute to true.
     * Moreover, an EngineUpdate is set to load the death screen.
     */
    public void endGame(boolean win) {
        running = false;
        pause = new AtomicBoolean(true);

        if(!win)
            // activate death screen
            gameEngine.update(EngineUpdate.DEATH_SCREEN, true);
    }

    /**
     * Creates a new {@link #snake} object and resets all other variables to default.
     * Sends to EngineUpdates to reset the score and to disable the death screen.
     */
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

    /**
     * Processes keyboard input by the user.
     * If an input is not rendered yet it will be blocked by the {@link #inputBlocked} variable.
     *
     * @param keyCode pressed key
     */
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

    /**
     * Saves a timestamp in order to know when an effect was given to the player.
     *
     * @param effectGiven timestamp
     */
    public void setEffectGiven(long effectGiven) {
        this.effectGiven = effectGiven;
    }

    /**
     * Sets a boolean to save whether double points are enabled or not.
     *
     * @param doublePoints new boolean
     */
    public void setDoublePoints(boolean doublePoints) {
        this.doublePoints = doublePoints;
    }

    /**
     * Returns the configuration of the engine.
     *
     * @return engineConfiguration
     */
    public EngineConfiguration getEngineConfiguration() {
        return engineConfiguration;
    }

    /**
     * Returns pause boolean
     *
     * @return pause
     */
    public AtomicBoolean getPause() {
        return pause;
    }

    /**
     * Returns the time an effects stays.
     *
     * @return effectTime
     */
    public int getEffectTime() {
        return effectTime;
    }

    /**
     * Returns the timestamp when the player got an effect.
     *
     * @return effectGiven
     */
    public long getEffectGiven() {
        return effectGiven;
    }

    /**
     * Sets the color of the EffectBar
     *
     * @param effectBarColor new {@link Color} object
     */
    public void setEffectBarColor(Color effectBarColor) {
        this.effectBarColor = effectBarColor;
    }

    /**
     * Returns the effectBar object.
     *
     * @return effectBar
     */
    public List<Point> getEffectBar() {
        return effectBar;
    }

    /**
     * Returns the game engine.
     *
     * @return gameEngine
     */
    public GameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * Returns the snake object.
     *
     * @return snake
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     *
     * Returns the current color of the effect bar
     *
     * @return effectBarColor
     */
    public Color getEffectBarColor() {
        return effectBarColor;
    }

    /**
     * Returns current map model
     *
     * @return mapModel
     */
    public CustomMap getMapModel() {
        return mapModel;
    }

    /**
     * @param mapModel sets current map model.
     */
    public void setMapModel(CustomMap mapModel) {
        this.mapModel = mapModel;
    }

    /**
     * Returns a list of all running animation threads regrading moving borders.
     *
     * @return set containing threads
     */
    public Set<Thread> getAnimatedBorders() {
        return animatedBorders;
    }

    /**
     * @param inputBlocked enables or disables keyboard input
     */
    public void setInputBlocked(boolean inputBlocked) {
        this.inputBlocked = inputBlocked;
    }
}