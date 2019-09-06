package de.devathlon.hnl.game.food;

import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

/**
 * This class represents the abstract class SpecialFood.
 * All special food objects will inherit from this class.
 *
 * @author Leon
 */
public abstract class SpecialFood extends Food {

    private int timeAlive;
    private Game game;
    private boolean withTimer;

    /**
     * Default constructor for an new special food object.
     *
     * @param x location
     * @param y location
     * @param color {@link Color} object to define the color
     * @param game current game
     * @param timer boolean whether the effect needs to be removed
     */
    public SpecialFood(int x, int y, Color color, Game game, boolean timer) {
        super(x, y, color);
        this.timeAlive = 20;
        this.game = game;
        this.withTimer = timer;
    }

    /**
     * Removes all effects from the player and sets the
     * time if it needs to be removed.
     */
    public void apply() {
        game.removeAllEffects();
        activateEffect();
        if (isWithTimer()) {
            game.setEffectGiven(System.currentTimeMillis());
        }
    }

    /**
     * Activates an effect.
     */
    protected abstract void activateEffect();

    /**
     * Sets how long the food item is visible till yet.
     *
     * @param timeAlive integer
     */
    public void setTimeAlive(int timeAlive) {
        this.timeAlive = timeAlive;
    }

    /**
     * @return int how long the object is visible on the map
     */
    public int getTimeAlive() {
        return timeAlive;
    }

    /**
     * @return current game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return boolean whether the effect needs to be removed after a specific time
     */
    private boolean isWithTimer() {
        return withTimer;
    }
}
