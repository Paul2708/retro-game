package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This overlay draws the death screen, if the player dies.
 *
 * @author Paul2708
 */
public class DeathOverlay extends Overlay {

    private static final Color[] COLORS = new Color[]{
            new Color(205, 55, 0),
            new Color(139, 37, 0),
            new Color(84, 84, 84),
            new Color(46, 46, 46),
            new Color(178, 28, 28),
            new Color(139, 44, 38)
    };

    private static final Color BORDER_COLOR = Color.GRAY;

    private Map<Point, Color> spreading;

    /**
     * Map all points to a color.
     */
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

    /**
     * Fill the points with the given color and change the border color.
     *
     * @param graphics graphics
     */
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

        drawText("You died.", graphics);
    }

    /**
     * The overlay is enabled if the game state states, that the player is dead.
     *
     * @return true if the player is dead, otherwise false
     */
    @Override
    public boolean isEnabled() {
        return getEngine().getGameState().isDead();
    }

    /**
     * Draw the text in the center to the screen.
     *
     * @param text     text to draw
     * @param graphics graphics
     */
    private void drawText(String text, Graphics2D graphics) {
        graphics.setColor(Color.WHITE);

        Font font = getFont().deriveFont(30f).deriveFont(Font.BOLD);

        graphics.setFont(font);

        FontRenderContext renderContext = new FontRenderContext(null, true, true);
        Rectangle2D bounds = font.getStringBounds(text, renderContext);

        Dimension dimension = getCanvas().getGameDimension();

        int x = (int) ((dimension.getWidth() / 2) - (bounds.getWidth() / 2) - bounds.getX());
        int y = (int) ((dimension.getHeight() / 2) - (bounds.getHeight() / 2) - bounds.getY());

        graphics.drawString(text, x, y);
    }

    /**
     * Create a random color from {@link #COLORS}.
     *
     * @return random color
     */
    private Color randomColor() {
        return DeathOverlay.COLORS[ThreadLocalRandom.current().nextInt(DeathOverlay.COLORS.length)];
    }
}