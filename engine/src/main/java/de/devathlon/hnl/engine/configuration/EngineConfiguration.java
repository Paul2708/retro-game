package de.devathlon.hnl.engine.configuration;

import de.devathlon.hnl.engine.internal.window.GameCanvas;

import java.awt.Dimension;

/**
 * This class holds configuration settings for {@link de.devathlon.hnl.engine.GameEngine}.
 * They can be applied before starting the engine.
 * <p>
 * It contains information like game size and fps.
 *
 * @author Paul2708
 */
public class EngineConfiguration {

    // TODO: Use map size instead

    private final int widthInBlocks;
    private final int heightInBlocks;

    private final Dimension dimension;

    private final int fps;

    /**
     * Create a new engine configuration.
     * The real {@link Dimension} will be calculated based on given block size and {@link GameCanvas#BLOCK_SIZE}
     * and {@link GameCanvas#GAP}.
     *
     * @param widthBlocks  screen width in game blocks
     * @param heightBlocks screen height in game blocks
     * @param fps          frames per seconds, the screen will update <code>1/fps</code> seconds
     */
    public EngineConfiguration(int widthBlocks, int heightBlocks, int fps) {
        this.widthInBlocks = widthBlocks;
        this.heightInBlocks = heightBlocks;

        this.dimension = new Dimension(transform(widthBlocks), transform(heightBlocks));

        this.fps = fps;
    }

    /**
     * Transform given amount in blocks to real screen size.
     * It's based on {@link GameCanvas#GAP} and {@link GameCanvas#BLOCK_SIZE}.
     *
     * @param blocks amount of blocks
     * @return transformed real screen size
     */
    private int transform(int blocks) {
        return blocks * GameCanvas.BLOCK_SIZE + (blocks - 1) * GameCanvas.GAP;
    }

    /**
     * Get the width in blocks.
     *
     * @return width in blocks
     */
    public int getWidthInBlocks() {
        return widthInBlocks;
    }

    /**
     * Get the height in blocks.
     *
     * @return height in blocks
     */
    public int getHeightInBlocks() {
        return heightInBlocks;
    }

    /**
     * Get the calculated dimension based on size in blocks.
     *
     * @return calculated dimension
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Get the frames per second.
     * The screen will update <code>1/fps</code> seconds.
     *
     * @return fps
     */
    public int getFps() {
        return fps;
    }
}