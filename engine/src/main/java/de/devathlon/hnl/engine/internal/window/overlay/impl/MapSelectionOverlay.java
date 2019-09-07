package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This overlay represents the map selection overlay.
 * It holds {@link MapModel}s that are displayed.
 *
 * @author Paul2708
 */
public class MapSelectionOverlay extends Overlay implements MouseListener {

    // TODO: Center items (more) correctly

    private Font font;

    private List<MapModel> mapPool;
    private Map<MapModel, Rectangle> rectangles;

    private Consumer<MapModel> consumer;

    /**
     * Adjust the font, get all map models and calculate the clickable borders for them.
     */
    @Override
    public void onInitialize() {
        this.font = getFont().deriveFont(25f).deriveFont(Font.BOLD);

        this.mapPool = getEngine().getGameState().getMapPool();
        Collections.reverse(mapPool);

        this.rectangles = new HashMap<>();
        for (MapModel item : mapPool) {
            rectangles.put(item, calculateBorder(item));
        }
    }

    /**
     * Draw the items.
     *
     * @param graphics graphics
     * @see #drawItem(Graphics2D, MapModel)
     */
    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);

        for (MapModel item : mapPool) {
            drawItem(graphics, item);
        }
    }

    /**
     * If the overlay got activated, the current listener wil be removed.
     * Otherwise the listener will be added to be active.
     *
     * @param activate activate or disable this overlay
     */
    @Override
    public void activate(boolean activate) {
        super.activate(activate);

        if (activate) {
            getCanvas().addMouseListener(this);
        } else {
            getCanvas().removeMouseListener(this);
        }
    }

    /**
     * Set the consumer that will be used if a player clicks a map model.
     *
     * @param consumer consumer that consumes a map model an map selection
     */
    public void setConsumer(Consumer<MapModel> consumer) {
        this.consumer = consumer;
    }

    /**
     * Get the map model by point.
     *
     * @param point clicked point
     * @return map model or null if none was found
     */
    private MapModel getItemByPoint(Point point) {
        for (Map.Entry<MapModel, Rectangle> entry : rectangles.entrySet()) {
            if (entry.getValue().contains(point)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Calculate the border a given map model.
     *
     * @param item map model item
     * @return clickable rectangle area
     */
    private Rectangle calculateBorder(MapModel item) {
        FontRenderContext renderContext = new FontRenderContext(null, true, true);
        Rectangle2D bounds = font.getStringBounds(item.getConfiguration().getName(), renderContext);

        Dimension dimension = getCanvas().getGameDimension();

        int x = (int) ((dimension.getWidth() / 2) - (bounds.getWidth() / 2) - bounds.getX());
        int y = (int) ((dimension.getHeight() / 2) - (bounds.getHeight() / 2) - bounds.getY());

        return new Rectangle(x, y - mapPool.indexOf(item) * 100 - (int) bounds.getHeight(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    /**
     * Draw the item to the screen.
     *
     * @param graphics graphics
     * @param item     pause item
     */
    private void drawItem(Graphics2D graphics, MapModel item) {
        Rectangle rectangle = rectangles.get(item);

        graphics.drawString(item.getConfiguration().getName(), (int) rectangle.getX(), (int) rectangle.getY()
                + (int) rectangle.getHeight());
    }

    /**
     * Check if the player selected a map item.
     * If so, the consumer consumes the selected map.
     *
     * @param event mouse event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        MapModel item = getItemByPoint(event.getPoint());

        if (item != null) {
            consumer.accept(item);
            activate(false);
        }
    }

    /**
     * Not used.
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Not used.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Not used.
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Not used.
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}