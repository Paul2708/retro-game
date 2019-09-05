package de.devathlon.hnl.engine.window.overlay.impl;

import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.overlay.Overlay;

import java.awt.*;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class SnakeOverlay extends Overlay {

    private static final Color HEAD_COLOR = Color.RED;

    private static final Color BODY_COLOR = Color.WHITE;

    @Override
    public void onInitialize() {

    }

    @Override
    public void onRender(Graphics2D graphics) {
        SnakeModel snakeModel = getMap().getSnake();

        // Draw body
        graphics.setColor(SnakeOverlay.BODY_COLOR);
        for (Point point : snakeModel.getBodyPoints()) {
            Point transform = transform(point);

            graphics.fillRect(transform.getX(), transform.getY(), GameCanvas.BLOCK_SIZE, GameCanvas.BLOCK_SIZE);
        }

        // Draw head
        graphics.setColor(SnakeOverlay.HEAD_COLOR);
        graphics.fillRect(transform(snakeModel.getHeadPoint()).getX(),
                transform(snakeModel.getHeadPoint()).getY(), GameCanvas.BLOCK_SIZE, GameCanvas.BLOCK_SIZE);
    }
}