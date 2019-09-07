package de.devathlon.hnl.engine.internal.window.overlay.impl;

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

/**
 * This overlay represents the pause or game settings overlay.
 * It holds {@link PauseItem}s that are displayed.
 *
 * @author Paul2708
 */
public class GameSettingsOverlay extends Overlay implements MouseListener {

    // TODO: Center items (more) correctly

    private Font font;

    private List<PauseItem> items;
    private Map<PauseItem, Rectangle> rectangles;

    /**
     * Adjust the font, get all pause items and calculate the clickable borders for them.
     */
    @Override
    public void onInitialize() {
        this.font = getFont().deriveFont(25f).deriveFont(Font.BOLD);

        this.items = getEngine().getPauseItems();
        Collections.reverse(items);

        this.rectangles = new HashMap<>();
        for (PauseItem item : items) {
            rectangles.put(item, calculateBorder(item));
        }
    }

    /**
     * Draw the items.
     *
     * @param graphics graphics
     * @see #drawItem(Graphics2D, PauseItem)
     */
    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);

        for (PauseItem item : items) {
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
     * Get the item by point.
     *
     * @param point clicked point
     * @return pause item or null if none was found
     */
    private PauseItem getItemByPoint(Point point) {
        for (Map.Entry<PauseItem, Rectangle> entry : rectangles.entrySet()) {
            if (entry.getValue().contains(point)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Calculate the border a given pause item.
     *
     * @param item pause item
     * @return clickable rectangle area
     */
    private Rectangle calculateBorder(PauseItem item) {
        FontRenderContext renderContext = new FontRenderContext(null, true, true);
        Rectangle2D bounds = font.getStringBounds(item.getTitle(), renderContext);

        Dimension dimension = getCanvas().getGameDimension();

        int x = (int) ((dimension.getWidth() / 2) - (bounds.getWidth() / 2) - bounds.getX());
        int y = (int) ((dimension.getHeight() / 2) - (bounds.getHeight() / 2) - bounds.getY());

        return new Rectangle(x, y - items.indexOf(item) * 100 - (int) bounds.getHeight(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    /**
     * Draw the item to the screen.
     *
     * @param graphics graphics
     * @param item     pause item
     */
    private void drawItem(Graphics2D graphics, PauseItem item) {
        Rectangle rectangle = rectangles.get(item);

        graphics.drawString(item.getTitle(), (int) rectangle.getX(), (int) rectangle.getY()
                + (int) rectangle.getHeight());
    }

    /**
     * Check if the player clicked a pause item.
     *
     * @param event mouse event
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        PauseItem item = getItemByPoint(event.getPoint());

        if (item != null) {
            item.onSelect();
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