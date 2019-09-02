package de.devathlon.hnl.game.entities;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.math.Point;

import java.awt.*;
import java.util.Random;

public class Food implements FoodModel {

    private Point location;
    private Color color;

    public Food(int x, int y, Color color) {
        location.setX(x);
        location.setY(y);
        this.color = color;
    }


    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
