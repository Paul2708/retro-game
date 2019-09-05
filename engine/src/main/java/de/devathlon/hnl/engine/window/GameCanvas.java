package de.devathlon.hnl.engine.window;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.engine.GameEngine;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.map.MapMenu;
import de.devathlon.hnl.engine.window.map.MapMouseListener;
import de.devathlon.hnl.engine.window.overlay.Overlay;
import de.devathlon.hnl.engine.window.overlay.impl.BackgroundOverlay;
import de.devathlon.hnl.engine.window.overlay.impl.BorderOverlay;
import de.devathlon.hnl.engine.window.overlay.impl.EffectBarOverlay;
import de.devathlon.hnl.engine.window.overlay.impl.FoodOverlay;
import de.devathlon.hnl.engine.window.overlay.impl.GameSettingsOverlay;
import de.devathlon.hnl.engine.window.overlay.impl.SnakeOverlay;
import de.devathlon.hnl.engine.window.score.Score;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This canvas will be used to render the map, snake and all other entities.
 *
 * @author Paul2708
 */
public final class GameCanvas extends Canvas {

    /**
     * Path to ground png.
     */
    private static final String GROUND_IMAGE = "/ground.png";

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

    private final MapModel mapModel;

    private final Image image;

    private final MapMenu mapMenu;

    private final Dimension dimension;

    private Score score;
    private String effect;

    private Map<Point, Color> colorMap;

    private Overlay backgroundOverlay;
    private Overlay borderOverlay;
    private Overlay snakeOverlay;
    private Overlay foodOverlay;
    private Overlay effectBarOverlay;
    private Overlay gameSettingsOverlay;

    /**
     * Create a new game canvas and read in the ground file.
     *
     * @param mapModel      model to get the current positions to draw
     * @param inputListener listener set to {@link CanvasKeyListener}
     */
    public GameCanvas(GameEngine engine, List<MapModel> mapModels, List<PauseItem> pauseItems, Dimension dimension, MapModel mapModel, InputListener inputListener) {
        this.mapModel = mapModel;
        this.dimension = dimension;

        // Load ground image
        try (InputStream stream = getClass().getResourceAsStream(GameCanvas.GROUND_IMAGE)) {
            this.image = ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.mapMenu = new MapMenu();
        this.mapMenu.load(mapModels, dimension);

        // Set key listener and canvas settings
        addKeyListener(new CanvasKeyListener(inputListener));
        addMouseListener(new MapMouseListener(mapMenu));
        setFocusable(true);

        this.score = new Score("High-Score: ", 0);
        this.effect = "kein Effekt";
        this.colorMap = new HashMap<>();


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

        this.gameSettingsOverlay = new GameSettingsOverlay();
        this.gameSettingsOverlay.initialize(engine, this);
        this.gameSettingsOverlay.activate(true);
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
        // graphics.setColor(Color.BLACK);
        // graphics.fillRect(0, 0, getWidth(), getHeight());

        // Draw ground
        // graphics.drawImage(image, 0, 0, (int) getSize().getWidth(), (int) getSize().getHeight(), this);

        backgroundOverlay.render((Graphics2D) graphics);


        // Draw border
        borderOverlay.render((Graphics2D) graphics);

        // Draw snake
        snakeOverlay.render((Graphics2D) graphics);

        // Draw food
        foodOverlay.render((Graphics2D) graphics);
        
        // Draw effect bar
        effectBarOverlay.render((Graphics2D) graphics);

        // Draw settings menu
        gameSettingsOverlay.render((Graphics2D) graphics);

        //graphics.drawString("Map-Auswahl", 100, 300);
        //graphics.drawString("Spiel beenden", 100, 400);

        // Draw map menu
        mapMenu.render((Graphics2D) graphics);

        // Draw score
        /*graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.drawString(score.getText() + " " + score.getScore(), (int) (dimension.getWidth() - 300), 30);
        graphics.drawString("Aktueller Score:" + score.getScore(), (int) (dimension.getWidth() - 300), 60);

        // Draw effect
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.drawString(effect, (int) (dimension.getWidth() - 750), 30);*/

        graphics.dispose();
        bufferStrategy.show();
    }

    public void setPause(boolean enabled) {
        gameSettingsOverlay.activate(enabled);
    }

    public void setSelection(boolean enabled, Consumer<String> consumer) {
        gameSettingsOverlay.activate(false);

        mapMenu.setPause(enabled);
        mapMenu.setConsumer(consumer);
    }

    public void setScore(Score score) {
        this.score = score;
    }

    /**
     * Transform a block point into an real sized screen point.
     *
     * @param point point to transform
     * @return transformed point
     */
    private Point transform(Point point) {
        int x = point.getX() * (BLOCK_SIZE + GAP);
        int y = (int) getSize().getHeight() - BLOCK_SIZE - (BLOCK_SIZE + GAP) * (point.getY());

        return Point.of(x, y);
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Dimension getGameDimension() {
        return dimension;
    }
}