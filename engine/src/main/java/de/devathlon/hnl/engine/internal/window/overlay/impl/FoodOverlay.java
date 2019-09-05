package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Graphics2D;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class FoodOverlay extends Overlay {

    @Override
    public void onInitialize() {

    }

    @Override
    public void onRender(Graphics2D graphics) {
        for (FoodModel food : getMap().getFood()) {
            Point transform = transform(food.getLocation());

            graphics.setColor(food.getColor());
            graphics.fillRect(transform.getX(), transform.getY(), GameCanvas.BLOCK_SIZE, GameCanvas.BLOCK_SIZE);
        }
    }
}