package de.devathlon.hnl.game.util;

import java.awt.event.KeyEvent;

public enum Direction {

    UP(new Integer[]{1, 2}),
    DOWN(new Integer[]{1, 2}),
    LEFT(new Integer[]{1, 2}),
    RIGHT(new Integer[]{1, 2});

    private Integer[] keys;

    Direction(Integer[] keys) {
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
