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

    private final Dimension dimension;

    private Overlay backgroundOverlay;
    private Overlay borderOverlay;
    private Overlay snakeOverlay;
    private Overlay foodOverlay;
    private Overlay effectBarOverlay;
    private Overlay scoreOverlay;
    private Overlay effectInfoOverlay;
    private Overlay gameSettingsOverlay;
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

        this.backgroundOverlay = new BackgroundOverlay();
        this.backgroundOverlay.initialize(engine, this);
        this.backgroundOverlay.activate(true);

        this.borderOverlay = new BorderOverlay();
        this.borderOverlay.initialize(engine, this);
        this.borderOverlay.activate(true);

        this.snakeOverlay = new SnakeOverlay();
        this.snakeOverlay.initialize(engine, this);
        this.snakeOverlay.activate(true);

        this.foodOverlay = new FoodOverlay();
        this.foodOverlay.initialize(engine, this);
        this.foodOverlay.activate(true);

        this.effectBarOverlay = new EffectBarOverlay();
        this.effectBarOverlay.initialize(engine, this);
        this.effectBarOverlay.activate(true);

        this.scoreOverlay = new ScoreOverlay();
        this.scoreOverlay.initialize(engine, this);
        this.scoreOverlay.activate(true);

        this.effectInfoOverlay = new EffectInfoOverlay();
        this.effectInfoOverlay.initialize(engine, this);
        this.effectInfoOverlay.activate(true);

        this.gameSettingsOverlay = new GameSettingsOverlay();
        this.gameSettingsOverlay.initialize(engine, this);
        this.gameSettingsOverlay.activate(false);

        this.mapSelectionOverlay = new MapSelectionOverlay();
        this.mapSelectionOverlay.initialize(engine, this);
        this.mapSelectionOverlay.activate(false);
    }

    /**
     * Render the game.
     * It includes border, snake, items and scores.
     *
     * @see GameLoop#run()
     */
    public void render() {
        // TODO: Optimize me
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(GameCanvas.BUFFERS);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        backgroundOverlay.render((Graphics2D) graphics);


        // Draw border
        borderOverlay.render((Graphics2D) graphics);

        // Draw snake
        snakeOverlay.render((Graphics2D) graphics);

        // Draw food
        foodOverlay.render((Graphics2D) graphics);
        
        // Draw effect bar
        effectBarOverlay.render((Graphics2D) graphics);

        // Draw score overlay
        scoreOverlay.render((Graphics2D) graphics);

        effectInfoOverlay.render(((Graphics2D) graphics));

        // Draw settings menu
        gameSettingsOverlay.render((Graphics2D) graphics);

        // Draw map menu
        mapSelectionOverlay.render(((Graphics2D) graphics));

        // Draw score

        // Draw effect

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