package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.animation.Border;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.food.foodtypes.*;
import de.devathlon.hnl.game.snake.Game;

import java.util.Collection;
import java.util.Random;

/**
 * This class represents the map called "empty map".
 *
 * @author Leon
 */
public class DifficultMap extends CustomMap {

    /**
     * Calls the constructor from {@link CustomMap} and passes map name, the spawn point and
     * the current game object.
     *
     * @param game current game object
     */
    public DifficultMap(Game game) {
        super("Schwer", Point.of(10, 2), game);
    }

    /**
     * Method in oder to generate special food.
     * Picks the spawn point and examines the probability that such items will spawn.
     */
    @Override
    public void generateSpecialFood() {
        // special food
        Random random = new Random();
        Game game = getGame();

        int specialX = random.nextInt(game.getEngineConfiguration().getWidthInBlocks() - 2) + 1;
        int specialY = random.nextInt(game.getEngineConfiguration().getHeightInBlocks() - 7) + 1;

        SpecialFood special = null;

        switch (random.nextInt(30)) {
            case 1:
                special = new SpeedFood(specialX, specialY, game); // green (speed)
                break;
            case 2:
            case 3:
            case 4:
                special = new SlowFood(specialX, specialY, game); // blue (slowness)
                break;
            case 10:
                special = new InvincibleFood(specialX, specialY, game); // blue (slowness)
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                special = new BadFood(specialX, specialY, game); // gray (half snake disappears)
                break;
            case 20:
                special = new DoublePointsFood(specialX, specialY, game); // magenta (double points)
                break;
        }

        if (special != null) {
            getFood().add(special);
        }
    }

    /**
     * Generates a custom border.
     */
    @Override
    protected void generateCustomBorder() {
        for (int x = 5; x < 10; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 15; x < 20; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 25; x < 30; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 5; x < 10; x++) {
            for (int y = 25; y < 30; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 15; x < 20; x++) {
            for (int y = 25; y < 30; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 25; x < 30; x++) {
            for (int y = 25; y < 30; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 5; x < 10; x++) {
            for (int y = 5; y < 10; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 15; x < 20; x++) {
            for (int y = 5; y < 10; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 25; x < 30; x++) {
            for (int y = 5; y < 10; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int y = 20; y < 30; y++) {
            this.borderPoints.add(Point.of(7, y));
        }
        for (int y = 20; y < 30; y++) {
            this.borderPoints.add(Point.of(7, y));
        }
        for (int x = 10; x < 30; x++) {
            this.borderPoints.add(Point.of(x, 17));
        }
        for (int y = 10; y < 20; y++) {
            this.borderPoints.add(Point.of(27, y));
        }

        Border.animateMovingBorder(0, 1000, getGame(), 5, 30);
        Border.animateMovingBorder(5500, 1500, getGame(), 5, 30);
        Border.animateMovingBorder(10250, 2750, getGame(), 5, 30);
    }

    /**
     * Get a collection of all border points.
     *
     * @return collection of points
     */
    @Override
    public Collection<Point> getBorder() {
        return getBorderPoints();
    }
}
