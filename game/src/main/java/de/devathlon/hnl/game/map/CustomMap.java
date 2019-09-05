package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Class description.
 *
 * @author Paul2708
 */
public abstract class CustomMap implements MapModel {

    private final Game game;

    protected final Collection<FoodModel> foodList;

    public CustomMap(Game game) {
        this.game = game;
        this.foodList = new ArrayList<>();
    }

    public abstract void updateFood();

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
     * Get a collection of all current food entity models.
     *
     * @return food model
     */
    @Override
    public Collection<FoodModel> getFood() {
        return foodList;
    }

    /**
     * Get the effect bar model.
     *
     * @return effect bar model
     */
    @Override
    public EffectBarModel getEffectBar() {
        return null;
    }
}
