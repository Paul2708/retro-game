package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.game.snake.Game;

import java.util.function.Consumer;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class MapPauseItem implements PauseItem {

    private Game game;

    public MapPauseItem(Game game) {
        this.game = game;
    }

    @Override
    public String getTitle() {
        return "Karte auswählen";
    }

    @Override
    public void onSelect() {
        game.getGameEngine().openMapDialog(s -> {
            System.out.println("Test" + s.getConfiguration().getName());
        });
    }
}
