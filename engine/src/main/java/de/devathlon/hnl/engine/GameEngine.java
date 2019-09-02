package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;

public interface GameEngine {

    void setUp(EngineConfiguration configuration);

    void setModel(MapModel model);

    void setInputListener(InputListener listener);

    void start();

    void stop();
}