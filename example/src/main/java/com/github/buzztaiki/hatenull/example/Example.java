package com.github.buzztaiki.hatenull.example;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Example {
    private final String plain = null;
    private final @Nonnull String nonnull = null;
    private final @Nullable String nullable = null;
    private final @CheckForNull String checkForNull = null;

    public static void main(String[] args) throws Exception {
        String lplain = null;
        @Nonnull String lnonnull = null;
        @Nullable String lnullable = null;
        @CheckForNull String lcheckForNull = null;

        System.out.println(plain(null));
        System.out.println(nullable(null));
        System.out.println(nonnull(null));
    }

    public static Object plain(Object o) {
        return o;
    }

    public static @Nullable Object nullable(@Nullable Object o) {
        return o;
    }

    public static @CheckForNull Object checkForNull(@CheckForNull Object o) {
        return o;
    }


    public static @Nonnull Object nonnull(@Nonnull Object o) {
        return o;
    }
}
