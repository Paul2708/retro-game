package de.devathlon.hnl.engine.internal.window;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.internal.window.overlay.impl.*;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.internal.loop.GameLoop;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.function.Consumer;

/**
 * This canvas will be used to render the map, snake and all other entities.
 *
 * @author Paul2708
 */
public final class GameCanvas extends Canvas {

    /**
     * Number of strategy buffers to create, including the front buffer
     */
    private static final int BUFFERS = 3;

    /**
     * Size in pixel that one single block has.
     * The block will be shown as square.
     */
    public static final int BLOCK_SIZE = 20;

    /**
     * Size in pixel between to blocks.
     */
    public static final int GAP = 2;

    private static final Overlay[] OVERLAYS = new Overlay[]{
            new BackgroundOverlay(),
            new BorderOverlay(),
            new FoodOverlay(),
            new DeathOverlay(),
            new SnakeOverlay(),
            new EffectBarOverlay(),
            new ScoreOverlay(),
            new EffectInfoOverlay(),
            new GameSettingsOverlay(),
            new MapSelectionOverlay()
    };

    private final Dimension dimension;

    private final BackgroundOverlay backgroundOverlay;
    private final GameSettingsOverlay gameSettingsOverlay;
    private final MapSelectionOverlay mapSelectionOverlay;

    /**
     * Create a new game canvas and initialize all overlays.
     *
     * @param engine        game engine
     * @param dimension     game size in absolute size
     * @param inputListener listener set to {@link CanvasKeyListener}
     */
    public GameCanvas(GameEngine engine, Dimension dimension, InputListener inputListener) {
        this.dimension = dimension;

        // Set key listener and canvas settings
        addKeyListener(new CanvasKeyListener(inputListener));
        setFocusable(true);

        // Initialize overlays
        this.backgroundOverlay = (BackgroundOverlay) GameCanvas.OVERLAYS[0];
        this.gameSettingsOverlay = (GameSettingsOverlay) GameCanvas.OVERLAYS[8];
        this.mapSelectionOverlay = (MapSelectionOverlay) GameCanvas.OVERLAYS[9];

        for (Overlay overlay : GameCanvas.OVERLAYS) {
            overlay.initialize(engine, this);
            overlay.activate(true);
        }

        gameSettingsOverlay.activate(false);
        mapSelectionOverlay.activate(false);
    }

    /**
     * Render the game.
     * It includes all given overlays.
     *
     * @see Overlay#render(Graphics2D)
     * @see GameLoop#run()
     */
    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(GameCanvas.BUFFERS);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        for (Overlay overlay : GameCanvas.OVERLAYS) {
            overlay.render((Graphics2D) graphics);
        }

        graphics.dispose();
        bufferStrategy.show();
    }

    /**
     * Enables or disables the pause screen.
     *
     * @param enabled true to enable, false to disable
     */
    public void enablePause(boolean enabled) {
        gameSettingsOverlay.activate(enabled);
        mapSelectionOverlay.activate(false);
    }

    /**
     * Enables or disables the map selection screen.
     *
     * @param enabled  true to enable, false to disable
     * @param consumer consumer that consumes a map model if the map got selected
     */
    public void enableMapSelection(boolean enabled, Consumer<MapModel> consumer) {
        mapSelectionOverlay.setConsumer(consumer);

        gameSettingsOverlay.activate(false);

        mapSelectionOverlay.activate(enabled);
    }

    /**
     * Refresh the background.
     */
    public void refreshBackground() {
        backgroundOverlay.refresh();
    }

    /**
     * Get the game dimension in absolute coordinates.
     *
     * @return dimension
     */
    public Dimension getGameDimension() {
        return dimension;
    }
}