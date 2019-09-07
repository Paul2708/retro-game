package de.devathlon.hnl.core.math;

/**
 * This class models a 2D point.
 *
 * @author Paul2708
 */
public final class Point implements Cloneable {

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

    /**
     * Set the x coordinate to a new value.
     *
     * @param newX new x coordinate
     */
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * Set the y coordinate to a new value.
     *
     * @param newY new y coordinate
     */
    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * Set new coordinate values.
     *
     * @param newX new x coordinate
     * @param newY new y coordinate
     */
    public void set(int newX, int newY) {
        setX(newX);
        setY(newY);
    }

    /**
     * Update the x coordinate by a delta.
     *
     * @param deltaX delta x
     */
    public void updateX(int deltaX) {
        this.x += deltaX;
    }

    /**
     * Update the y coordinate by a delta.
     *
     * @param deltaY delta y
     */
    public void updateY(int deltaY) {
        this.y += deltaY;
    }

    /**
     * Update the coordinates by relative values.
     *
     * @param deltaX delta x
     * @param deltaY delta y
     */
    public void update(int deltaX, int deltaY) {
        updateX(deltaX);
        updateY(deltaY);
    }

    /**
     * Get the x coordinate.
     *
     * @return x value
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate.
     *
     * @return y value
     */
    public int getY() {
        return y;
    }

    /**
     * Get the point as readable string.
     *
     * @return point as string
     */
    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }

    /**
     * Check if two points are equal.
     * Two points are equal, if their coordinates are the same.
     *
     * @param o object to check
     * @return true if the objects are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    /**
     * Get the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Clone the current point object with x and y coordinate.
     *
     * @return cloned point
     */
    @Override
    public Point clone() {
        return Point.of(x, y);
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