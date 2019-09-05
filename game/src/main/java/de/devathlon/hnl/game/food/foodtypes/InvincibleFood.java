package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;
import de.devathlon.hnl.game.util.Messages;

import java.awt.*;

public class InvincibleFood extends SpecialFood {

    public InvincibleFood(int x, int y, Game game) {
        super(x, y, Color.RED, game, true);

    }

    @Override
    protected void activateEffect() {
        getGame().getSnake().setInvincible(true);
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, Messages.EFFECT_UPDATE + "Unbesiegbar");
    }
}
