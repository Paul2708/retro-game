package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.internal.GameState;
import de.devathlon.hnl.engine.listener.InputListener;

import java.util.List;
import java.util.function.Consumer;

/**
 * This interface represents the actual game engine.
 * It will be used in the game module to start.
 *
 * @author Paul2708
 */
public interface GameEngine {

    /**
     * Set the underlying model to get the positions of snake, border and entities.
     *
     * @param model map model implementation
     * @see MapModel
     */
    void setModel(MapModel model);

    /**
     * Set the pause items for the pause menu.
     * It also provides a listener that got called if the item gets clicked.
     *
     * @param items array of pause items
     */
    void setPauseItems(PauseItem... items);

    /**
     * Set a list of all maps that represent the map pool.
     * The maps will be displayed if the user wants to change the map.
     *
     * @param mapPool list of maps
     */
    void setMaps(List<MapModel> mapPool);

    /**
     * Open the map selection dialog and consume the selected map.
     *
     * @param mapConsumer consumer that consumes the selected map model
     */
    void openMapDialog(Consumer<MapModel> mapConsumer);

    /**
     * Set the input listener that will be called on key input.
     *
     * @param listener input listener
     */
    void setInputListener(InputListener listener);

    /**
     * Setup the engine by a given configuration.
     * It has to be called after {@link #setModel(MapModel)} and {@link #setInputListener(InputListener)}.
     *
     * @param configuration engine configuration
     * @see EngineConfiguration
     */
    void setUp(EngineConfiguration configuration);

    /**
     * Start the engine.
     * It opens the frame and starts to render the game map.
     */
    void start();

    /**
     * Pause the game.
     */
    void pause();

    /**
     * Unpause the game.
     */
    void unpause();

    /**
     * Used to update the game view by game module.
     * Verify the arguments to ensure that they are valid parameters.
     *
     * @param update    update enum type
     * @param arguments arguments that matches the parameter classes
     */
    void update(EngineUpdate update, Object... arguments);

    /**
     * Stop the engine.
     * The rendering stops and the window disappears.
     * <p>
     * The process will be still active.
     */
    void stop();

    /**
     * Get a list of all pause items.
     * They will displayed if the users pauses the game.
     *
     * @return list of pause items
     */
    List<PauseItem> getPauseItems();

    /**
     * Get the internal game state.
     * This method shouln't be called outside the engine module!
     * It's for internal purposes only.
     *
     * @return internal game state
     */
    GameState getGameState();

    /**
     * Create the game engine implementation.
     *
     * @return engine implementation
     */
    static GameEngine create() {
        return new GameEngineImpl();
    }
}