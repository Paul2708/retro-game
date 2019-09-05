package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.awt.*;

public class DoublePointsFood extends SpecialFood {

    public DoublePointsFood(int x, int y, Game game) {
        super(x, y, Color.PINK, game, true);
    }

    @Override
    protected void activateEffect() {
        getGame().setDoublePoints(true);
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, Messages.EFFECT_UPDATE + "Doppelte Punkte");
    }
}
