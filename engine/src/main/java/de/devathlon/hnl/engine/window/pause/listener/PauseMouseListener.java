package de.devathlon.hnl.engine.window.pause.listener;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.engine.window.pause.PauseMenu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class description.
 *
 * @author Paul2708
 */
public final class PauseMouseListener implements MouseListener {

    private final PauseMenu pauseMenu;

    public PauseMouseListener(PauseMenu pauseMenu) {
        this.pauseMenu = pauseMenu;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        PauseItem item = pauseMenu.getItemByPoint(event.getPoint());

        if (item != null) {
            item.onSelect();
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
