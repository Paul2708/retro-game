package de.devathlon.hnl.engine.window.pause;

import de.devathlon.hnl.core.pause.PauseItem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Class description.
 *
 * @author Paul2708
 */
public final class PauseMenu {

    // TODO: Center items (more) correctly

    private static final String PIXEL_FONT = "/pixel_font.ttf";

    private List<PauseItem> items;
    private Dimension dimension;

    private Font font;

    private Map<PauseItem, Rectangle> itemMap;

    private boolean enabled;

    public void load(List<PauseItem> itemList, Dimension dimension) {
        this.items = itemList;
        Collections.reverse(itemList);

        this.dimension = dimension;

        this.enabled = false;

        // Load custom font
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try (InputStream stream = getClass().getResourceAsStream(PauseMenu.PIXEL_FONT)) {
            this.font = Font.createFont(Font.TRUETYPE_FONT, stream);
            environment.registerFont(font);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        font = font.deriveFont(25f).deriveFont(Font.BOLD);

        // Init items
        this.itemMap = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            PauseItem item = items.get(i);
            itemMap.put(item, calculateBorder(item));
        }
    }

    public void render(Graphics2D graphics) {
        if (!enabled) {
            return;
        }

        graphics.setColor(Color.BLACK);
        graphics.setFont(font);

        for (int i = 0; i < items.size(); i++) {
            PauseItem item = items.get(i);
            Rectangle rectangle = itemMap.get(item);

            drawItem(graphics, item);
        }
    }

    public PauseItem getItemByPoint(Point point) {
        for (Map.Entry<PauseItem, Rectangle> entry : itemMap.entrySet()) {
            if (entry.getValue().contains(point)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void setPause(boolean pause) {
        this.enabled = pause;
    }

    private Rectangle calculateBorder(PauseItem item) {
        FontRenderContext renderContext = new FontRenderContext(null, true, true);
        Rectangle2D bounds = font.getStringBounds(item.getTitle(), renderContext);

        int x = (int) ((dimension.getWidth() / 2) - (bounds.getWidth() / 2) - bounds.getX());
        int y = (int) ((dimension.getHeight() / 2) - (bounds.getHeight() / 2) - bounds.getY());

        return new Rectangle(x, y - items.indexOf(item) * 100 - (int) bounds.getHeight(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    private void drawItem(Graphics2D graphics, PauseItem item) {
        Rectangle rectangle = itemMap.get(item);

        graphics.drawString(item.getTitle(), (int) rectangle.getX(), (int) rectangle.getY() + (int) rectangle.getHeight());
    }

    public boolean isEnabled() {
        return enabled;
    }
}