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
 * This abstract class represents an overlay.
 * An overlay can draw to the screen.
 *
 * @author Paul2708
 */
public abstract class Overlay {

    private static final String FONT_PATH = "/pixel_font.ttf";

    private static Font font;

    private GameEngine engine;
    private GameCanvas canvas;

    private boolean enabled;

    /**
     * Initialize the overlay by setting engine and canvas and calling {@link #onInitialize()}.
     *
     * @param engine game engine
     * @param canvas game canvas
     */
    public void initialize(GameEngine engine, GameCanvas canvas) {
        this.engine = engine;
        this.canvas = canvas;

        this.enabled = false;

        this.onInitialize();
    }

    /**
     * Render the overlay if it's active.
     *
     * @param graphics graphics
     */
    public void render(Graphics2D graphics) {
        if (isEnabled()) {
            onRender(graphics);
        }
    }

    /**
     * Enable or disable the overlay.
     * It indicates if the overlay will be drawn or not.
     *
     * @param activate activate or disable
     */
    public void activate(boolean activate) {
        this.enabled = activate;
    }

    /**
     * Initialize the overlay.
     * This got called once.
     */
    protected abstract void onInitialize();

    /**
     * Render the overlay to the screen.
     * It got called every render-tick.
     *
     * @param graphics graphics
     */
    protected abstract void onRender(Graphics2D graphics);

    /**
     * Get the current map.
     *
     * @return map model
     */
    protected MapModel getMap() {
        return engine.getGameState().getMapModel();
    }

    /**
     * Get the used font.
     * If the font is not used yet, an instance will be created and the font got leaded.
     *
     * @return pixel font
     */
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

    /**
     * Check if the overlay is enabled or not.
     *
     * @return enabled status
     */
    protected boolean isEnabled() {
        return enabled;
    }

    /**
     * Get the game engine.
     *
     * @return game engine
     */
    protected GameEngine getEngine() {
        return engine;
    }

    /**
     * Get the game canvas.
     *
     * @return game canvas
     */
    protected GameCanvas getCanvas() {
        return canvas;
    }
}