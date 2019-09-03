package de.devathlon.hnl.engine.window;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Canvas;
import java.awt.Dimension;

/**
 * This frame holds the {@link GameCanvas} and shows the game to the user.
 *
 * @author Paul2708
 */
public final class GameWindow extends JFrame {

    /**
     * Create a new game window.
     * Set the fame size, title and other settings.
     *
     * @param dimension canvas and frame size
     * @param title     frame title
     * @param canvas    game canvas
     * @see GameCanvas
     */
    public GameWindow(Dimension dimension, String title, Canvas canvas) {
        super(title);

        canvas.setPreferredSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setMinimumSize(dimension);

        add(canvas);
        setResizable(false);
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}