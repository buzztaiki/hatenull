package buzztaiki.hatenull.example;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.Test;

public class ExampleTest {
    @Test public void plain() throws Exception {
        Method m = Example.class.getDeclaredMethod("plain", Object.class);
        assertTrue(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void nonnull() throws Exception {
        Method m = Example.class.getDeclaredMethod("nonnull", Object.class);
        assertTrue(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void nullable() throws Exception {
        Method m = Example.class.getDeclaredMethod("nullable", Object.class);
        assertTrue(m.isAnnotationPresent(Nullable.class));
        assertFalse(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void checkForNull() throws Exception {
        Method m = Example.class.getDeclaredMethod("checkForNull", Object.class);
        assertTrue(m.isAnnotationPresent(CheckForNull.class));
        assertFalse(m.isAnnotationPresent(Nonnull.class));
    }

    @Test public void plainField() throws Exception {
        Field f = Example.class.getDeclaredField("plain");
        assertTrue(f.isAnnotationPresent(Nonnull.class));
    }

    @Test public void nonnullField() throws Exception {
        Field f = Example.class.getDeclaredField("nonnull");
        assertTrue(f.isAnnotationPresent(Nonnull.class));
    }

    @Test public void nullableField() throws Exception {
        Field f = Example.class.getDeclaredField("nullable");
        assertTrue(f.isAnnotationPresent(Nullable.class));
        assertFalse(f.isAnnotationPresent(Nonnull.class));
    }

    @Test public void checkForNullField() throws Exception {
        Field f = Example.class.getDeclaredField("checkForNull");
        assertTrue(f.isAnnotationPresent(CheckForNull.class));
        assertFalse(f.isAnnotationPresent(Nonnull.class));
    }
}
