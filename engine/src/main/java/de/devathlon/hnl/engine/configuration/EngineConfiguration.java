package de.devathlon.hnl.engine.configuration;

import java.awt.*;

public class EngineConfiguration {

    private final Dimension dimension;
    private final int fps;

    public EngineConfiguration(Dimension dimension, int fps) {
        this.dimension = dimension;
        this.fps = fps;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getFps() {
        return fps;
    }
}