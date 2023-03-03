package ru.practicum.ewm.utls;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ValueApplier {
    public static <V> void applyNotNull(Consumer<V> applier, V value) {
        if (value == null) {
            return;
        }
        applier.accept(value);
    }

    public static <V, T> void applyNotNull(BiConsumer<T, V> applier, T target, V value) {
        if (value == null) {
            return;
        }
        applier.accept(target, value);
    }
}
