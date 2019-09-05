package de.devathlon.hnl.engine.internal.window.map;

import de.devathlon.hnl.core.MapModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class description.
 *
 * @author Paul2708
 */
public final class MapMouseListener implements MouseListener {

    private final MapMenu menu;

    public MapMouseListener(MapMenu mapMenu) {
        this.menu = mapMenu;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!menu.isEnabled()) {
            return;
        }

        MapModel item = menu.getItemByPoint(event.getPoint());

        if (item != null) {
            // TODO: Select map
            menu.getConsumer().accept(item.getConfiguration().getName());
            menu.setPause(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
