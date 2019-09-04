package de.devathlon.hnl.game;

import de.devathlon.hnl.game.snake.Game;

/**
 * This is the main class.
 *
 * @author Leon
 */
public class Launcher {

    private static Game game;

    public static void main(String[] args) {
        game = new Game();
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Launcher.game = game;
    }
}
