package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.animation.Border;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.food.foodtypes.*;
import de.devathlon.hnl.game.snake.Game;

import java.util.Collection;
import java.util.Random;

/**
 * This class represents the map called "normal map".
 *
 * @author Leon
 */
public class NormalMap extends CustomMap {

    /**
     * Calls the constructor from {@link CustomMap} and passes map name, the spawn point and
     * the current game object.
     *
     * @param game current game object
     */
    public NormalMap(Game game) {
        super("Normal", Point.of(10, 2), game);
    }

    /**
     * Method in oder to generate special food.
     * Picks the spawn point and examines the probability that such items will spawn.
     */
    @Override
    public void generateSpecialFood() {
        if(getGame().getSnake() != null && getGame().getSnake().calculateScore() > 5) {
            // special food
            Random random = new Random();
            Game game = getGame();

            int specialX = random.nextInt(game.getEngineConfiguration().getWidthInBlocks() - 2) + 1;
            int specialY = random.nextInt(game.getEngineConfiguration().getHeightInBlocks() - 7) + 1;

            SpecialFood special = null;

            switch (random.nextInt(20)) {
                case 1:
                    special = new SpeedFood(specialX, specialY, game); // green (speed)
                    break;
                case 2:
                    special = new SlowFood(specialX, specialY, game); // blue (slowness)
                    break;
                case 3:
                    special = new InvincibleFood(specialX, specialY, game); // blue (slowness)
                    break;
                case 4:
                    special = new BadFood(specialX, specialY, game); // gray (half snake disappears)
                    break;
                case 5:
                    special = new DoublePointsFood(specialX, specialY, game); // magenta (double points)
                    break;
            }

            if (special != null) {
                if (isFoodInBorder(special)) {
                    generateSpecialFood();
                } else {
                    getFood().add(special);
                }
            }
        }
    }

    /**
     * Generates custom borders.
     */
    @Override
    protected void generateCustomBorder() {
        for (int x = 5; x < 20; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }
        for (int x = 25; x < 30; x++) {
            for (int y = 15; y < 20; y++) {
                this.borderPoints.add(Point.of(x, y));
            }
        }

        Border.animateMovingBorder(0, 1000, getGame(), 25, 30);
        Border.animateMovingBorder(5500, 1500, getGame(), 25, 30);
    }

    /**
     * Returns border points from class {@link Point}.
     *
     * @return a collection with points.
     */
    @Override
    public Collection<Point> getBorder() {
        return getBorderPoints();
    }
}
