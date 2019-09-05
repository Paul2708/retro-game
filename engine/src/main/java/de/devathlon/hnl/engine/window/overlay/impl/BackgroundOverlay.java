package de.devathlon.hnl.engine.window.overlay.impl;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.overlay.Overlay;

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
    }

    private Color randomColor() {
        return BackgroundOverlay.COLORS[ThreadLocalRandom.current().nextInt(BackgroundOverlay.COLORS.length)];
    }
}