package de.devathlon.hnl.engine.window.overlay;

import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.window.GameCanvas;

import java.awt.*;
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

    public void activate() {
        this.enabled = true;
    }

    public abstract void onInitialize();

    public abstract void render(Graphics2D graphics);

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

    public boolean isEnabled() {
        return enabled;
    }

    protected GameEngine getEngine() {
        return engine;
    }

    protected GameCanvas getCanvas() {
        return canvas;
    }
}