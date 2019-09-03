package de.devathlon.hnl.game.animation;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

public class Effect {

    public static void animateTimer(Game game, int timePassed) {
        int timeLeft = game.getEffectTime() - timePassed;

        if (timeLeft == game.getEffectTime()) {
            for (int i = timeLeft; i > 0; i--) {
                game.getBorderPoints().add(Point.of(2 + i, game.getEngineConfiguration().getHeightInBlocks() - 2));
            }
        } else {
            Point point = Point.of(2 + (timeLeft + 1), game.getEngineConfiguration().getHeightInBlocks() - 2);

            System.out.println( point);

            game.getBorderPoints().remove(point);
        }

    }
}
