package de.devathlon.hnl.engine.window;

import de.devathlon.hnl.engine.listener.InputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CanvasKeyListener implements KeyListener {

    private InputListener inputListener;

    CanvasKeyListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Ignored
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputListener.onInput(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Ignored
    }
}