package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

/**
 * This class is used to create a food model
 * with the capabilities to activate a bad effect
 *
 * @author Leon
 */
public class BadFood extends SpecialFood {

    /**
     * Default constructor which sets up the location, the color and whether
     * the special effect is one time or needs to be removed after a specific time.
     *
     * @param x location
     * @param y location
     * @param game current game object
     */
    public BadFood(int x, int y, Game game) {
        super(x, y, new Color(139, 125, 107), game, false);
    }

    /**
     * Removes half body points of the {@link Snake} and updates the view to show the new effect name.
     * The default value will be used, if there are less then default body points left.
     */
    @Override
    protected void activateEffect() {
        Snake snake = getGame().getSnake();
        int halfSize = (snake.getBodyPoints().size() / 2);
        if (halfSize < snake.getDefaultLength()) {
            halfSize = snake.getDefaultLength();
        }
        for (int i = snake.getBodyPoints().size() - 1; i > halfSize; i--) {
            snake.getBodyPoints().remove(i);
        }
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Deine Schlange", "wurde verkürzt :(");
        snake.updateScore(getGame().getGameEngine());
    }
}
