package de.devathlon.hnl.game.snake;

import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.game.entities.Snake;
import de.devathlon.hnl.game.util.Direction;

import java.awt.event.KeyEvent;

public class Game implements InputListener {

    private Snake snake;

    private Direction currentDirection;

    public Game() {
        setup();
    }

    private void setup() {
        // initialize snake
        this.snake = new Snake();
    }

    private void updateSnakePosition() {
    }

    private void pauseGame() {

    }

    @Override
    public void onInput(int keyCode) {
        if (keyCode != KeyEvent.VK_SPACE || keyCode != KeyEvent.VK_ESCAPE) {
            Direction direction = Direction.getDirectionByKey(keyCode);
            if (direction != null) {
                currentDirection = direction;
            }
        } else {
            pauseGame();
        }
    }
}
