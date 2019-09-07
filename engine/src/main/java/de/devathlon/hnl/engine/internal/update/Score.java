package de.devathlon.hnl.engine.internal.update;

/**
 * This class holds information about the current player score.
 *
 * @author Paul2708
 */
public class Score {

    private final String text;
    private final long score;

    /**
     * Create a new score with text description and value score.
     *
     * @param text  text description
     * @param score actual value
     */
    public Score(String text, long score) {
        this.text = text;
        this.score = score;
    }

    /**
     * Get the text description.
     *
     * @return text description
     */
    public String getText() {
        return text;
    }

    /**
     * Get the actual score value.
     *
     * @return score value
     */
    public long getScore() {
        return score;
    }
}