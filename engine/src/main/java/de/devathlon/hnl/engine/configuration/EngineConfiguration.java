package de.devathlon.hnl.engine.configuration;

import de.devathlon.hnl.engine.window.GameCanvas;

import java.awt.Dimension;

public class EngineConfiguration {

    private final int widthInBlocks;
    private final int heightInBlocks;

    private final Dimension dimension;

    private final int fps;

    public EngineConfiguration(int widthBlocks, int heightBlocks, int fps) {
        this.widthInBlocks = widthBlocks;
        this.heightInBlocks = heightBlocks;

        this.dimension = new Dimension(transform(widthBlocks), transform(heightBlocks));

        this.fps = fps;
    }

    private int transform(int blocks) {
        return blocks * GameCanvas.BLOCK_SIZE + (blocks - 1) * GameCanvas.GAP;
    }

    public int getWidthInBlocks() {
        return widthInBlocks;
    }

    public int getHeightInBlocks() {
        return heightInBlocks;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getFps() {
        return fps;
    }
}