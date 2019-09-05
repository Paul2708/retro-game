package de.devathlon.hnl.game.map;

import de.devathlon.hnl.core.map.MapConfiguration;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.game.food.Food;
import de.devathlon.hnl.game.food.SpecialFood;
import de.devathlon.hnl.game.food.foodtypes.*;
import de.devathlon.hnl.game.snake.Game;

import java.awt.*;
import java.util.Collection;
import java.util.Random;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class EasyMap extends CustomMap {

    public EasyMap(Game game) {
        super("Leicht", Point.of(10, 2), game);
    }

    @Override
    public void generateSpecialFood() {
        // special food
        Random random = new Random();
        Game game = getGame();

        int specialX = random.nextInt(game.getEngineConfiguration().getWidthInBlocks() - 2) + 1;
        int specialY = random.nextInt(game.getEngineConfiguration().getHeightInBlocks() - 7) + 1;

        SpecialFood special = null;

        switch (random.nextInt(6)) {
            case 1:
                special = new SpeedFood(specialX, specialY, game); // green (speed)
                break;
            case 2:
                special = new SlowFood(specialX, specialY, game); // blue (slowness)
                break;
            case 3:
                special = new InvincibleFood(specialX, specialY, game); // blue (slowness)
                break;
            case 5:
                special = new DoublePointsFood(specialX, specialY, game); // magenta (double points)
                break;
        }

        if (special != null) {
            getFood().add(special);
        }
    }

    @Override
    protected void generateCustomBorder() {
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
