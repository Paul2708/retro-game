package de.devathlon.hnl.engine.window;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.map.MapMenu;
import de.devathlon.hnl.engine.window.map.MapMouseListener;
import de.devathlon.hnl.engine.window.pause.PauseMenu;
import de.devathlon.hnl.engine.window.pause.listener.PauseMouseListener;
import de.devathlon.hnl.engine.window.score.Score;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

    private final PauseMenu pauseMenu;
    private final MapMenu mapMenu;

    private final Dimension dimension;

    private Score score;
    private String effect;

    private Map<Point, Color> colorMap;

    /**
     * Create a new game canvas and read in the ground file.
     *
     * @param mapModel      model to get the current positions to draw
     * @param inputListener listener set to {@link CanvasKeyListener}
     */
    public GameCanvas(List<MapModel> mapModels, List<PauseItem> pauseItems, Dimension dimension, MapModel mapModel, InputListener inputListener) {
        this.mapModel = mapModel;
        this.dimension = dimension;

        // Load ground image
        try (InputStream stream = getClass().getResourceAsStream(GameCanvas.GROUND_IMAGE)) {
            this.image = ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.pauseMenu = new PauseMenu();
        this.pauseMenu.load(pauseItems, dimension);

        this.mapMenu = new MapMenu();
        this.mapMenu.load(mapModels, dimension);

        // Set key listener and canvas settings
        addKeyListener(new CanvasKeyListener(inputListener));
        addMouseListener(new PauseMouseListener(pauseMenu));
        addMouseListener(new MapMouseListener(mapMenu));
        setFocusable(true);

        this.score = new Score("High-Score: ", 0);
        this.effect = "kein Effekt";
        this.colorMap = new HashMap<>();

        for (int i = 0; i < dimension.getWidth(); i += BLOCK_SIZE + GAP/2) {
            for (int j = 0; j < dimension.getHeight(); j += BLOCK_SIZE + GAP/2) {
                int random = new Random().nextInt(4);
                Color color = null;

                if (random == 0) {
                    color = new Color(102, 153, 51);
                } else if (random == 1) {
                    color = new Color(143, 197, 98);
                } else if (random == 2) {
                    color = new Color(72, 141, 29);
                } else if (random == 3) {
                    color = new Color(51, 153, 51);
                }

                this.colorMap.put(Point.of(i, j), color);
            }
        }
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
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // Draw ground
        // graphics.drawImage(image, 0, 0, (int) getSize().getWidth(), (int) getSize().getHeight(), this);

        for (Map.Entry<Point, Color> entry : colorMap.entrySet()) {
            graphics.setColor(entry.getValue());
            graphics.fillRect(entry.getKey().getX(), entry.getKey().getY(), BLOCK_SIZE + GAP / 2, BLOCK_SIZE + GAP / 2);
        }

        // Draw border
        graphics.setColor(new Color(139, 69, 19));

        for (Point point : mapModel.getBorder()) {
            Point transformed = transform(point);

            graphics.fillRect(transformed.getX(), transformed.getY(), BLOCK_SIZE, BLOCK_SIZE);
        }

        // Draw snake
        SnakeModel snakeModel = mapModel.getSnake();

        graphics.setColor(Color.WHITE);
        for (Point point : snakeModel.getBodyPoints()) {
            Point transform = transform(point);

            graphics.fillRect(transform.getX(), transform.getY(), BLOCK_SIZE, BLOCK_SIZE);
        }

        graphics.setColor(Color.RED);
        graphics.fillRect(transform(snakeModel.getHeadPoint()).getX(),
                transform(snakeModel.getHeadPoint()).getY(), BLOCK_SIZE, BLOCK_SIZE);

        // Draw food
        for (FoodModel foodModel : mapModel.getFood()) {
            Point transform = transform(foodModel.getLocation());

            graphics.setColor(foodModel.getColor());
            graphics.fillRect(transform.getX(), transform.getY(), BLOCK_SIZE, BLOCK_SIZE);
        }
        
        // Draw effect bar
        EffectBarModel effectBarModel = mapModel.getEffectBar();

        if (effectBarModel.isActive()) {
            graphics.setColor(effectBarModel.getColor());

            for (Point point : effectBarModel.getBar()) {
                Point transform = transform(point);
                graphics.fillRect(transform.getX(), transform.getY(), BLOCK_SIZE, BLOCK_SIZE);
            }
        }

        // Draw settings menu
        Font font;
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try (InputStream stream = getClass().getResourceAsStream("/pixel_font.ttf")) {
            font = Font.createFont(Font.TRUETYPE_FONT, stream);
            environment.registerFont(font);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        font = font.deriveFont(15f).deriveFont(Font.BOLD);
        pauseMenu.render((Graphics2D) graphics);

        //graphics.drawString("Map-Auswahl", 100, 300);
        //graphics.drawString("Spiel beenden", 100, 400);

        // Draw map menu
        mapMenu.render((Graphics2D) graphics);

        // Draw score
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.drawString(score.getText() + " " + score.getScore(), (int) (dimension.getWidth() - 300), 30);
        graphics.drawString("Aktueller Score:" + score.getScore(), (int) (dimension.getWidth() - 300), 60);

        // Draw effect
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.drawString(effect, (int) (dimension.getWidth() - 750), 30);

        graphics.dispose();
        bufferStrategy.show();
    }

    public void setPause(boolean enabled) {
        pauseMenu.setPause(enabled);
    }

    public void setSelection(boolean enabled, Consumer<String> consumer) {
        pauseMenu.setPause(false);

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
}