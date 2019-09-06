package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.awt.*;

/**
 * This class is used to create a food model
 * with the capabilities to activate a slow effect
 *
 * @author Leon
 */
public class SlowFood extends SpecialFood {

    /**
     * Default constructor which sets up the location, the color and whether
     * the special effect is one time or needs to be removed after a specific time.
     *
     * @param x location
     * @param y location
     * @param game current game object
     */
    public SlowFood(int x, int y, Game game) {
        super(x, y, new Color(024, 116, 205), game, true);

    }

    /**
     * Sets the new speed value and updates the view to show the new effect name.
     */
    @Override
    protected void activateEffect() {
        getGame().getSnake().setSpeed(300);
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, Messages.EFFECT_UPDATE, "Verlangsamung");
    }
}
