package de.devathlon.hnl.engine.loop;

import de.devathlon.hnl.engine.window.GameCanvas;

/**
 * Class description.
 *
 * @author Paul2708
 */
public final class GameLoop implements Runnable {

    private GameCanvas canvas;
    private Boolean running;
    private int fps;

    public GameLoop(GameCanvas canvas, Boolean running, int fps) {
        this.canvas = canvas;
        this.running = running;
        this.fps = fps;
    }

    @Override
    public void run() {
        while (running) {
            canvas.render();

            try {
                Thread.sleep((long) 1000.0 / fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}