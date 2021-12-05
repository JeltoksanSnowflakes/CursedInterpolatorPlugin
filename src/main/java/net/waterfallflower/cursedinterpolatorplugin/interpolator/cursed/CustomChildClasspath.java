package net.waterfallflower.cursedinterpolatorplugin.interpolator.cursed;

import lombok.Getter;
import net.waterfallflower.cursedinterpolatorplugin.api.IndirectUse;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Weird way to bypass java9+ things.
 */
@Deprecated
@SuppressWarnings({"deprecation", "all"})
public class CustomChildClasspath {

    @Getter
    protected URLClassLoader CLASSLOADER_;

    public @NotNull URL[] toURL(@NotNull File... f) throws MalformedURLException {
        URL[] u = new URL[f.length];
        for(byte i = 0; i < f.length; i++)
            u[i] = f[i].toURI().toURL();
        return u;
    }

    public void addToClasspath(@NotNull File... f) {
        try {
            CLASSLOADER_ = new URLClassLoader(toURL(f), UnknownUnexpectedAppletMinecraft.class.getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException("Cannot load library from jar file '" + Arrays.toString(f) + "'. \nReason: " + e.getMessage());
        }
    }

    public void addLibraryPath(@NotNull String registerLocation) {
        try {
            final Field usr_paths = ClassLoader.class.getDeclaredField("usr_paths");
            usr_paths.setAccessible(true);

            String[] paths = (String[])usr_paths.get(null);

            for(String path : paths)
                if(path.equals(registerLocation)) return;

            String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
            newPaths[newPaths.length-1] = registerLocation;

            usr_paths.set(null, newPaths);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public @NotNull Class<?> fromName(@NotNull String s) throws ClassNotFoundException {
        return Class.forName(s, true, CLASSLOADER_);
    }

    public CustomChildClasspath() {
        register();
    }

    /**
     * Used in anonymous classes or in stuff that doesn't directly extend this class.
     */
    @IndirectUse
    protected File[] register() {
        return null;
    }
}
