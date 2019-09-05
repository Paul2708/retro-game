package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.game.snake.Game;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class EndGameItem implements PauseItem {

    private Game game;

    public EndGameItem(Game game) {
        this.game = game;
    }

    @Override
    public String getTitle() {
        return "Spiel beenden";
    }

    @Override
    public void onSelect() {
        game.getGameEngine().stop();
        System.exit(0);
    }
}
