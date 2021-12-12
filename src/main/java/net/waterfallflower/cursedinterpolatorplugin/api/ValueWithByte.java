package net.waterfallflower.cursedinterpolatorplugin.api;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class ValueWithByte<T> {

    @Getter private final byte byteVar;
    @Getter @Nullable private final T value;

    public ValueWithByte(byte b, @Nullable T t) {
        this.byteVar = b;
        this.value = t;
    }

    public ValueWithByte(int i, @Nullable T t) {
        this((byte)i, t);
    }

    public ValueWithByte(boolean b, @Nullable T t) {
        this((byte) (b ? 1 : 0), t);
    }

}
