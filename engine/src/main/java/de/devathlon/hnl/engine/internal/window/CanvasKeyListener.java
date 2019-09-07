package de.devathlon.hnl.engine.internal.window;

import de.devathlon.hnl.engine.listener.InputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This key handles user input from canvas and delegates to {@link InputListener}.
 *
 * @author Paul2708
 */
final class CanvasKeyListener implements KeyListener {

    private final InputListener inputListener;

    /**
     * Create a new canvas key listener.
     *
     * @param inputListener input listener that got called on user input
     */
    CanvasKeyListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    /**
     * Ignored as we do not use the event.
     *
     * @param e ignored
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Ignored
    }

    /**
     * Delegate the pressed key to {@link InputListener} set in constructor.
     *
     * @param event key event
     */
    @Override
    public void keyPressed(KeyEvent event) {
        inputListener.onInput(event.getKeyCode());
    }

    /**
     * Ignored as we do not use the event.
     *
     * @param e ignored
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Ignored
    }
}