package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.*;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class BorderOverlay extends Overlay {

    private static final Color COLOR = new Color(139, 69, 19);

    @Override
    public void onInitialize() {

    }

    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(BorderOverlay.COLOR);

        for (Point point : getMap().getBorder()) {
            Point transformed = transform(point);

            graphics.fillRect(transformed.getX(), transformed.getY(), GameCanvas.BLOCK_SIZE, GameCanvas.BLOCK_SIZE);
        }
    }
}
