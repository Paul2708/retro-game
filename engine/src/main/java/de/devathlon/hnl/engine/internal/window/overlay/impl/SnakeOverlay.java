package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This overlay draws the snake entity.
 *
 * @author Paul2708
 */
public class SnakeOverlay extends Overlay {

    private static final Color HEAD_COLOR = Color.RED;

    private static final Color BODY_COLOR = Color.WHITE;

    /**
     * Nothing to do here.
     */
    @Override
    public void onInitialize() {
        // Not needed
    }

    /**
     * Get the snake model and render body and head.
     *
     * @param graphics graphics
     */
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