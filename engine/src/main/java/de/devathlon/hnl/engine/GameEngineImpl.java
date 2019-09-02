package de.devathlon.hnl.engine;

import de.devathlon.hnl.core.FoodModel;
import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.core.SnakeModel;
import de.devathlon.hnl.core.math.Point;
import de.devathlon.hnl.engine.configuration.DefaultEngineConfiguration;
import de.devathlon.hnl.engine.configuration.EngineConfiguration;
import de.devathlon.hnl.engine.listener.InputListener;
import de.devathlon.hnl.engine.loop.GameLoop;
import de.devathlon.hnl.engine.window.GameCanvas;
import de.devathlon.hnl.engine.window.GameWindow;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

final class GameEngineImpl implements GameEngine {

    private MapModel mapModel;
    private InputListener inputListener;

    private GameWindow gameWindow;
    private GameCanvas gameCanvas;

    GameEngineImpl() {

    }

    @Override
    public void setModel(MapModel model) {
        this.mapModel = model;
    }

    @Override
    public void setInputListener(InputListener listener) {
        this.inputListener = listener;
    }

    @Override
    public void setUp(EngineConfiguration configuration) {
        this.gameCanvas = new GameCanvas(mapModel, inputListener);
        this.gameWindow = new GameWindow(configuration.getDimension(), "Snake", gameCanvas);
    }

    @Override
    public void start() {
        gameWindow.setVisible(true);

        new Thread(new GameLoop(gameCanvas, true, 30)).start();
    }

    @Override
    public void stop() {

    }

    public static Point HEAD = Point.of(5, 5);

    public static void main(String[] args) {
        GameEngineImpl engine = new GameEngineImpl();
        engine.setModel(new MapModel() {
            @Override
            public Collection<Point> getBorder() {
                return new HashSet<>();
            }

            @Override
            public SnakeModel getSnake() {
                return new SnakeModel() {
                    @Override
                    public Point getHeadPoint() {
                        return HEAD;
                    }

                    @Override
                    public Collection<Point> getBodyPoints() {
                        Set<Point> set = new HashSet<>();
                        set.add(Point.of(5, 4));
                        set.add(Point.of(5, 3));
                        set.add(Point.of(4, 3));
                        set.add(Point.of(3, 3));
                        return set;
                    }
                };
            }

            @Override
            public Collection<FoodModel> getFood() {
                return null;
            }
        });
        engine.setInputListener(keyCode -> {
            System.out.println("Pressed: " + KeyEvent.getKeyText(keyCode));
        });
        engine.setUp(new DefaultEngineConfiguration());
        engine.start();

        new Thread(() -> {
            while (true) {
                GameEngineImpl.HEAD.updateY(1);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}