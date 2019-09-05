package de.devathlon.hnl.engine.internal.update;

/**
 * Class description.
 *
 * @author Paul2708
 */
public class EffectInformation {

    private final String description;
    private final String effect;

    public EffectInformation(String description, String effect) {
        this.description = description;
        this.effect = effect;
    }

    public String getDescription() {
        return description;
    }

    public String getEffect() {
        return effect;
    }
}
