package de.devathlon.hnl.engine.internal.window;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.internal.loop.GameLoop;
import de.devathlon.hnl.engine.internal.window.overlay.Overlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.BackgroundOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.BorderOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.EffectBarOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.EffectInfoOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.FoodOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.GameSettingsOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.MapSelectionOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.ScoreOverlay;
import de.devathlon.hnl.engine.internal.window.overlay.impl.SnakeOverlay;

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

    private static final Overlay[] OVERLAYS = new Overlay[] {
            new BackgroundOverlay(),
            new BorderOverlay(),
            new SnakeOverlay(),
            new FoodOverlay(),
            new EffectBarOverlay(),
            new ScoreOverlay(),
            new EffectInfoOverlay(),
            new GameSettingsOverlay(),
            new MapSelectionOverlay()
    };

    private final Dimension dimension;

    private GameSettingsOverlay gameSettingsOverlay;
    private MapSelectionOverlay mapSelectionOverlay;

    /**
     * Create a new game canvas and read in the ground file.
     *
     * @param inputListener listener set to {@link CanvasKeyListener}
     */
    public GameCanvas(GameEngine engine, Dimension dimension, InputListener inputListener) {
        this.dimension = dimension;

        // Set key listener and canvas settings
        addKeyListener(new CanvasKeyListener(inputListener));
        setFocusable(true);

        // Initialize overlays
        this.gameSettingsOverlay = (GameSettingsOverlay) GameCanvas.OVERLAYS[7];
        this.mapSelectionOverlay = (MapSelectionOverlay) GameCanvas.OVERLAYS[8];

        for (Overlay overlay : GameCanvas.OVERLAYS) {
            overlay.initialize(engine, this);
            overlay.activate(true);
        }

        gameSettingsOverlay.activate(false);
        mapSelectionOverlay.activate(false);
    }

    /**
     * Render the game.
     * It includes border, snake, items and scores.
     *
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

    public void setPause(boolean enabled) {
        gameSettingsOverlay.activate(enabled);
    }

    public void setSelection(boolean enabled, Consumer<MapModel> consumer) {
        mapSelectionOverlay.setConsumer(consumer);

        gameSettingsOverlay.activate(false);

        mapSelectionOverlay.activate(enabled);
    }

    public Dimension getGameDimension() {
        return dimension;
    }
}