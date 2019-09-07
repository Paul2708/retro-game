package de.devathlon.hnl.engine.internal;

import de.devathlon.hnl.core.MapModel;
import de.devathlon.hnl.engine.internal.update.EffectInformation;
import de.devathlon.hnl.engine.internal.update.Score;

import java.util.LinkedList;
import java.util.List;

/**
 * Pure data class that holds information about the game state.
 *
 * @author Paul2708
 */
public class GameState {

    private MapModel mapModel;
    private List<MapModel> mapPool;
    private Score score;
    private EffectInformation effectInformation;
    private boolean dead;

    private Boolean running;

    /**
     * Create a new game state with default values.
     */
    public GameState() {
        this.mapModel = null;
        this.mapPool = new LinkedList<>();
        this.score = new Score("Score:", 0);
        this.effectInformation = new EffectInformation("Effekt:", "-/-");
        this.dead = false;

        this.running = false;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public void setMapPool(List<MapModel> mapPool) {
        this.mapPool = mapPool;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public void setEffectInformation(EffectInformation effectInformation) {
        this.effectInformation = effectInformation;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public List<MapModel> getMapPool() {
        return mapPool;
    }

    public Score getScore() {
        return score;
    }

    public EffectInformation getEffectInformation() {
        return effectInformation;
    }

    public boolean isDead() {
        return dead;
    }

    public Boolean isRunning() {
        return running;
    }
}