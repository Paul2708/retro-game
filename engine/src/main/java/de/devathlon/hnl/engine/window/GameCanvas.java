package de.devathlon.hnl.engine.window;

import de.devathlon.hnl.core.EffectBarModel;
import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;

import javax.imageio.ImageIO;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;

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

    /**
     * Create a new game canvas and read in the ground file.
     *
     * @param mapModel      model to get the current positions to draw
     * @param inputListener listener set to {@link CanvasKeyListener}
     */
    public GameCanvas(MapModel mapModel, InputListener inputListener) {
        this.mapModel = mapModel;

        try (InputStream stream = getClass().getResourceAsStream(GameCanvas.GROUND_IMAGE)) {
            this.image = ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addKeyListener(new CanvasKeyListener(inputListener));
        setFocusable(true);
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
        graphics.drawImage(image, 0, 0, (int) getSize().getWidth(), (int) getSize().getHeight(), this);

        // Draw border
        graphics.setColor(new Color(139, 69, 19));

        for (Point point : mapModel.getBorder()) {
            Point transformed = transform(point);

            graphics.fillRect(transformed.getX(), transformed.getY(), BLOCK_SIZE, BLOCK_SIZE);
        }

        // Draw snake
        graphics.setColor(Color.RED);

        SnakeModel snakeModel = mapModel.getSnake();

        graphics.fillRect(transform(snakeModel.getHeadPoint()).getX(),
                transform(snakeModel.getHeadPoint()).getY(), BLOCK_SIZE, BLOCK_SIZE);

        graphics.setColor(Color.WHITE);
        for (Point point : snakeModel.getBodyPoints()) {
            Point transform = transform(point);

            graphics.fillRect(transform.getX(), transform.getY(), BLOCK_SIZE, BLOCK_SIZE);
        }

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

        graphics.dispose();
        bufferStrategy.show();
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
}