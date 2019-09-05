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

public class EmptyMap extends CustomMap {

    public EmptyMap(Game game) {
        super("Leer", Point.of(10, 2), game);
    }

    @Override
    public void generateSpecialFood() {
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
            getFood().add(special);
        }
    }

    @Override
    protected void generateCustomBorder() {

    }

    @Override
    public Collection<Point> getBorder() {
        return getBorderPoints();
    }
}
