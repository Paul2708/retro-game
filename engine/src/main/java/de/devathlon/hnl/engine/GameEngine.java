package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.core.update.EngineUpdate;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;

/**
 * This interface represents the actual game engine.
 * It will be used in the game module to start.
 *
 * @author Paul2708
 */
public interface GameEngine {

    /**
     * Set the underlying model to get the positions of snake, border and entites.
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

    void pause();

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
     * Create the game engine implementation.
     *
     * @return engine implementation
     */
    static GameEngine create() {
        return new GameEngineImpl();
    }
}