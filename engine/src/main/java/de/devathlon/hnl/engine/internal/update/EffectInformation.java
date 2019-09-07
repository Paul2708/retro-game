package de.devathlon.hnl.engine.internal.update;

/**
 * This data class holds information about the current active effect.
 *
 * @author Paul2708
 */
public class EffectInformation {

    private final String description;
    private final String effect;

    /**
     * Create a new effect information.
     *
     * @param description effect description
     * @param effect      actual effect type
     */
    public EffectInformation(String description, String effect) {
        this.description = description;
        this.effect = effect;
    }

    /**
     * Get the effect description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the actual effect type.
     *
     * @return effect type
     */
    public String getEffect() {
        return effect;
    }
}
