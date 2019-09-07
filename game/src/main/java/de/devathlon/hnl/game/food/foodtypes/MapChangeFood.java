package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.animation.Effect;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used to create a food model
 * with the capabilities to activate an periodic map change.
 *
 * @author Leon
 */
public class MapChangeFood extends SpecialFood {

    /**
     * Default constructor which sets up the location, the color and whether
     * the special effect is one time or needs to be removed after a specific time.
     *
     * @param x    location
     * @param y    location
     * @param game current game object
     */
    public MapChangeFood(int x, int y, Game game) {
        super(x, y, new Color(123, 104, 238), game, false);
    }

    /**
     * Changes the background periodically each 2 seconds.
     */
    @Override
    protected void activateEffect() {
        new Timer().schedule(new TimerTask() {
            int timer = 0;
            long effectGiven = getGame().getEffectGiven();

            @Override
            public void run() {
                if (timer <= 10 && getGame().getEffectGiven() == effectGiven) {
                    if (timer % 2 == 0) {
                        // refresh background
                        getGame().getGameEngine().update(EngineUpdate.REFRESH_BACKGROUND);
                        System.out.println(timer);
                    }
                    timer++;
                }
            }
        }, 0, 2000);

        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Dein Effekt:", "Neuer Hintergrund!");
    }
}
