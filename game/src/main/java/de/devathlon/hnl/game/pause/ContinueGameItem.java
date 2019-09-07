package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.game.snake.Game;

/**
 * This class represents the button in order to continue the game.
 *
 * @author Paul2708
 * @author Leon
 */
public class ContinueGameItem implements PauseItem {

    private Game game;

    /**
     * Creates an new end game item.
     *
     * @param game the running game
     */
    public ContinueGameItem(Game game) {
        this.game = game;
    }

    /**
     * @return String with the button title
     */
    @Override
    public String getTitle() {
        return "Spiel fortfahren";
    }

    /**
     * Unpauses the game by changing the value of the pause variable.
     */
    @Override
    public void onSelect() {
        game.pauseGame();
    }
}
