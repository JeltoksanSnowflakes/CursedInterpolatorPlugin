package net.waterfallflower.cursedinterpolatorplugin.api.utils;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class TwoValuesWithByte<T, K> {

    @Getter private final byte byteValue;
    @Getter @Nullable private final T firstValue;
    @Getter @Nullable private final K secondValue;

    public TwoValuesWithByte(byte b, @Nullable T t, @Nullable K k) {
        this.byteValue = b;
        this.firstValue = t;
        this.secondValue = k;
    }

    public TwoValuesWithByte(int i, @Nullable T t, @Nullable K k) {
        this((byte)i, t, k);
    }

    public TwoValuesWithByte(boolean b, @Nullable T t, @Nullable K k) {
        this((byte) (b ? 1 : 0), t, k);
    }
}
