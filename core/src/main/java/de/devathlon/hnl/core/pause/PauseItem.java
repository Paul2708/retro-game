package de.devathlon.hnl.core.pause;

/**
 * This interface represents a pause item.
 * A pause item will be displayed, if the user pauses the game.
 *
 * @author Paul2708
 */
public interface PauseItem {

    /**
     * Get the displayed pause item title.
     *
     * @return displayed title
     */
    String getTitle();

    /**
     * Got called, if the user selects this option.
     */
    void onSelect();
}
