package de.devathlon.hnl.core.update;

/**
 * This enum holds all engine updates.
 * An engine update can be triggered by game engine from game module.
 * It will be used as observer update.
 *
 * @author Paul2708
 */
public enum EngineUpdate {

    /**
     * Test update. Will be used to test the verify method.
     */
    TEST(Integer.class, String.class),
    SCORE_UPDATE(String.class, Integer.class);

    private final Class<?>[] classes;

    /**
     * Create a new engine update.
     *
     * @param classes classes of passed arguments
     */
    EngineUpdate(Class<?>... classes) {
        if (classes == null) {
            this.classes = new Class<?>[0];
        } else {
            this.classes = classes;
        }
    }

    /**
     * Verify if the given arguments matches with the defined argument classes from the enum.
     * This ensures that the arguments can be passed correctly.
     *
     * @param arguments arguments
     */
    public void verify(Object... arguments) {
        // Check if none arguments were passed
        if (arguments == null) {
            if (classes.length == 0) {
                return;
            } else {
                throw new IllegalArgumentException("The update expects " + classes.length + " parameters.");
            }
        }

        // Validate arguments
        if (classes.length != arguments.length) {
            throw new IllegalArgumentException("The update expects " + classes.length + " parameters.");
        }
        for (int i = 0; i < classes.length; i++) {
            if (!classes[i].isInstance(arguments[i])) {
                throw new IllegalArgumentException("False parameter. Expected " + classes[i] + ", but got "
                        + classes.getClass() + ".");
            }
        }
    }
}