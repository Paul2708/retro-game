package de.devathlon.hnl.game;

import de.devathlon.hnl.game.snake.Game;

/**
 * This is the main class which runs the game.
 *
 * @author Leon
 */
public class Launcher {

    private static Game game;

    /**
     * Main method. Creates a new game object.
     *
     * @param args ignored command line arguments
     */
    public static void main(String[] args) {
        game = new Game();
    }


    /**
     * Returns game object.
     *
     * @return game
     */
    public static Game getGame() {
        return game;
    }

    /**
     * Set a new game object.
     *
     * @param game instance
     */
    public static void setGame(Game game) {
        Launcher.game = game;
    }
}
