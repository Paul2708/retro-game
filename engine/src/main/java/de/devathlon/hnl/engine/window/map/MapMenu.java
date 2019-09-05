package de.devathlon.hnl.engine.window.map;

import de.devathlon.hnl.core.MapModel;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class description.
 *
 * @author Paul2708
 */
public final class MapMenu {

    // TODO: Center items (more) correctly

    private static final String PIXEL_FONT = "/pixel_font.ttf";

    private List<MapModel> items;
    private Dimension dimension;

    private Font font;

    private Map<MapModel, Rectangle> itemMap;

    private Consumer<String> consumer;

    private boolean enabled;

    public void load(List<MapModel> maps, Dimension dimension) {
        this.items = maps;
        Collections.reverse(maps);

        this.dimension = dimension;

        this.enabled = false;

        // Load custom font
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try (InputStream stream = getClass().getResourceAsStream(MapMenu.PIXEL_FONT)) {
            this.font = Font.createFont(Font.TRUETYPE_FONT, stream);
            environment.registerFont(font);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        font = font.deriveFont(25f).deriveFont(Font.BOLD);

        // Init items
        this.itemMap = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            MapModel item = items.get(i);
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
            MapModel item = items.get(i);
            Rectangle rectangle = itemMap.get(item);

            drawItem(graphics, item);
        }
    }

    public MapModel getItemByPoint(Point point) {
        for (Map.Entry<MapModel, Rectangle> entry : itemMap.entrySet()) {
            if (entry.getValue().contains(point)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void setPause(boolean pause) {
        this.enabled = pause;
    }

    private Rectangle calculateBorder(MapModel item) {
        FontRenderContext renderContext = new FontRenderContext(null, true, true);
        Rectangle2D bounds = font.getStringBounds(item.getName(), renderContext);

        int x = (int) ((dimension.getWidth() / 2) - (bounds.getWidth() / 2) - bounds.getX());
        int y = (int) ((dimension.getHeight() / 2) - (bounds.getHeight() / 2) - bounds.getY());

        return new Rectangle(x, y - items.indexOf(item) * 100 - (int) bounds.getHeight(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    private void drawItem(Graphics2D graphics, MapModel item) {
        Rectangle rectangle = itemMap.get(item);

        graphics.drawString(item.getName(), (int) rectangle.getX(), (int) rectangle.getY() + (int) rectangle.getHeight());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setConsumer(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public Consumer<String> getConsumer() {
        return consumer;
    }
}