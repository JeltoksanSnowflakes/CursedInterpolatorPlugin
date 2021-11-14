package net.glasslauncher.cursedinterpolator.objects;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class ClassMappingEntry {

    @Getter
    private final String obfName;

    @Getter
    private final String intermediaryName;

    @Getter
    private final String cursedName;

    public ClassMappingEntry(@Nullable String obfName, @Nullable String intermediaryName, @Nullable String cursedName) {
        this.obfName = obfName;
        this.intermediaryName = intermediaryName;
        this.cursedName = cursedName;
    }
}
