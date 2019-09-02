package de.devathlon.hnl.engine.window;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.listener.InputListener;

import java.awt.*;
import java.awt.image.BufferStrategy;

public final class GameCanvas extends Canvas {

    /**
     * Number of strategy buffers to create, including the front buffer
     */
    private static final int BUFFERS = 3;

    public static final int BLOCK_SIZE = 20;

    public static final int GAP = 2;

    private MapModel mapModel;

    public GameCanvas(MapModel mapModel, InputListener inputListener) {
        this.mapModel = mapModel;

        addKeyListener(new CanvasKeyListener(inputListener));
    }

    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(GameCanvas.BUFFERS);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // Draw border
        graphics.setColor(Color.BLUE);

        for (Point point : mapModel.getBorder()) {
            Point transform = transform(point);

            graphics.fillRect(transform.getX(), transform.getY(), BLOCK_SIZE, BLOCK_SIZE);
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

        graphics.drawString("It works!", 100, 100);

        graphics.dispose();
        bufferStrategy.show();
    }

    private Point transform(Point point) {
        int x = point.getX() == 0 ? 0 : point.getX() * (BLOCK_SIZE + GAP);
        int y = (int) getSize().getHeight() - BLOCK_SIZE - (BLOCK_SIZE + GAP) * (point.getY());

        return Point.of(x, y);
    }
}