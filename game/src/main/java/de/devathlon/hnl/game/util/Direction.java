package de.devathlon.hnl.game.util;


import java.awt.event.KeyEvent;

public enum Direction {

    UP(new Integer[]{KeyEvent.VK_W, KeyEvent.VK_UP}),
    DOWN(new Integer[]{KeyEvent.VK_S, KeyEvent.VK_DOWN}),
    LEFT(new Integer[]{KeyEvent.VK_A, KeyEvent.VK_LEFT}),
    RIGHT(new Integer[]{KeyEvent.VK_D, KeyEvent.VK_RIGHT});

    private Integer[] keys;

    Direction(Integer[] keys) {
        this.keys = keys;
    }

    public Integer[] getKeys() {
        return keys;
    }

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
