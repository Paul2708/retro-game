package de.devathlon.hnl.game.pause;

import de.devathlon.hnl.core.pause.PauseItem;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class ContinuePauseItem implements PauseItem {

    @Override
    public String getTitle() {
        return "Spiel fortfahren";
    }

    @Override
    public void onSelect() {
        // TODO: Leon, implement me
        System.out.println("Spiel fortfahren.");
    }
}
