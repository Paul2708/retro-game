package de.devathlon.hnl.engine.window.overlay.impl;

import de.devathlon.hnl.engine.window.overlay.Overlay;
import de.devathlon.hnl.engine.window.score.Score;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class EffectInfoOverlay extends Overlay {

    private Font font;

    @Override
    public void onInitialize() {
        this.font = getFont().deriveFont(15f).deriveFont(Font.BOLD);
    }

    @Override
    public void onRender(Graphics2D graphics) {
        Dimension dimension = getCanvas().getGameDimension();
        String effect = getEngine().getEffect();

        graphics.setColor(Color.BLACK);

        graphics.setFont(font);
        graphics.drawString(effect, (int) (dimension.getWidth() - 750), 30);
    }
}