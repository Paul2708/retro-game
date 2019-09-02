package de.devathlon.hnl.core.math;

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

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void set(int newX, int newY) {
        setX(newX);
        setY(newY);
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

    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;

        if (x != point.x) {
            return false;
        }

        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
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