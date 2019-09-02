package de.devathlon.hnl.core.math;

/**
 * Class description.
 *
 * @author Paul2708
 */
public final class Point {

    private int x;
    private int y;

    /**
     * Create a new 2D point.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updateX(int deltaX) {
        this.x +=  deltaX;
    }

    public void updateY(int deltaY) {
        this.y += deltaY;
    }

    public void update(int deltaX, int deltaY) {
        updateX(deltaX);
        updateY(deltaY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Create a new 2D point.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return created point
     */
    public static Point of(int x, int y) {
        return new Point(x, y);
    }
}