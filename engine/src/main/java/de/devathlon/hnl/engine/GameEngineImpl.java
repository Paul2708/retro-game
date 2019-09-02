package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.GameWindow;

final class GameEngineImpl implements GameEngine {

    private GameWindow gameWindow;
    private GameCanvas gameCanvas;

    private MapModel mapModel;

    private GameEngineImpl() {

    }

    @Override
    public void setUp(EngineConfiguration configuration, MapModel mapModel) {
        this.gameCanvas = new GameCanvas(mapModel);
        this.gameWindow = new GameWindow(configuration.getDimension(), "Snake", gameCanvas);
    }

    @Override
    public void setInputListener(InputListener listener) {

    }

    @Override
    public void start() {
        gameWindow.setVisible(true);

        new Thread(new GameLoop(gameCanvas, true, 30)).start();
    }

    @Override
    public void stop() {

    }
}