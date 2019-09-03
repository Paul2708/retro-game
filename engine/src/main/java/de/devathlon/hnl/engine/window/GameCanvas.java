package de.devathlon.hnl.engine.window;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.listener.InputListener;
import sun.awt.image.ImageWatched;
import sun.security.tools.policytool.PolicyTool;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        setFocusable(true);
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

        List<Point> corners = mapModel.getBorder();
        List<Point> border = new LinkedList<>();

        for (int i = 0; i < corners.size() - 1; i++) {
            border.addAll(getPoints(corners.get(i), corners.get(i + 1)));
        }
        border.addAll(getPoints(corners.get(corners.size() - 1), corners.get(0)));

        for (Point point : border) {
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

        graphics.drawString("It works!", 100, 100);

        graphics.dispose();
        bufferStrategy.show();
    }

    private Point transform(Point point) {
        int x = point.getX() * (BLOCK_SIZE + GAP);
        int y = (int) getSize().getHeight() - BLOCK_SIZE - (BLOCK_SIZE + GAP) * (point.getY());

        return Point.of(x, y);
    }

    private List<Point> getPoints(Point start, Point end) {
        // TODO: Refactor me
        List<Point> points = new LinkedList<>();

        if (start.getX() == end.getX()) {
            int delta = (int) Math.signum(Integer.compare(end.getY(), start.getY()));

            Point point = start;
            while (!point.equals(end)) {
                point = Point.of(start.getX(), point.getY() + delta);
                points.add(point);
            }
        } else if (start.getY() == end.getY()) {
            int delta = (int) Math.signum(Integer.compare(end.getX(), start.getX()));

            Point point = start;
            while (!point.equals(end)) {
                point = Point.of(point.getX() + delta, start.getY());
                points.add(point);
            }
        } else {
            throw new UnsupportedOperationException("Only rectangle corners are allowed.");
        }

        return points;
    }
}