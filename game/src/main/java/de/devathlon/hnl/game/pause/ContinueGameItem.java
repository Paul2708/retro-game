package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.game.Launcher;
import de.devathlon.hnl.game.snake.Game;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class ContinueGameItem implements PauseItem {

    private Game game;

    public ContinueGameItem(Game game) {
        this.game = game;
    }

    @Override
    public String getTitle() {
        return "Spiel fortfahren";
    }

    @Override
    public void onSelect() {
        game.pauseGame();
    }
}
