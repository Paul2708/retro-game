package de.devathlon.hnl.engine.internal.window.overlay;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.internal.window.GameCanvas;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class description.
 *
 * @author Paul2708
 */
public abstract class Overlay {

    private static final String FONT_PATH = "/pixel_font.ttf";

    private static Font font;

    private GameEngine engine;
    private GameCanvas canvas;

    private boolean enabled;

    public void initialize(GameEngine engine, GameCanvas canvas) {
        this.engine = engine;
        this.canvas = canvas;

        this.enabled = false;

        this.onInitialize();
    }

    public void render(Graphics2D graphics) {
        if (isEnabled()) {
            onRender(graphics);
        }
    }

    public void activate(boolean activate) {
        this.enabled = activate;
    }

    protected abstract void onInitialize();

    protected abstract void onRender(Graphics2D graphics);

    protected MapModel getMap() {
        return engine.getGameState().getMapModel();
    }

    protected Font getFont() {
        if (Overlay.font == null) {
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

            try (InputStream stream = getClass().getResourceAsStream(Overlay.FONT_PATH)) {
                font = Font.createFont(Font.TRUETYPE_FONT, stream);
                environment.registerFont(font);
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
        }

        return Overlay.font;
    }

    /**
     * Transform a block point into an real sized screen point.
     *
     * @param point point to transform
     * @return transformed point
     */
    protected Point transform(de.devathlon.hnl.core.math.Point point) {
        int x = point.getX() * (GameCanvas.BLOCK_SIZE + GameCanvas.GAP);
        int y = (int) canvas.getGameDimension().getHeight() - GameCanvas.BLOCK_SIZE
                - (GameCanvas.BLOCK_SIZE + GameCanvas.GAP) * (point.getY());

        return Point.of(x, y);
    }

    protected boolean isEnabled() {
        return enabled;
    }

    protected GameEngine getEngine() {
        return engine;
    }

    protected GameCanvas getCanvas() {
        return canvas;
    }
}