package de.devathlon.hnl.engine.update;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class Score {

    private String text;
    private long score;

    public Score(String text, long score) {
        this.text = text;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public long getScore() {
        return score;
    }
}
