package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.map.MapConfiguration;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.animation.Effect;
import de.devathlon.hnl.game.food.Food;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represents the abstract class CustomMap.
 * All new maps will inherit from this class.
 *
 * @author Leon
 */
public abstract class CustomMap implements MapModel {

    private final Game game;

    private final MapConfiguration mapConfiguration;

    private List<FoodModel> foodList;
    private List<Point> borderPoints;

    /**
     * Default constructor for all maps.
     *
     * @param name of the map
     * @param spawnPoint of the snake
     * @param game current game object
     */
    CustomMap(String name, Point spawnPoint, Game game) {
        this.game = game;
        this.foodList = new CopyOnWriteArrayList<>();
        this.borderPoints = new CopyOnWriteArrayList<>();

        this.mapConfiguration = new MapConfiguration(name, new Dimension(35, 35), spawnPoint);
    }

    /**
     * This method should only be called after the game engine registered the new map.
     * It will stop all tasks from old maps and setups new content.
     */
    public void setup() {
        game.getAnimatedBorders().forEach(Thread::stop);
        game.getAnimatedBorders().clear();

        generateDefaultBorder();
        generateCustomBorder();
        startFoodTimer();
        updateFood();
    }

    /**
     * Removes food effect and special food after an period of time.
     */
    private void startFoodTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!game.getPause().get()) {
                    if (game.getEffectGiven() != 0) {
                        long secondsLeft = ((System.currentTimeMillis() - game.getEffectGiven()) / 1000);
                        Effect.animateTimer(getGame(), (int) secondsLeft);
                        if (secondsLeft >= getGame().getEffectTime()) {
                            game.removeAllEffects();
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
        }, 0, 1000);
    }

    /**
     * Generates new food and special food items.
     */
    public void updateFood() {
        generateFood();
        generateSpecialFood();
    }

    /**
     * Method examining the possibility that special food can spawn.
     */
    protected abstract void generateSpecialFood();

    /**
     * Method which ads points to {@link #borderPoints}
     */
    protected abstract void generateCustomBorder();

    /**
     * Adds {@link Food} objects to {@link #foodList} which random locations.
     */
    private void generateFood() {
        Random random = new Random();

        int x = random.nextInt(game.getEngineConfiguration().getWidthInBlocks() - 2) + 1;
        int y = random.nextInt(game.getEngineConfiguration().getHeightInBlocks() - 7) + 1;

        Food food = new Food(x, y, new Color(238, 118, 0));

        if (isFoodInBorder(food)) {
            generateFood();
        } else {
            foodList.add(food);
        }
    }

    /**
     * Compares x and y with all border points to see whether food spawns
     * in an border object.
     *
     * @param foodModel the new food object
     * @return boolean whether food would spawn in an border point
     */
    boolean isFoodInBorder(FoodModel foodModel) {
        for (Point borderPoint : this.borderPoints) {
            if (foodModel.getLocation().getX() == borderPoint.getX() && foodModel.getLocation().getY() == borderPoint.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the default rectangle border points.
     */
    private void generateDefaultBorder() {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < getGame().getEngineConfiguration().getWidthInBlocks(); x++) {
                this.borderPoints.add(Point.of(x, 0));
                this.borderPoints.add(Point.of(x, getGame().getEngineConfiguration().getHeightInBlocks() - 4));
            }
            for (int y = 0; y < getGame().getEngineConfiguration().getHeightInBlocks() - 4; y++) {
                this.borderPoints.add(Point.of(0, y));
                this.borderPoints.add(Point.of(getGame().getEngineConfiguration().getWidthInBlocks() - 1, y));
            }
        }
    }

    /**
     * Get the snake entity model.
     *
     * @return snake model
     */
    @Override
    public SnakeModel getSnake() {
        return game.getSnake();
    }

    /**
     * Returns a list with all food models
     *
     * @return food list
     */
    public List<FoodModel> getFood() {
        return foodList;
    }

    /**
     * @return game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns a list with all border points
     *
     * @return border list
     */
    public List<Point> getBorderPoints() {
        return borderPoints;
    }

    /**
     * @return an new EffectBarModel
     */
    @Override
    public EffectBarModel getEffectBar() {
        return new EffectBarModel() {
            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public Color getColor() {
                return game.getEffectBarColor();
            }

            @Override
            public Collection<Point> getBar() {
                return game.getEffectBar();
            }
        };
    }

    /**
     * @return the map configuration
     */
    @Override
    public MapConfiguration getConfiguration() {
        return mapConfiguration;
    }
}