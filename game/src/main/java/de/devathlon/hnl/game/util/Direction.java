package de.devathlon.hnl.game.util;

import java.awt.event.KeyEvent;

/**
 * This enum holds key names and their KeyEvent equivalent.
 *
 * @author Leon
 */
public enum Direction {

    UP(new Integer[]{KeyEvent.VK_W, KeyEvent.VK_UP}),
    DOWN(new Integer[]{KeyEvent.VK_S, KeyEvent.VK_DOWN}),
    LEFT(new Integer[]{KeyEvent.VK_A, KeyEvent.VK_LEFT}),
    RIGHT(new Integer[]{KeyEvent.VK_D, KeyEvent.VK_RIGHT});

    /**
     * Each direction-key variable can have different keys assigned.
     */
    private Integer[] keys;

    /**
     * Constructor for this class.
     *
     * @param keys {@link KeyEvent}s'
     */
    Direction(Integer[] keys) {
        this.keys = keys;
    }

    /**
     * Returns all registered keys.
     *
     * @return Integer[] containing KeyEvents'.
     */
    public Integer[] getKeys() {
        return keys;
    }

    /**
     * Returns an object from the class Direction to identify whether a key
     * is assigned to a direction. The method returns null if there is no direction for an input.
     *
     * @param keyCode Input by client
     * @return Returns a direction related to the given parameter if it exists
     */
    public static Direction getDirectionByKey(int keyCode) {
        for (Direction direction : values()) {
            for (int i = 0; i < direction.keys.length; i++) {
                if (keyCode == direction.keys[i])
                    return direction;
            }
        }
        return null;
    }
}
