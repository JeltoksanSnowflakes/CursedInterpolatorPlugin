package net.waterfallflower.cursedinterpolatorplugin.api.utils;

public class InitAndApply {
    public interface ConsumerT<T> {
        void apply(T t);
    }

    public static <T> T get(T t, ConsumerT<T> c) {
        c.apply(t);
        return t;
    }

}
