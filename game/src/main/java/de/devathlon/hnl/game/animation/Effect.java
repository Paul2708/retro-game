package de.devathlon.hnl.game.animation;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.snake.Game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Effect {

    public static void animateTimer(Game game, int timePassed) {
        int timeLeft = game.getEffectTime() - timePassed;
        List<Point> pointsToAdd = new CopyOnWriteArrayList<>();

        if (timeLeft == game.getEffectTime()) {
            for (int i = timeLeft; i >= 0; i--) {
                pointsToAdd.add(Point.of(2 + i, game.getEngineConfiguration().getHeightInBlocks() - 2));
            }
        } else {
            System.out.println("REMOVE");
            System.out.println("x: " + (2+ (timeLeft + 1)));
            Point point = Point.of(2 + (timeLeft + 1), game.getEngineConfiguration().getHeightInBlocks() - 2);
            game.getBorderPoints().remove(point);
        }
        System.out.println("TimeLeft: " + timeLeft);
        game.getBorderPoints().addAll(pointsToAdd);
    }
}
