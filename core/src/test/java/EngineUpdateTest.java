import de.devathlon.hnl.core.update.EngineUpdate;
import org.junit.Test;

/**
 * This test class tests {@link EngineUpdate#verify(Object...)}.
 *
 * @author Paul2708
 */
public final class EngineUpdateTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyParameter() {
        EngineUpdate update = EngineUpdate.TEST;

        update.verify();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParameter() {
        EngineUpdate update = EngineUpdate.TEST;

        update.verify((Object) null);
    }

    @Test
    public void testCorrectParsing() {
        EngineUpdate update = EngineUpdate.TEST;

        update.verify(42, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongParameter() {
        EngineUpdate update = EngineUpdate.TEST;

        update.verify("test", 42);
    }
}