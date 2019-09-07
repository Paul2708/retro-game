package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.internal.GameState;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.internal.loop.GameLoop;
import de.devathlon.hnl.engine.internal.update.EffectInformation;
import de.devathlon.hnl.engine.internal.window.GameCanvas;
import de.devathlon.hnl.engine.internal.window.GameWindow;
import de.devathlon.hnl.engine.internal.update.Score;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is the actual implementation of the game engine.
 *
 * @author Paul2708
 */
final class GameEngineImpl implements GameEngine {

    private InputListener inputListener;
    private EngineConfiguration configuration;

    private GameWindow gameWindow;
    private GameCanvas gameCanvas;

    private final List<PauseItem> pauseItems;

    private final GameState state;


    /**
     * Private constructor.
     * Set running identifier to <code>false</code>.
     */
    GameEngineImpl() {
        this.pauseItems = new LinkedList<>();

        this.state = new GameState();
    }

    /**
     * Set the underlying model to get the positions of snake, border and entities.
     *
     * @param model map model implementation
     * @see MapModel
     */
    @Override
    public void setModel(MapModel model) {
        state.setMapModel(model);
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
     * Set a list of all maps that represent the map pool.
     * The maps will be displayed if the user wants to change the map.
     *
     * @param mapPool list of maps
     */
    @Override
    public void setMaps(List<MapModel> mapPool) {
        state.setMapPool(mapPool);
    }

    /**
     * Open the map selection dialog and consume the selected map.
     *
     * @param mapConsumer consumer that consumes the selected map model
     */
    @Override
    public void openMapDialog(Consumer<MapModel> mapConsumer) {
        gameCanvas.enableMapSelection(true, mapConsumer);
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

        this.gameCanvas = new GameCanvas(this, configuration.getDimension(), inputListener);
        this.gameWindow = new GameWindow(configuration.getDimension(), "Snake", gameCanvas);
    }

    /**
     * Start the engine.
     * It opens the frame and starts to render the game map.
     */
    @Override
    public void start() {
        state.setRunning(true);

        gameWindow.setVisible(true);
        gameCanvas.requestFocus();

        Thread loopThread = new Thread(new GameLoop(gameCanvas, state.isRunning(), configuration.getFps()));
        loopThread.start();
    }

    /**
     * Pause the game.
     */
    @Override
    public void pause() {
        gameCanvas.enablePause(true);
    }

    /**
     * Unpause the game.
     */
    @Override
    public void unpause() {
        gameCanvas.enablePause(false);
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

                state.setScore(new Score(title, score));
                break;
            case EFFECT_UPDATE:
                EffectInformation effect = new EffectInformation((String) arguments[0], (String) arguments[1]);
                state.setEffectInformation(effect);
                break;
            case DEATH_SCREEN:
                state.setDead((Boolean) arguments[0]);
                break;
            case REFRESH_BACKGROUND:
                gameCanvas.refreshBackground();
                break;
            default:
                break;
        }
    }

    /**
     * Get a list of all pause items.
     * They will displayed if the users pauses the game.
     *
     * @return list of pause items
     */
    @Override
    public List<PauseItem> getPauseItems() {
        return pauseItems;
    }

    /**
     * Get the internal game state.
     * This method shouln't be called outside the engine module!
     * It's for internal purposes only.
     *
     * @return internal game state
     */
    @Override
    public GameState getGameState() {
        return state;
    }

    /**
     * Stop the engine.
     * The rendering stops and the window disappears.
     * <p>
     * The process will be still active.
     */
    @Override
    public void stop() {
        state.setRunning(false);

        gameWindow.dispose();
    }
}