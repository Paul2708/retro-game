package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;
import de.devathlon.hnl.game.map.CustomMap;
import de.devathlon.hnl.game.snake.Game;

/**
 * This class represents the button in order to change a map.
 *
 * @author Paul2708
 * @author Leon
 */
public class MapChangeItem implements PauseItem {

    private final Game game;

    /**
     * Creates an new map change item.
     *
     * @param game the running game
     */
    public MapChangeItem(Game game) {
        this.game = game;
    }

    /**
     * @return String with the button title
     */
    @Override
    public String getTitle() {
        return "Karte auswählen";
    }

    /**
     * Sets a new map if the user clicked on a map name.
     */
    @Override
    public void onSelect() {
        game.getGameEngine().openMapDialog(map -> {
            game.getGameEngine().setModel(map);
            game.setMapModel((CustomMap) map);
            game.getMapModel().setup();
            game.reset();
        });
    }
}
