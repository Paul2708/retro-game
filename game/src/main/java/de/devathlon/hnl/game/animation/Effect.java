package de.devathlon.hnl.game.animation;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;

public class Effect {

    public static void animateTimer(Game game, int timePassed) {
        game.setEffectBarColor(Color.GREEN);
        int timeLeft = game.getEffectTime() - timePassed;

        int startX = 11;

        if (timeLeft == game.getEffectTime()) {
            for (int i = timeLeft; i > 0; i--) {
                game.getEffectBar().add(Point.of(startX + i, game.getEngineConfiguration().getHeightInBlocks() - 2));
            }
        } else {
            if(timeLeft <= 3) {
                game.setEffectBarColor(Color.PINK);
            }
            Point point = Point.of(startX + (timeLeft + 1), game.getEngineConfiguration().getHeightInBlocks() - 2);

            game.getEffectBar().remove(point);
        }

    }
}
