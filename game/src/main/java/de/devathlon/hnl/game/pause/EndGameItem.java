package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.game.snake.Game;

/**
 * This class represents the button in order to close the application.
 *
 * @author Paul2708
 * @author Leon
 */
public class EndGameItem implements PauseItem {

    private final Game game;

    /**
     * Creates an new end game item.
     *
     * @param game the running game
     */
    public EndGameItem(Game game) {
        this.game = game;
    }

    /**
     * @return String with the button title
     */
    @Override
    public String getTitle() {
        return "Spiel beenden";
    }


    /**
     * Stops the game engine and exists the application
     */
    @Override
    public void onSelect() {
        game.getGameEngine().stop();
        System.exit(0);
    }
}
