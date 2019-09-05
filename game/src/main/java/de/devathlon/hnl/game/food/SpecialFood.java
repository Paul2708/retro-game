package de.devathlon.hnl.game.food;

import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

public abstract class SpecialFood extends Food {

    private int timeAlive;
    private Game game;
    private boolean withTimer;

    public SpecialFood(int x, int y, Color color, Game game, boolean timer) {
        super(x, y, color);
        this.timeAlive = 20;
        this.game = game;
        this.withTimer = timer;
    }

    public void apply() {
        game.removeAllEffects();
        activateEffect();
        if (isWithTimer()) {
            game.setEffectGiven(System.currentTimeMillis());
        }
    }

    protected abstract void activateEffect();

    public void setTimeAlive(int timeAlive) {
        this.timeAlive = timeAlive;
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public Game getGame() {
        return game;
    }

    public boolean isWithTimer() {
        return withTimer;
    }
}
