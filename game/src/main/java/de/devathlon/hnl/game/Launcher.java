package de.devathlon.hnl.game;

import de.devathlon.hnl.game.snake.Game;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class Launcher {

    private static Game game;

    public static void main(String[] args) {
        game = new Game();
    }

    public static Game getGame() {
        return game;
    }
}
