package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.GameWindow;

import java.awt.event.WindowEvent;

final class GameEngineImpl implements GameEngine {

    private MapModel mapModel;
    private InputListener inputListener;

    private GameWindow gameWindow;
    private GameCanvas gameCanvas;

    private Thread loopThread;

    GameEngineImpl() {

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
        this.gameCanvas = new GameCanvas(mapModel, inputListener);
        this.gameWindow = new GameWindow(configuration.getDimension(), "Snake", gameCanvas);
    }

    @Override
    public void start() {
        gameWindow.setVisible(true);

        this.loopThread = new Thread(new GameLoop(gameCanvas, true, 30));
        loopThread.start();
    }

    @Override
    public void stop() {
        loopThread.interrupt();
        gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
    }
}