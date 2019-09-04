package de.devathlon.hnl.game.food;

import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.game.animation.Effect;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

public class SpecialFood extends Food {

    private int timeAlive;

    public SpecialFood(int x, int y, Color color) {
        super(x, y, color);
        this.timeAlive = 2;
    }

    public void activateEffect(Game game, Snake snake) {
        // update score
        game.removeAllEffects();
        if (getColor() == Color.GREEN) {
            snake.setSpeed(50);
            game.getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Dein aktueller Effekt: Speed");
        } else if (getColor() == Color.BLUE) {
            snake.setSpeed(300);
            game.getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Dein aktueller Effekt: Verlangsamung");
        } else if (getColor() == Color.RED) {
            snake.setInvincible(true);
            game.getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Dein aktueller Effekt: Unbesiegbar");
        } else if (getColor() == Color.GRAY) {
            for (int i = snake.getBodyPoints().size() - 1; i > (snake.getBodyPoints().size() / 2); i--) {
                snake.getBodyPoints().remove(i);
            }
            return; // return if no timer is needed
        } else if (getColor() == Color.YELLOW) {
            game.setDoublePoints(true);
            game.getGameEngine().update(EngineUpdate.EFFECT_UPDATE, "Dein aktueller Effekt: Doppelte Punkte");
        }
        game.setEffectGiven(System.currentTimeMillis());
    }

    public void setTimeAlive(int timeAlive) {
        this.timeAlive = timeAlive;
    }

    public int getTimeAlive() {
        return timeAlive;
    }
}
