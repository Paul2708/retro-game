package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;

public interface GameEngine {

    void setModel(MapModel model);

    void setInputListener(InputListener listener);

    void setUp(EngineConfiguration configuration);

    void start();

    void stop();

    static GameEngine create() {
        return new GameEngineImpl();
    }
}