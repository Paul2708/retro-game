package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.GameWindow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the actual implementation of the game engine.
 *
 * @author Paul2708
 */
final class GameEngineImpl implements GameEngine {

    private MapModel mapModel;
    private InputListener inputListener;
    private EngineConfiguration configuration;

    private GameWindow gameWindow;
    private GameCanvas gameCanvas;

    private Boolean running;

    private List<PauseItem> pauseItems;

    /**
     * Private constructor.
     * Set running identifier to <code>false</code>.
     */
    GameEngineImpl() {
        this.running = false;
        this.pauseItems = new LinkedList<>();
    }

    /**
     * Set the underlying model to get the positions of snake, border and entites.
     *
     * @param model map model implementation
     * @see MapModel
     */
    @Override
    public void setModel(MapModel model) {
        this.mapModel = model;
    }

    /**
     * Set the pause items for the pause menu.
     * It also provides a listener that got called if the item gets clicked.
     *
     * @param items array of pause items
     */
    @Override
    public void setPauseItems(PauseItem... items) {
        pauseItems.addAll(Arrays.asList(items));
    }

    /**
     * Set the input listener that will be called on key input.
     *
     * @param listener input listener
     */
    @Override
    public void setInputListener(InputListener listener) {
        this.inputListener = listener;
    }

    /**
     * Setup the engine by a given configuration.
     * It has to be called after {@link #setModel(MapModel)} and {@link #setInputListener(InputListener)}.
     *
     * @param configuration engine configuration
     * @see EngineConfiguration
     */
    @Override
    public void setUp(EngineConfiguration configuration) {
        this.configuration = configuration;

        this.gameCanvas = new GameCanvas(pauseItems, configuration.getDimension(), mapModel, inputListener);
        this.gameWindow = new GameWindow(configuration.getDimension(), "Snake", gameCanvas);
    }

    /**
     * Start the engine.
     * It opens the frame and starts to render the game map.
     */
    @Override
    public void start() {
        running = true;

        gameWindow.setVisible(true);
        gameCanvas.requestFocus();

        Thread loopThread = new Thread(new GameLoop(gameCanvas, running, configuration.getFps()));
        loopThread.start();
    }

    @Override
    public void pause() {
        gameCanvas.setPause(true);
    }

    @Override
    public void unpause() {
        gameCanvas.setPause(false);
    }

    /**
     * Used to update the game view by game module.
     * Verify the arguments to ensure that they are valid parameters.
     *
     * @param update    update enum type
     * @param arguments arguments that matches the parameter classes
     */
    @Override
    public void update(EngineUpdate update, Object... arguments) {
        try {
            update.verify(arguments);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }

        switch (update) {
            case SCORE_UPDATE:
                String title = (String) arguments[0];
                int score = (int) arguments[1];

                // TODO: Implement me

                break;
            default:
                break;
        }
    }

    /**
     * Stop the engine.
     * The rendering stops and the window disappears.
     * <p>
     * The process will be still active.
     */
    @Override
    public void stop() {
        running = false;

        gameWindow.dispose();
    }
}