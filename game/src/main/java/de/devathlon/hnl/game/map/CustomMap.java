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
 * Class description.
 *
 * @author Paul2708
 */
public abstract class CustomMap implements MapModel {

    private final Game game;

    private MapConfiguration mapConfiguration;

    protected final List<FoodModel> foodList;
    protected final List<Point> borderPoints;

    public CustomMap(String name, Point spawnPoint, Game game) {
        this.game = game;
        this.foodList = new CopyOnWriteArrayList<>();
        this.borderPoints = new CopyOnWriteArrayList<>();

        this.mapConfiguration = new MapConfiguration(name, new Dimension(35, 35), spawnPoint);
    }

    public void setup() {
        game.getAnimatedBorders().forEach(Thread::interrupt);

        generateDefaultBorder();
        generateCustomBorder();
        startFoodTimer();
        updateFood();
    }

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

    public void updateFood() {
        generateFood();
        generateSpecialFood();
    }

    protected abstract void generateSpecialFood();

    protected abstract void generateCustomBorder();

    protected void generateFood() {
        Random random = new Random();

        int x = random.nextInt(game.getEngineConfiguration().getWidthInBlocks() - 2) + 1;
        int y = random.nextInt(game.getEngineConfiguration().getHeightInBlocks() - 7) + 1;

        Food food = new Food(x, y, new Color(238, 118, 000));

        if (checkIfFoodIsInBorder(food)) {
            generateFood();
            return;
        }

        foodList.add(food);
    }

    private boolean checkIfFoodIsInBorder(FoodModel foodModel) {
        for (Point borderPoint : this.borderPoints) {
            if (foodModel.getLocation().getX() == borderPoint.getX() && foodModel.getLocation().getY() == borderPoint.getY()) {
                return true;
            }
        }
        return false;
    }

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

    public List<FoodModel> getFood() {
        return foodList;
    }

    public Game getGame() {
        return game;
    }

    public List<Point> getBorderPoints() {
        return borderPoints;
    }

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

    @Override
    public MapConfiguration getConfiguration() {
        return mapConfiguration;
    }
}