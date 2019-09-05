package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class DeathOverlay extends Overlay {

    private static final Color[] COLORS = new Color[] {
            new Color(205, 55, 0),
            new Color(139, 37, 0),
            new Color(84, 84, 84),
            new Color(46, 46, 46),
            new Color(178, 28, 28),
            new Color(139, 44, 38)
    };

    private static final Color BORDER_COLOR = Color.GRAY;

    private Map<Point, Color> spreading;

    @Override
    public void onInitialize() {
        this.spreading = new HashMap<>();

        Dimension dimension = getCanvas().getGameDimension();
        int blockSize = GameCanvas.BLOCK_SIZE;
        int gap = GameCanvas.GAP;

        for (int i = 0; i < dimension.getWidth(); i += blockSize + gap / 2) {
            for (int j = 0; j < dimension.getHeight(); j += blockSize + gap / 2) {
                spreading.put(Point.of(i, j), randomColor());
            }
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        for (Map.Entry<Point, Color> entry : spreading.entrySet()) {
            Point point = entry.getKey();
            Color color = entry.getValue();

            graphics.setColor(color);
            graphics.fillRect(point.getX(), point.getY(), GameCanvas.BLOCK_SIZE + GameCanvas.GAP / 2,
                    GameCanvas.BLOCK_SIZE + GameCanvas.GAP / 2);
        }

        graphics.setColor(DeathOverlay.BORDER_COLOR);
        for (Point point : getMap().getBorder()) {
            Point transformed = transform(point);

            graphics.fillRect(transformed.getX(), transformed.getY(), GameCanvas.BLOCK_SIZE + GameCanvas.GAP / 2,
                    GameCanvas.BLOCK_SIZE + GameCanvas.GAP / 2);
        }
    }

    @Override
    public boolean isEnabled() {
        return getEngine().getGameState().isDead();
    }

    private Color randomColor() {
        return DeathOverlay.COLORS[ThreadLocalRandom.current().nextInt(DeathOverlay.COLORS.length)];
    }
}