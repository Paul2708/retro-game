package de.devathlon.hnl.engine.loop;

import de.devathlon.hnl.engine.window.GameCanvas;

/**
 * This runnable represents the game loop and updates every 1/fps second.
 *
 * @author Paul2708
 */
public final class GameLoop implements Runnable {

    private final GameCanvas canvas;
    private final Boolean running;
    private final int fps;

    /**
     * Create a new game loop with game canvas, running identifier and fps.
     *
     * @param canvas  used game canvas to render
     * @param running running identifier
     * @param fps     fps, updates every 1/fps second
     */
    public GameLoop(GameCanvas canvas, Boolean running, int fps) {
        this.canvas = canvas;
        this.running = running;
        this.fps = fps;
    }

    /**
     * Render the canvas every 1/fps second while the game is running.
     *
     * @see GameCanvas#render()
     */
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