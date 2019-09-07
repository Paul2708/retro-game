package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.math.Point;
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
public class EasyMap extends CustomMap {

    /**
     * Calls the constructor from {@link CustomMap} and passes map name, the spawn point and
     * the current game object.
     *
     * @param game current game object
     */
    public EasyMap(Game game) {
        super("Leicht", Point.of(10, 2), game);
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

        switch (random.nextInt(7)) {
            case 1:
                special = new SpeedFood(specialX, specialY, game);
                break;
            case 2:
                special = new SlowFood(specialX, specialY, game);
                break;
            case 3:
                special = new InvincibleFood(specialX, specialY, game);
                break;
            case 5:
                special = new DoublePointsFood(specialX, specialY, game);
                break;
            case 6:
                special = new MapChangeFood(specialX, specialY, game);
                break;
        }

        if (special != null) {
            if(isFoodInBorder(special)) {
                generateSpecialFood();
            } else {
                getFood().add(special);
            }
        }
    }

    /**
     * Generates a custom border. In this case nothing like this is needed.
     */
    @Override
    protected void generateCustomBorder() {
        for (int x = 15; x < 20; x++) {
            for (int y = 15; y < 20; y++) {
                getBorderPoints().add(Point.of(x, y));
            }
        }
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
