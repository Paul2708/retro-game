package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.GameWindow;

final class GameEngineImpl implements GameEngine {

    private MapModel mapModel;
    private InputListener inputListener;
    private EngineConfiguration configuration;

    private GameWindow gameWindow;
    private GameCanvas gameCanvas;

    private Boolean running;


    GameEngineImpl() {
        this.running = false;
    }

    @Override
    public void setModel(MapModel model) {
        this.mapModel = model;
    }

    @Override
    public void setInputListener(InputListener listener) {
        this.inputListener = listener;
    }

    @Override
    public void setUp(EngineConfiguration configuration) {
        this.configuration = configuration;

        this.gameCanvas = new GameCanvas(mapModel, inputListener);
        this.gameWindow = new GameWindow(configuration.getDimension(), "Snake", gameCanvas);
    }

    @Override
    public void start() {
        running = true;

        gameWindow.setVisible(true);
        gameCanvas.requestFocus();

        Thread loopThread = new Thread(new GameLoop(gameCanvas, running, configuration.getFps()));
        loopThread.start();
    }

    @Override
    public void stop() {
        running = false;

        gameWindow.dispose();
    }
}