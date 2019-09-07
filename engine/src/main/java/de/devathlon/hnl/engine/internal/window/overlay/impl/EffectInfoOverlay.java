package de.devathlon.hnl.engine.internal.window.overlay.impl;

import de.devathlon.hnl.engine.internal.update.EffectInformation;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * This overlay draws the effect bar information.
 *
 * @author Paul2708
 */
public class EffectInfoOverlay extends Overlay {

    private Font font;

    /**
     * Adjust the font.
     */
    @Override
    public void onInitialize() {
        this.font = getFont().deriveFont(15f).deriveFont(Font.BOLD);
    }

    /**
     * Draw the information as string to the screen.
     *
     * @param graphics graphics
     */
    @Override
    public void onRender(Graphics2D graphics) {
        Dimension dimension = getCanvas().getGameDimension();
        EffectInformation effect = getEngine().getGameState().getEffectInformation();

        graphics.setColor(getEngine().getGameState().isDead() ? Color.WHITE : Color.BLACK);

        graphics.setFont(font);
        graphics.drawString(effect.getDescription(), (int) (dimension.getWidth() - 750), 30);
        graphics.drawString(effect.getEffect(), (int) (dimension.getWidth() - 750), 60);
    }
}