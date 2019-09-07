package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Graphics2D;

/**
 * This overlay draws the effect bar.
 *
 * @author Paul2708
 */
public class EffectBarOverlay extends Overlay {

    private EffectBarModel effectBar;

    /**
     * Get the current effect bar model.
     */
    @Override
    public void onInitialize() {
        this.effectBar = getMap().getEffectBar();
    }

    /**
     * Draw the effect bar.
     *
     * @param graphics graphics
     */
    @Override
    public void onRender(Graphics2D graphics) {
        graphics.setColor(effectBar.getColor());

        for (Point point : effectBar.getBar()) {
            Point transform = transform(point);

            graphics.fillRect(transform.getX(), transform.getY(), GameCanvas.BLOCK_SIZE, GameCanvas.BLOCK_SIZE);
        }
    }

    /**
     * The overlay is as long active as the effect bar is active.
     *
     * @return true if the effect bar is active, otherwise false
     */
    @Override
    public boolean isEnabled() {
        return effectBar.isActive();
    }
}