package de.devathlon.hnl.game.food.foodtypes;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

public class BadFood extends SpecialFood {

    public BadFood(int x, int y, Game game) {
        super(x, y, new Color(139, 125, 107), game, false);
    }

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
        getGame().getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Deine Schlange wurde verkürzt :(");
        snake.updateScore(getGame().getGameEngine());
    }
}
