package de.devathlon.hnl.engine.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public final class GameCanvas extends Canvas {

    /**
     * Number of strategy buffers to create, including the front buffer
     */
    private static final int BUFFERS = 3;

    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(GameCanvas.BUFFERS);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(Color.RED);
        graphics.drawString("It works!", 100, 100);

        graphics.dispose();
        bufferStrategy.show();
    }
}