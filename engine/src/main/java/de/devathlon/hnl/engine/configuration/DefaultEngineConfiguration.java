package de.devathlon.hnl.engine.configuration;

/**
 * This {@link EngineConfiguration} can be used as default engine configuration.
 * It creates a small game map with 30 FPS.
 *
 * @author Paul2708
 */
public class DefaultEngineConfiguration extends EngineConfiguration {

    /**
     * Create a new default configuration with a small map and 30 fps.
     */
    public DefaultEngineConfiguration() {
        super(20, 35, 30);
    }
}