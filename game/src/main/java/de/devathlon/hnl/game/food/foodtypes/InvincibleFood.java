package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.awt.*;

/**
 * This class is used to create a food model
 * with the capabilities to activate an invincible effect
 *
 * @author Leon
 */
public class InvincibleFood extends SpecialFood {

    /**
     * Default constructor which sets up the location, the color and whether
     * the special effect is one time or needs to be removed after a specific time.
     *
     * @param x location
     * @param y location
     * @param game current game object
     */
    public InvincibleFood(int x, int y, Game game) {
        super(x, y, new Color(205, 45, 0), game, true);

    }

    /**
     * Sets the new invincible value and updates the view to show the new effect name.
     */
    @Override
    protected void activateEffect() {
        getGame().getSnake().setInvincible(true);
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, Messages.EFFECT_UPDATE, "Unbesiegbar");
    }
}
