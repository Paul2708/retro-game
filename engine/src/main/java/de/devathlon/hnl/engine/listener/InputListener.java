package de.devathlon.hnl.engine.listener;

/**
 * This listener got called if the user presses a key on the keyboard.
 * It is used to react on user input in the game module.
 *
 * @author Paul2708
 */
public interface InputListener {

    /**
     * The user pressed the key with the given key code.
     * The key code can be compared with {@link java.awt.event.KeyEvent#VK_0} etc.
     *
     * @param keyCode pressed key
     */
    void onInput(int keyCode);
}
