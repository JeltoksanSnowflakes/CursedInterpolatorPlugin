package net.waterfallflower.cursedinterpolatorplugin.api;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class TwoValueWithByte<T, K> {

    @Getter private final byte byteValue;
    @Getter @Nullable private final T firstValue;
    @Getter @Nullable private final K secondValue;

    public TwoValueWithByte(byte b, @Nullable T t, @Nullable K k) {
        this.byteValue = b;
        this.firstValue = t;
        this.secondValue = k;
    }

    public TwoValueWithByte(int i, @Nullable T t, @Nullable K k) {
        this((byte)i, t, k);
    }

    public TwoValueWithByte(boolean b, @Nullable T t, @Nullable K k) {
        this((byte) (b ? 1 : 0), t, k);
    }
}
