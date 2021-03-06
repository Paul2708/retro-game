package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This overlay draws the map border.
 *
 * @author Paul2708
 */
public class BorderOverlay extends Overlay {

    private static final Color COLOR = new Color(139, 69, 19);

    /**
     * Nothing to do here.
     */
    @Override
    public void onInitialize() {
        // Not needed
    }

    /**
     * Draw the border points.
     *
     * @param graphics graphics
     */
    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(BorderOverlay.COLOR);

        for (Point point : getMap().getBorder()) {
            Point transformed = transform(point);

            graphics.fillRect(transformed.getX(), transformed.getY(), GameCanvas.BLOCK_SIZE, GameCanvas.BLOCK_SIZE);
        }
    }
}
