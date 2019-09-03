package de.devathlon.hnl.game.food;

import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

public class SpecialFood extends Food {

    public SpecialFood(int x, int y, Color color) {
        super(x, y, color);
    }

    public void activateEffect(Game game, Snake snake) {
        game.removeAllEffects();
        if (getColor() == Color.GREEN) {
            snake.setSpeed(50);
        } else if (getColor() == Color.BLUE) {
            snake.setSpeed(300);
        } else if (getColor() == Color.RED) {
            snake.setInvincible(true);
        } else if (getColor() == Color.GRAY) {
            for (int i = 0; i < snake.getBodyPoints().size() / 2; i++) {
                snake.getBodyPoints().remove(i);
                i--;
            }
            return; // return if no timer is needed
        } else if (getColor() == Color.YELLOW) {
            game.setDoublePoints(true);
        }
        game.setEffectGiven(System.currentTimeMillis());
    }
}
