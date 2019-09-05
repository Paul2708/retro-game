package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.awt.*;

public class SpeedFood extends SpecialFood {

    public SpeedFood(int x, int y, Game game) {
        super(x, y, new Color(127, 255, 212), game, true);
    }

    @Override
    protected void activateEffect() {
        getGame().getSnake().setSpeed(75);
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, Messages.EFFECT_UPDATE + "Speed");
    }
}
