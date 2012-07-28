package buzztaiki.hatenull.example;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Method;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.Test;

public class ExampleTest {
    @Test public void plain() throws Exception {
        Method m = Example.class.getMethod("plain", Object.class);
        assertTrue(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void nonnull() throws Exception {
        Method m = Example.class.getMethod("nonnull", Object.class);
        assertTrue(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void nullable() throws Exception {
        Method m = Example.class.getMethod("nullable", Object.class);
        assertTrue(m.isAnnotationPresent(Nullable.class));
        assertFalse(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void checkForNull() throws Exception {
        Method m = Example.class.getMethod("checkForNull", Object.class);
        assertTrue(m.isAnnotationPresent(CheckForNull.class));
        assertFalse(m.isAnnotationPresent(Nonnull.class));
    }
}
