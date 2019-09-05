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
public class BackgroundOverlay extends Overlay {

    private static final Color[] COLORS = new Color[] {
            new Color(102, 153, 51),
            new Color(143, 197, 98),
            new Color(72, 141, 29),
            new Color(51, 153, 51)
    };

    private static final Color[] DEATH_COLORS = new Color[] {
            new Color(205, 55, 0),
            new Color(139, 37, 0),
            new Color(84, 84, 84),
            new Color(46, 46, 46),
            new Color(178, 28, 28),
            new Color(139, 44, 38)
    };

    private Map<Point, Color> spreading;
    private Map<Point, Color> deadSpreading;

    @Override
    public void onInitialize() {
        this.spreading = new HashMap<>();
        this.deadSpreading = new HashMap<>();

        Dimension dimension = getCanvas().getGameDimension();
        int blockSize = GameCanvas.BLOCK_SIZE;
        int gap = GameCanvas.GAP;

        for (int i = 0; i < dimension.getWidth(); i += blockSize + gap / 2) {
            for (int j = 0; j < dimension.getHeight(); j += blockSize + gap / 2) {
                spreading.put(Point.of(i, j), randomColor());
                deadSpreading.put(Point.of(i, j), randomDeadColor());
            }
        }
    }

    @Override
    public void onRender(Graphics2D graphics) {
        Map<Point, Color> map;
        if (getEngine().isDead()) {
            map = deadSpreading;
        } else {
            map = spreading;
        }

        for (Map.Entry<Point, Color> entry : map.entrySet()) {
            Point point = entry.getKey();
            Color color = entry.getValue();

            graphics.setColor(color);
            graphics.fillRect(point.getX(), point.getY(), GameCanvas.BLOCK_SIZE + GameCanvas.GAP / 2,
                    GameCanvas.BLOCK_SIZE + GameCanvas.GAP / 2);
        }
    }

    private Color randomColor() {
        return BackgroundOverlay.COLORS[ThreadLocalRandom.current().nextInt(BackgroundOverlay.COLORS.length)];
    }

    private Color randomDeadColor() {
        return BackgroundOverlay.DEATH_COLORS[ThreadLocalRandom.current().nextInt(BackgroundOverlay.DEATH_COLORS.length)];
    }
}